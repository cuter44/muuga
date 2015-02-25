package com.github.cuter44.muuga.desire.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.List;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.getLongList;
import static com.github.cuter44.nyafx.servlet.Params.getStringList;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.desire.core.*;

/** 移除心愿
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /desire/remove.api

   <strong>参数</strong>
   <i>以下参数的任意一个</i>
   id   :long[]     , 逗号分隔, 移除的心愿id
   isbn :string[]   , 逗号分隔, 移除所有关于该isbn的心愿

   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   HTTP 204:NO CONTENT

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 *
 */
@WebServlet("/desire/remove.api")
public class DesireRemove extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String ID      = "id";
    private static final String ISBN    = "isbn";

    protected DesireDao desireDao = DesireDao.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long            uid     = needLong(req, UID);
            List<String>    isbns   = getStringList(req, ISBN);
            List<Long>      ids     = getLongList(req, ID);

            this.desireDao.begin();

            if (ids!=null && ids.size()!=0)
            {
                this.desireDao.createQuery("DELETE FROM Desire d_0 WHERE d_0.originator.id = :uid AND d_0.id IN :ids")
                    .setLong("uid", uid)
                    .setParameterList("ids", ids)
                    .executeUpdate();
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

                this.desireDao.commit();

                return;
            }

            if (isbns!=null && isbns.size()!=0)
            {
                this.desireDao.createQuery("DELETE FROM Desire d_0 WHERE d_0.originator.id = :uid AND d_0.isbn IN :isbns")
                    .setLong("uid", uid)
                    .setParameterList("isbns", isbns)
                    .executeUpdate();
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

                this.desireDao.commit();

                return;
            }

            throw(new MissingParameterException());
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.desireDao.close();
        }

        return;
    }
}
