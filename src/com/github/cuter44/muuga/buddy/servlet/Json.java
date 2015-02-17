package com.github.cuter44.muuga.buddy.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.buddy.model.*;

class Json
{
    private static final String ID  = "id";
    private static final String ME  = "me";
    private static final String OP  = "op";

  // FOLLOW
    /**
     * <pre style="font-size:12px;">
     * 序列化 Follow 对象, 字段如下:
     * <ul>
     * <li>id   :long   , id
     * <li>me   :long   , 关注者ID
     * <li>op   :long   , 被关注ID
     * </ul>
     * </pre>
     */
    protected static JSONObject jsonizeFollow(com.github.cuter44.muuga.buddy.model.Follow f)
    {
        JSONObject j = new JSONObject();

        j.put(ID    , f.getId());
        j.put(ME    , f.getMe());
        j.put(OP    , f.getOp());

        return(j);
    }

    protected static JSONArray jsonizeFollow(Collection<com.github.cuter44.muuga.buddy.model.Follow> coll)
    {
        JSONArray a = new JSONArray();

        for (com.github.cuter44.muuga.buddy.model.Follow f:coll)
            a.add(jsonizeFollow(f));

        return(a);
    }

    public static void writeFollow(com.github.cuter44.muuga.buddy.model.Follow f, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeFollow(f).toJSONString()
        );

        return;
    }

    public static void writeFollow(Collection<com.github.cuter44.muuga.buddy.model.Follow> coll, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeFollow(coll).toJSONString()
        );

        return;
    }

  // HATE
    /**
     * <pre style="font-size:12px;">
     * 序列化 Hate 对象, 字段如下:
     * <ul>
     * <li>id   :long   , id
     * <li>me   :long   , 关注者ID
     * <li>op   :long   , 被关注ID
     * </ul>
     * </pre>
     */
    protected static JSONObject jsonizeHate(com.github.cuter44.muuga.buddy.model.Hate h)
    {
        JSONObject j = new JSONObject();

        j.put(ID    , h.getId());
        j.put(ME    , h.getMe());
        j.put(OP    , h.getOp());

        return(j);
    }

    protected static JSONArray jsonizeHate(Collection<com.github.cuter44.muuga.buddy.model.Hate> coll)
    {
        JSONArray a = new JSONArray();

        for (com.github.cuter44.muuga.buddy.model.Hate h:coll)
            a.add(jsonizeHate(h));

        return(a);
    }

    public static void writeHate(com.github.cuter44.muuga.buddy.model.Hate h, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeHate(h).toJSONString()
        );

        return;
    }

    public static void writeHate(Collection<com.github.cuter44.muuga.buddy.model.Hate> coll, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeHate(coll).toJSONString()
        );

        return;
    }

  // STAT
    private static final String FOLLOW      = "follow";
    private static final String FOLLOWED    = "followed";
    private static final String HATE        = "hate";
    private static final String HATED       = "hated";

    /**
     * <pre style="font-size:12px;">
     * 序列化 Stat 对象, 字段如下:
     * <ul>
     * <li>id       :long   , user id
     * <li>follow   :long   , 关注数
     * <li>followed :long   , 被关注数
     * <li>hate     :long   , 拉黑数
     * <li>hated    :long   , 被黑数
     * </ul>
     * </pre>
     */
    protected static JSONObject jsonizeStat(Stat s)
    {
        JSONObject j = new JSONObject();

        j.put(ID        , s.getId());
        j.put(FOLLOW    , s.getFollow());
        j.put(FOLLOWED  , s.getFollowed());
        j.put(HATE      , s.getHate());
        j.put(HATED     , s.getHated());

        return(j);
    }

    protected static JSONArray jsonizeStat(Collection<Stat> coll)
    {
        JSONArray a = new JSONArray();

        for (Stat s:coll)
            a.add(jsonizeStat(s));

        return(a);
    }

    public static void writeStat(Stat s, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeStat(s).toJSONString()
        );

        return;
    }

    public static void writeStat(Collection<Stat> coll, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeStat(coll).toJSONString()
        );

        return;
    }

}
