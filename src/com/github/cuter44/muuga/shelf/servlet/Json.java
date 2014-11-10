package com.github.cuter44.muuga.shelf.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.user.model.*;

class Json
{
    private static final String ID = "id";
    private static final String ISBN = "isbn";
    private static final String OWNER = "owner";
    private static final String REG_DATE = "regDate";
    private static final String STATUS = "status";


    public static JSONObject jsonizeBook(Book b)
    {
        JSONObject j = new JSONObject();

        j.put(ID        , b.getId());
        j.put(ISBN      , b.getIsbn());
        j.put(REG_DATE  , b.getRegDate());

        if (b.getOwner()!=null)
            j.put(OWNER , b.getOwner().getId());

        return(j);
    }

    public static JSONArray jsonizeBook(Collection<Book> coll)
    {
        JSONArray j = new JSONArray();

        for (Book b:coll)
            j.add(jsonizeBook(b));

        return(j);
    }

    public static void writeBook(Book b, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeBook(b).toJSONString()
        );

        return;
    }

    public static void writeBook(Collection<Book> b, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeBook(b).toJSONString()
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
