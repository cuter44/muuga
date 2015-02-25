package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.Arrays;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.alibaba.fastjson.*;
import com.github.cuter44.nyafx.dao.*;
import com.github.cuter44.nyafx.crypto.*;
import static com.github.cuter44.nyafx.crypto.CryptoBase.byteToHex;
import static com.github.cuter44.nyafx.servlet.Params.needLong;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

/** 获取 RSA 公钥
 * 生成的密钥是一次一密, 一人一密的, 生成的密钥有生存期(由服务器端配置决定)
 * 可以通过指定别人的 uid 扰乱他人的密钥, 这可能是一个漏洞.
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /security/get-rsa-key.api

   <strong>参数</strong>
   uid:long, uid, 可以指定一个负的随机数来为未注册用户保存一个key

   <strong>响应</strong>
   application/json:
   m:hex, modulus
   e:hex, public exponent

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/security/get-rsa-key.api")
public class GetRsaKey extends HttpServlet
{
    private static final String UID = "uid";
    private static final String M = "m";
    private static final String E = "e";

    protected RSAKeyCache keyCache = RSAKeyCache.getInstance();

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
        resp.setContentType("application/json; charset=utf-8");
        PrintWriter out = resp.getWriter();

        try
        {
            Long uid = needLong(req, UID);

            RSAPublicKey pk = (RSAPublicKey)this.keyCache.genKey(uid);

            JSONObject json = new JSONObject();

            byte[] m = pk.getModulus().toByteArray();
            if (m[0] == 0)
                m = Arrays.copyOfRange(m, 1, m.length);
            byte[] e = pk.getPublicExponent().toByteArray();
            if (e[0] == 0)
                e = Arrays.copyOfRange(e, 1, e.length);

            json.put(M, byteToHex(m));
            json.put(E, byteToHex(e));

            out.println(json.toJSONString());
        }
        catch (Exception ex)
        {
            req.setAttribute(KEY_EXCEPTION, ex);
            req.getRequestDispatcher(URI_ERROR_HANDLER).forward(req, resp);
        }

        return;
    }
}
