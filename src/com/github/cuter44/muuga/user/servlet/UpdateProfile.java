package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.getString;
import static com.github.cuter44.nyafx.servlet.Params.needLong;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.dao.*;
import com.github.cuter44.muuga.user.core.*;

/** 变更个人资料
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /profile/update.api

   <strong>参数</strong>
   uid:long, 必需, 变更的资料id
   <1>可变更项目</i>
   dname:string(48), 显示名
   motto:string(255), 签名
   pos:base32(24), 地理标记
   <i>鉴权</i>
   uid:long, 必需, uid
   s:hex, 必需, session key

   <strong>响应</strong>
   application/json class=profile.dao.Profile
   @see J#write

   <strong>例外</strong>
    通用, @see com.github.cuter44.muuga.sys.servlet.ExceptionHandler

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/profile/update.api")
public class UpdateProfile extends HttpServlet
{
    private static final String UID = "uid";
    private static final String DNAME = "dname";
    private static final String MOTTO = "motto";
    private static final String POS = "pos";

    protected ProfileDao profileDao = ProfileDao.getInstance();

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
            Long uid = needLong(req, UID);

            this.profileDao.begin();

            Profile p = (Profile)entFound(this.profileDao.get(uid));

            String dname = getString(req, DNAME);
            if (dname != null)
                p.setDname(dname);

            String motto = getString(req, MOTTO);
            if (motto != null)
                p.setMotto(motto);

            String pos = getString(req, POS);
            if (pos != null)
                p.setPos(pos);

            this.profileDao.update(p);

            this.profileDao.commit();

            J.write(p, resp);

        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.profileDao.close();
        }

        return;
    }
}
