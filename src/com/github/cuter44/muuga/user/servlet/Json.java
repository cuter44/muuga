package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.alibaba.fastjson.*;
import static com.github.cuter44.nyafx.crypto.CryptoBase.byteToHex;

import com.github.cuter44.muuga.user.model.*;

class Json
{
    private static final String ID          = "id";

    private static final String UID         = "uid";
    private static final String UNAME       = "uname";
    private static final String MAIL        = "mail";
    private static final String SECRET      = "secret";
    private static final String STATUS      = "status";
    private static final String REG_DATE    = "regDate";
    private static final String CLAZZ       = "clazz";

    /**
     * <pre style="font-size:12px;">
     * 序列化 User 对象, 字段如下:
     * <ul>
     * <li>id       :long                           , user id
     * <li>uid      :long                           , user id
     * <li>uname    :string                         , 登录名(username)
     * <li>mail     :string                         , e-mail
     * <li>status   :byte                           , 账户状态, 参见 User
     * <li>regDate  :unix-time-ms                   , 注册时间
     * <li>clazz    :class-name=IndividualUser|...  , 账户类型, 参见 User 的子类
     * </ul>
     * </pre>
     * @see com.github.cuter44.muuga.user.model.User
     * @see com.github.cuter44.muuga.user.model.IndividualUser
     * @see com.github.cuter44.muuga.user.model.EnterpriseUser
     */
    protected static JSONObject jsonizeUser(User u)
    {
        JSONObject j = new JSONObject();

        j.put(ID        , u.getId());
        j.put(UID       , u.getId());
        j.put(UNAME     , u.getUname());
        j.put(MAIL      , u.getMail());
        j.put(STATUS    , u.getStatus());
        j.put(REG_DATE  , u.getRegDate());
        j.put(CLAZZ     , u.getClazz());

        return(j);
    }

    protected static JSONArray jsonizeUser(Collection<User> coll)
    {
        JSONArray a = new JSONArray();

        for (User u:coll)
            a.add(jsonizeUser(u));

        return(a);
    }

    public static void writeUser(User u, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeUser(u).toJSONString()
        );

        return;
    }

    public static void writeUser(Collection<User> coll, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeUser(coll).toJSONString()
        );

        return;
    }

    private static final String DNAME       = "dname";
    private static final String TNAME       = "tname";
    private static final String MOTTO       = "motto";
    private static final String AVATAR      = "avatar";
    private static final String POS         = "pos";
    private static final String BG          = "bg";

    /**
     * <pre style="font-size:12px;">
     * 序列化 User 对象, 字段如下:
     * <ul>
     * <li>id       :long           , user id
     * <li>dname    :string         , 显示名(display-name)
     * <li><del>tname    :string        , </del>(currently not used)
     * <li>motto    :string         , 签名
     * <li>avatar   :url            , 头像URL, 参见 github://cuter44/muuga/wiki/Avatar_Specification
     * <li>pos      :geohash        , 用户位置, 受用户本身的隐私设置可能是不精确或不正确的
     * <li>bg       :"%s[,%0.2d]"   , 用户首页的背景图像, 取 isbn 为 %s 的书的封面, 从高度的 %d (比例, 缺省为0)开始的矩形区域.
     * </ul>
     */
    public static JSONObject jsonizeProfile(Profile p)
    {
        JSONObject j = new JSONObject();

        j.put(ID        , p.getId());
        j.put(DNAME     , p.getDname());
        j.put(TNAME     , p.getTname());
        j.put(MOTTO     , p.getMotto());
        j.put(AVATAR    , p.getAvatar());
        j.put(POS       , p.getPos());
        j.put(BG        , p.getBg());

        for (String key:p.others.stringPropertyNames())
            j.put(key, p.others.getProperty(key));

        return(j);
    }

    public static JSONArray jsonizeProfile(Collection<Profile> coll)
    {
        JSONArray j = new JSONArray();

        for (Profile p:coll)
            j.add(jsonizeProfile(p));

        return(j);
    }

    public static void writeProfile(Profile p, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeProfile(p).toJSONString()
        );

        return;
    }

    public static void writeProfile(Collection<Profile> coll, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeProfile(coll).toJSONString()
        );

        return;
    }

    public static void writeErrorOk(HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("{\"error\":\"ok\"}");

        return;
    }
}
