package com.github.cuter44.muuga.shelf.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 删除藏书
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /book/remove.api

   <strong>参数</strong>
   <del>id   :long   , 删除书的id;</del>
   isbn     :string , isbn
   uid  :long   , uid;
   <i>鉴权</i>
   s    :hex    , session key;

   <strong>响应</strong>
   HTTP 204:NO CONTENT

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/book/remove.api")
public class BookRemove extends HttpServlet
{
    private static final String UID     = "uid";
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

            this.bookDao.remove(uid, isbn);

            this.bookDao.commit();

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.bookDao.close();
        }


        return;
    }
}
