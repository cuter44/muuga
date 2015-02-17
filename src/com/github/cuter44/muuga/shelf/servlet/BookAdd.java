package com.github.cuter44.muuga.shelf.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needString;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

/** 增加藏书
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /book/add.api

   <strong>参数</strong>
   isbn :string , 必需, isbn
   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   application/json class={@link Json#jsonizeBook(Book) shelf.model.Book}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/book/add.api")
public class BookAdd extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String OWNER   = "owner";
    private static final String ISBN    = "isbn";

    protected BookDao bookDao = BookDao.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            String  isbn    = needString(req, ISBN);

            this.bookDao.begin();

            Book    book    = this.bookDao.create(uid, isbn);

            this.bookDao.commit();

            Json.writeBook(book, resp);
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
