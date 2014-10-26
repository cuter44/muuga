package com.github.cuter44.muuga.user.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import com.alibaba.fastjson.*;
import static com.github.cuter44.nyafx.crypto.CryptoBase.byteToHex;

import com.github.cuter44.muuga.user.dao.*;

class J
{
    private static String ID = "id";

    private static String UID = "uid";
    private static String UNAME = "uname";
    private static String MAIL = "mail";
    private static String S = "s";
    private static String STATUS = "status";
    private static String REG_DATE = "regDate";

    protected static JSONObject jsonizePublic(User u)
    {
        JSONObject j = new JSONObject();

        j.put(ID, u.getId());
        j.put(UID, u.getId());
        j.put(UNAME, u.getUname());
        j.put(MAIL, u.getMail());
        j.put(STATUS, u.getStatus());
        j.put(REG_DATE, u.getRegDate());

        return(j);
    }

    protected static JSONObject jsonizePrivate(User u)
    {
        JSONObject j = jsonizePublic(u);

        if (u.getSkey()!=null)
            j.put(S, byteToHex(u.getSkey()));

        return(j);
    }

    protected static JSONArray jsonizeUserPublic(List<User> l)
    {
        JSONArray a = new JSONArray();

        for (User u:l)
            a.add(jsonizePublic(u));

        return(a);
    }

    public static void writeUserPrivate(User u, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizePrivate(u).toJSONString()
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
            jsonizePublic(u).toJSONString()
        );

        return;
    }

    public static void writeUserPublic(List<User> l, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeUserPublic(l).toJSONString()
        );

        return;
    }

    private static final String DNAME       = "dname";
    private static final String TNAME       = "tname";
    private static final String MOTTO       = "motto";
    private static final String AVATAR      = "avatar";
    private static final String POS         = "pos";

    public static JSONObject jsonize(Profile p)
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

    public static JSONArray jsonizeProfile(List<Profile> l)
    {
        JSONArray j = new JSONArray();

        for (Profile p:l)
            j.add(jsonize(p));

        return(j);
    }

    public static void write(Profile p, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonize(p).toJSONString()
        );

        return;
    }

    public static void writeProfile(List<Profile> l, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeProfile(l).toJSONString()
        );

        return;
    }
}
