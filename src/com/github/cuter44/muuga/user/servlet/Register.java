package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.crypto.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.needString;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

/** 注册
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /user/register.api

   <strong>参数</strong>
   mail:string(60), 邮件地址

   <strong>响应</strong>
   application/json class=user.model.User
   attributes refer to {@link Json#jsonizeUserPublic(User) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/user/register.api")
public class Register extends HttpServlet
{
    private static final String MAIL = "mail";
    private static final String UID = "uid";

    protected UserDao userDao = UserDao.getInstance();
    protected Authorizer authorizer = Authorizer.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            String mail = needString(req, MAIL);

            this.userDao.begin();

            User u = this.authorizer.register(mail);

            this.userDao.commit();

            Json.writeUserPublic(u, resp);
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
