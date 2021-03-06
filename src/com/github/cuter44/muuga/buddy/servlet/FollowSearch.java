package com.github.cuter44.muuga.buddy.servlet;

import java.util.List;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import static com.github.cuter44.nyafx.servlet.Params.*;
import org.hibernate.criterion.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.buddy.model.*;
import com.github.cuter44.muuga.buddy.core.*;
import com.github.cuter44.muuga.conf.*;

/** 搜索关注者/被关注者/检查关注关系
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /buddy/follow/search.api

   <strong>参数</strong>
   me       :long[] , 可选, 逗号分隔, 关注方的uid
   op       :long[] , 可选, 逗号分隔, 被关注的uid
   <i>分页</i>
   start    :int    , 返回结果的起始笔数, 缺省从 0 开始
   size     :int    , 返回结果的最大笔数, 缺省使用服务器配置
   <i>排序</i>
   by       :string             , 按该字段...
   order    :string=asc|desc    , 顺序|逆序排列

   <strong>响应</strong>
   application/json ; array; class={@link Json#jsonizeFollow(com.github.cuter44.muuga.buddy.model.Follow) buddy.model.Follow}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/buddy/follow/search.api")
public class FollowSearch extends HttpServlet
{
    private static final String ME      = "me";
    private static final String OP      = "op";
    private static final String START   = "start";
    private static final String SIZE    = "size";
    private static final String ORDER   = "order";
    private static final String BY      = "by";

    private static final Integer defaultPageSize = Configurator.getInstance().getInt("com.cuter44.nyagurufx.search.defaultpagesize", 20);

    protected FollowDao followDao = FollowDao.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        this.doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            List<Long>  me      = getLongList(req, ME);
            List<Long>  op      = getLongList(req, OP);
            Integer     start   = getInt(req, START);
            Integer     size    = getInt(req, SIZE);
                        size    = size!=null?size:defaultPageSize;
            String      order   = getString(req, ORDER);
            String      by      = getString(req, BY);


            DetachedCriteria dc = DetachedCriteria.forClass(com.github.cuter44.muuga.buddy.model.Follow.class);

            if (me != null)
                dc.add(Restrictions.in("me", me));

            if (op != null)
                dc.add(Restrictions.in("op", op));

            if ("asc".equals(order))
                dc.addOrder(Order.asc(by));
            if ("desc".equals(order))
                dc.addOrder(Order.desc(by));

            this.followDao.begin();

            List<com.github.cuter44.muuga.buddy.model.Follow> f = this.followDao.search(dc, start, size);

            this.followDao.commit();

            Json.writeFollow(f, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.followDao.close();
        }

        return;
    }
}
