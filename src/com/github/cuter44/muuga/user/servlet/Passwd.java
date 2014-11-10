package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.security.PrivateKey;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.crypto.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needByteArray;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 修改密码
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/passwd.api

   <strong>参数</strong>
   uid:long, uid
   pass:hex, RSA 加密的 UTF-8 编码的用户登录密码.
   newpass:hex, 使用相同 key 加密的新密码

   <strong>响应</strong>
   application/json class=user.model.User(private)
   attributes refer to {@link Json#jsonizeUserPrivate(User) Json}
   <i>密码被变更为 newpass</i>
   <i>原session key失效</i>

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}
   <i>只要发生例外密码就不会变更</i>

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/passwd.api")
public class Passwd extends HttpServlet
{
    private static final String UID = "uid";
    private static final String PASS = "pass";
    private static final String NEWPASS = "newpass";

    protected UserDao userDao = UserDao.getInstance();
    protected Authorizer authorizer = Authorizer.getInstance();
    protected RSAKeyCache keyCache = RSAKeyCache.getInstance();
    protected RSACrypto rsa = RSACrypto.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long        uid     = needLong(req, UID);
            PrivateKey  key     = (PrivateKey)notNull(this.keyCache.get(uid));

            byte[]      pass    = needByteArray(req, PASS);
            pass                = this.rsa.decrypt(pass, key);

            byte[]      newpass = needByteArray(req, NEWPASS);
            newpass             = this.rsa.decrypt(newpass, key);

            this.userDao.begin();

            this.authorizer.passwd(uid, pass, newpass);
            this.authorizer.login(uid, newpass);

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
