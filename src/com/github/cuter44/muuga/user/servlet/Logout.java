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
import static com.github.cuter44.nyafx.servlet.Params.getByteArray;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.dao.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 登出
 * 登出会清除所有终端上的操作凭证
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/logout.api

   <strong>参数</strong>
   uid:long, uid
   以下参数的其中一个:
   s:hex, session key
   pass:hex, RSA 加密的 UTF-8 编码的用户登录密码.

   <strong>响应</strong>
    application/json class=authorize.dao.User(private)
    @see J#writeUserPrivate


   <strong>例外</strong>
    通用, @see com.github.cuter44.muuga.sys.servlet.ExceptionHandler

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/logout.api")
public class Logout extends HttpServlet
{
    private static final String UID = "uid";
    private static final String PASS = "pass";
    private static final String S = "s";

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

            // logout by session key
            byte[] skey = getByteArray(req, S);
            if (skey != null)
            {
                this.authorizer.logoutViaSkey(uid, skey);

                this.userDao.commit();

                J.writeUserPrivate(this.userDao.get(uid), resp);

                return;
            }

            // else
            // logout by password
            byte[] pass = getByteArray(req, PASS);
            if (pass != null)
            {
                // key 检定
                PrivateKey key  = (PrivateKey)notNull(this.keyCache.get(uid));
                pass            = this.rsa.decrypt(pass, key);

                this.authorizer.logoutViaPass(uid, pass);

                J.writeUserPrivate(this.userDao.get(uid), resp);

                this.userDao.commit();

                return;
            }
            // else
            // parameter missing
            throw(new MissingParameterException("Pequires skey or pass"));
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
