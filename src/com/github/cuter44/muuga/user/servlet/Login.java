package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.security.PrivateKey;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
import com.github.cuter44.nyafx.crypto.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needByteArray;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 登录
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/login.api

   <strong>参数</strong>
   uid:long, uid
   pass:hex, RSA 加密的 UTF-8 编码的用户登录密码.

   <strong>响应</strong>
   application/json class=user.model.User(private)
   attributes refer to {@link Json#jsonizeUserPrivate(User) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/login.api")
public class Login extends HttpServlet
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
            Long        uid     = needLong(req, UID);
            byte[]      pass    = needByteArray(req, PASS);
            PrivateKey  key     = (PrivateKey)notNull(this.keyCache.get(uid));

            pass = this.rsa.decrypt(pass, key);

            this.userDao.begin();

            this.authorizer.login(uid, pass);

            Json.writeUserPrivate(this.userDao.get(uid), resp);

            this.userDao.commit();
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
