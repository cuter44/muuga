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

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 登出
 * 登出使得 secret 失效, 而使得签名不再有效, 以阻止(包括其他终端)操作.
 * 客户端在持有的密钥失效时, 应使用 login 接口取得现在的密钥. 仅在用户认为其 secret 已经泄漏需要重置时使用该接口.
 * 如果持有密钥仍然有效, 且需要重置 secret, 请使用 logout-via-secret.api
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/logout-via-pass.api

   <strong>参数</strong>
   uid:long, uid
   pass:hex, RSA 加密的 UTF-8 编码的用户登录密码.

   <strong>响应</strong>
   application/json; class=user.model.User
   attributes refer to {@link Json#jsonizeUser(User) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/logout-via-pass.api")
public class LogoutViaPass extends HttpServlet
{
    private static final String UID = "uid";
    private static final String PASS = "pass";

    protected UserDao userDao = UserDao.getInstance();
    protected Authorizer authorizer = Authorizer.getInstance();
    protected RSAKeyCache keyCache = RSAKeyCache.getInstance();
    protected RSACrypto rsa = RSACrypto.getInstance();

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

            byte[] pass = needByteArray(req, PASS);

            // key 检定
            PrivateKey key  = (PrivateKey)notNull(this.keyCache.get(uid));
            pass            = this.rsa.decrypt(pass, key);

            User u = this.authorizer.logoutViaPass(uid, pass);

            this.userDao.commit();

            Json.writeUser(u, resp);

        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.userDao.close();
        }

        return;
    }
}
