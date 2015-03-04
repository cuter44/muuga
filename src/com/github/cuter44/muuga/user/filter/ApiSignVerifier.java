package com.github.cuter44.muuga.user.filter;

/* filter */
import java.util.Map;
import java.util.Arrays;
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.servlet.*;
import com.github.cuter44.nyafx.crypto.*;
import static com.github.cuter44.nyafx.servlet.Params.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.exception.*;

/** 检查 API 签名
 * 如果无法匹配, 则拦截请求, 抛出 LoggedOutException
 * <br />
 * <pre style="font-size:12px">
   <strong>配置</strong>
   uidKey   用于在请求参数中查找用户ID的键名, 默认 uid
   sKey     用于在请求参数中查找用户Session Key的键名, 默认 s

   <strong>例外</strong>
   LoggedOutException       : 当没有既存的 secret 时;
   UnauthorizedException    : 当 secret 无法匹配时;
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}
 * </pre>
 */
public class ApiSignVerifier
    implements Filter
{
    private static final String CONF_UID_KEY = "nameUid";
    private static final String CONF_S_KEY   = "nameS";
    private String UID;
    private String S;

    protected Authorizer        authorizer;
    protected UserDao           userDao;
    protected CryptoBase        crypto;
    protected UserSecretCache   secretCache;

    @Override
    public void init(FilterConfig conf)
    {
        this.UID = conf.getInitParameter(CONF_UID_KEY);
        this.S = conf.getInitParameter(CONF_S_KEY);

        this.crypto         = CryptoBase.getInstance();
        this.authorizer     = Authorizer.getInstance();
        this.userDao        = UserDao.getInstance();
        this.secretCache    = UserSecretCache.getInstance();

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
        try
        {
            this.userDao.begin();

            Long    uid     = needLong(req, UID);
            byte[]  s       = needByteArray(req, S);
            String  secret  = this.secretCache.get(uid);

            this.userDao.commit();

            if (secret == null)
                throw(new LoggedOutException("Need log in:uid="+uid));

            Map<String, String[]> paramMap = req.getParameterMap();
            String[] keys = new String[paramMap.size()];
            keys = paramMap.keySet().toArray(keys);
            Arrays.sort(keys);

            StringBuilder sb = new StringBuilder(32768);

            for (String key:keys)
                if (!S.equals(key))
                    sb.append(key).append(paramMap.get(key)[0]);

            sb.append(secret);

            byte[] hashed = this.crypto.MD5Digest(
                sb.toString().getBytes("utf-8")
            );

            if (!Arrays.equals(s, hashed))
                throw(new UnauthorizedException("Sign mismatch."));

            chain.doFilter(req, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);

            return;
        }
        finally
        {
            this.userDao.close();
        }
    }
}
