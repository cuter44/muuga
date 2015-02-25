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

/** 取得好友统计数据
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET /buddy/stat.api

   <strong>参数</strong>
   id      :long           , 必需, 查询的uid

   <strong>响应</strong>
   application/json class={@link Json#jsonizeStat(com.github.cuter44.muuga.buddy.model.Stat) buddy.model.Follow}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/buddy/stat.api")
public class StatGet extends HttpServlet
{
    private static final String ID  = "id";

    protected StatDao   statDao     = StatDao.getInstance();
    protected BuddyMgr  buddyMgr    = BuddyMgr.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    id      = needLong(req, ID);

            this.statDao.begin();

            Stat s = this.statDao.get(id);

            this.statDao.commit();

            Json.writeStat(s, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.statDao.close();
        }

        return;
    }
}
