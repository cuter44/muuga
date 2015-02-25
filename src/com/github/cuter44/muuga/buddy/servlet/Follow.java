package com.github.cuter44.muuga.buddy.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.buddy.model.*;
import com.github.cuter44.muuga.buddy.core.*;

/** 关注
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /buddy/follow.api

   <strong>参数</strong>
   uid      :long           , 必需, 自己的uid
   op       :long           , 必需, 对方的uid

   <strong>响应</strong>
   application/json class={@link Json#jsonizeFollow(com.github.cuter44.muuga.buddy.model.Follow) buddy.model.Follow}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/buddy/follow.api")
public class Follow extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String OP      = "op";

    protected FollowDao followDao = FollowDao.getInstance();
    protected BuddyMgr  buddyMgr  = BuddyMgr.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            Long    op      = needLong(req, OP);

            this.followDao.begin();

            com.github.cuter44.muuga.buddy.model.Follow f = this.buddyMgr.follow(uid, op);

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
