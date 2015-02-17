package com.github.cuter44.muuga.shelf.servlet;

import java.util.List;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.getLongList;
import static com.github.cuter44.nyafx.servlet.Params.getStringList;
import static com.github.cuter44.nyafx.servlet.Params.getByte;
import static com.github.cuter44.nyafx.servlet.Params.getInt;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.conf.Configurator;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;


/** 搜索/列出藏书
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /book/search.api

   <strong>参数</strong>
   <i>以下零至多个参数组, 按参数名分组, 组内以,分隔以or逻辑连接, 组间以and逻辑连接, 完全匹配</i>
   id       :long   , id of Book;
   owner    :long   , owner id of Book3;
   isbn     :string , 指定isbn;
   <i>以下单个参数</i>
   status   :byte   , status of book, refer to {@link com.github.cuter44.muuga.shelf.model.Book Book}
   <i>分页</i>
   start:int, 返回结果的起始笔数, 缺省从 1 开始
   size:int, 返回结果的最大笔数, 缺省使用服务器配置

   <strong>响应</strong>
   application/json; array; class={@link Json#jsonizeBook(Book) class=shelf.model.Book}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/book/search.api")
public class BookSearch extends HttpServlet
{
    private static final String ID = "id";
    private static final String OWNER = "owner";
    private static final String ISBN = "isbn";
    private static final String STATUS = "status";

    private static final String START = "start";
    private static final String SIZE = "size";

    private static final String ORDER = "order";
    private static final String BY = "by";

    private static final Integer defaultPageSize = Configurator.getInstance().getInt("librarica.search.defaultpagesize", 20);

    protected BookDao bookDao = BookDao.getInstance();

    /** 将参数翻译为 Criteria<Book>
     * @param dc Criteria<Book>. If null, construct one.
     * @param req params to parse.
     */
    public static DetachedCriteria parseCriteria(DetachedCriteria dc, HttpServletRequest req)
    {
        if (dc == null)
            dc = DetachedCriteria.forClass(Book.class);

        List<Long> ids = getLongList(req, ID);
        if (ids!=null && ids.size()>0)
            dc.add(Restrictions.in("id", ids));

        List<String> isbns = getStringList(req, ISBN);
        if (isbns!=null && isbns.size()>0)
            dc.add(Restrictions.in("isbn", isbns));

        Byte status = getByte(req, STATUS);
        if (status != null)
            dc.add(Restrictions.eq("status", status));

        List<Long> owners = getLongList(req, OWNER);
        if (owners!=null && owners.size()>0)
        {
            dc.createAlias("owner", "owner")
                .add(Restrictions.in("owner.id", owners));
        }

        return(dc);
    }

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            DetachedCriteria dc = parseCriteria(null, req);

            Integer start   = getInt(req, START);
            Integer size    = getInt(req, SIZE);
                    size    = size!=null?size:defaultPageSize;

            this.bookDao.begin();

            List<Book> l = (List<Book>)this.bookDao.search(dc, start, size);

            this.bookDao.commit();

            Json.writeBook(l, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.bookDao.close();
        }

        return;
    }
}
