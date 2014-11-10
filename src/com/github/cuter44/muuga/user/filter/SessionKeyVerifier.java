package com.github.cuter44.muuga.user.filter;

/* filter */
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
//import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needByteArray;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 检查用户 id 和 session key 是否匹配
 * 如果无法匹配, 则拦截请求, 抛出 LoggedOutException
 * <br />
 * <pre style="font-size:12px">
   <strong>配置</strong>
   userIdParamName      用于在请求参数中查找用户ID的键名, 默认 uid
   sessionKeyParamName  用于在请求参数中查找用户Session Key的键名, 默认 s

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}
 * </pre>
 */
public class SessionKeyVerifier
    implements Filter
{
    private static final String USER_ID_PARAM_NAME = "userIdParamName";
    private static final String SESSION_KEY_PARAM_NAME = "sessionKeyParamName";
    private String UID;
    private String S;

    protected Authorizer authorizer;
    protected UserDao userDao;

    @Override
    public void init(FilterConfig conf)
    {
        this.UID = conf.getInitParameter(USER_ID_PARAM_NAME);
        this.S = conf.getInitParameter(SESSION_KEY_PARAM_NAME);

        this.authorizer = Authorizer.getInstance();
        this.userDao = UserDao.getInstance();

        return;
    }

    @Override
    public void destroy()
    {
        return;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException
    {
        this.doFilter((HttpServletRequest)request, (HttpServletResponse)response, chain);
    }

    public void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
        throws IOException, ServletException
    {
        boolean flag = false;

        try
        {
            Long    uid     = needLong(req, UID);
            byte[]  skey    = needByteArray(req, S);

            this.userDao.begin();

            if (!this.authorizer.verifySkey(uid, skey))
                throw(new LoggedOutException(
                    String.format(
                        "Incorrect skey:%s=%d, %s=%s)",
                        UID, uid, S, skey
                    )
                ));

            this.userDao.commit();
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);

            return;
        }
        finally
        {
            this.userDao.close();
        }

        chain.doFilter(req, resp);
    }
}
