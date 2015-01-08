package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;

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
     * export below attributes:
       <ul>
       <li>id       :long               , user id
       <li>uid      :long               , user id
       <li>uname    :string             , username
       <li>mail     :string             , user e-mail
       <li>status   :byte               , account state, refer to {@link com.github.cuter44.muuga.user.model.User User}
       <li>regDate  :unix-time-millis   , register date
       <li>clazz    :string             , class of user (IndividualUser, EnterpriseUser, etc.)
       </ul>
     */
    protected static JSONObject jsonizeUserPublic(User u)
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

    protected static JSONArray jsonizeUserPublic(Collection<User> coll)
    {
        JSONArray a = new JSONArray();

        for (User u:coll)
            a.add(jsonizeUserPublic(u));

        return(a);
    }

    public static void writeUserWithSecret(User u, String encryptedSecret, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        JSONObject j = jsonizeUserPublic(u);
        j.put(SECRET, encryptedSecret);

        out.println(
            j.toJSONString()
        );

        return;
    }

    public static void writeUserPublic(User u, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeUserPublic(u).toJSONString()
        );

        return;
    }

    public static void writeUserPublic(Collection<User> coll, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeUserPublic(coll).toJSONString()
        );

        return;
    }

    private static final String DNAME       = "dname";
    private static final String TNAME       = "tname";
    private static final String MOTTO       = "motto";
    private static final String AVATAR      = "avatar";
    private static final String POS         = "pos";

    /**
     * export below attributes:
       <ul>
       <li>id       :long               , user id
       <li>dname    :string             , user display name
       <li>tname    :string             , (now unused)
       <li>motto    :string             , user motto
       <li>avatar   :url                , avatar pattern
       <li>pos      :geohash            , user most recently geo position
       </ul>
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

    public static void writeProfile(Profile p, ServletResponse resp)
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

    public static void writeProfile(Collection<Profile> coll, ServletResponse resp)
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

    public static void writeErrorOk(ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println("{\"error\":\"ok\"}");

        return;
    }
}
