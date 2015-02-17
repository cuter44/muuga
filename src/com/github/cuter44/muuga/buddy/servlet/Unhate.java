package com.github.cuter44.muuga.buddy.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.buddy.model.*;
import com.github.cuter44.muuga.buddy.core.*;

/** 取消拉黑
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /buddy/unhate.api

   <strong>参数</strong>
   uid  :long   , 必需, 自己的uid
   op   :long   , 必需, 对方的uid

   <strong>响应</strong>
   204 No Content

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/buddy/unhate.api")
public class Unhate extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String OP      = "op";

    protected HateDao   hateDao     = HateDao.getInstance();
    protected BuddyMgr  buddyMgr    = BuddyMgr.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            Long    op      = needLong(req, OP);

            this.hateDao.begin();

            this.buddyMgr.unhate(uid, op);

            this.hateDao.commit();

            resp.setStatus(204);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.hateDao.close();
        }

        return;
    }
}
