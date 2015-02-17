package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.security.PrivateKey;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.crypto.*;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needByteArray;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 登出
 * 登出使得 secret 失效, 而使得签名不再有效, 以阻止(包括其他终端)操作.
 * 客户端在持有的密钥失效时, 应使用 login 接口取得现在的密钥. 仅在用户认为其 secret 已经泄漏需要重置时使用该接口.
 * 如果持有密钥已失效, 请使用 logout-via-pass.api
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/logout-via-secret.api

   <strong>参数</strong>
   uid:long, uid

   <strong>响应</strong>
   application/json; class=user.model.User
   attributes refer to {@link Json#jsonizeUser(User) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/logout-via-secret.api")
public class LogoutViaSecret extends HttpServlet
{
    private static final String UID = "uid";

    protected UserDao userDao = UserDao.getInstance();
    protected Authorizer authorizer = Authorizer.getInstance();

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

            this.userDao.begin();

            User u = this.authorizer.logoutViaSecret(uid);

            this.userDao.commit();

            Json.writeUser(u, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.userDao.close();
        }

        return;
    }
}
