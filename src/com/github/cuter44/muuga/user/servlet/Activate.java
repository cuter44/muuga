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
import com.alibaba.fastjson.*;

//import com.github.cuter44.muuga.util.conf.*;
import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.dao.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 激活帐号
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/activate.api

   <strong>参数</strong>
   uid:long, uid
   code:hex, 激活码
   pass:hex, RSA 加密的 UTF-8 编码的用户登录密码.

   <strong>响应</strong>
   application/json, class=authorize.dao.User(private)
   @see J#writeUserPrivate

   <strong>例外</strong>
    通用, @see com.github.cuter44.muuga.sys.servlet.ExceptionHandler

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/activate.api")
public class Activate extends HttpServlet
{
    private static final String UID = "uid";
    private static final String CODE = "code";
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
            Long uid        = needLong(req, UID);
            byte[] code     = needByteArray(req, CODE);
            byte[] pass     = needByteArray(req, PASS);
            PrivateKey key  = (PrivateKey)notNull(this.keyCache.get(uid));

            pass = this.rsa.decrypt(pass, key);

            this.userDao.begin();

            this.authorizer.activate(uid, code, pass);

            this.userDao.commit();

            req.getRequestDispatcher("login.api").forward(req, resp);
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
