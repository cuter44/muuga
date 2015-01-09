package com.github.cuter44.muuga.shelf.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.user.model.*;

class Json
{
    private static final String ID          = "id";
    private static final String ISBN        = "isbn";
    private static final String OWNER       = "owner";
    private static final String REG_DATE    = "regDate";
    private static final String STATUS      = "status";


    public static JSONObject jsonizeBook(Book book)
    {
        JSONObject j = new JSONObject();

        j.put(ID        , book.getId());
        j.put(ISBN      , book.getIsbn());
        j.put(REG_DATE  , book.getRegDate());

        if (book.getOwner()!=null)
            j.put(OWNER , book.getOwner().getId());

        return(j);
    }

    public static JSONArray jsonizeBook(Collection<Book> coll)
    {
        JSONArray j = new JSONArray();

        for (Book book:coll)
            j.add(jsonizeBook(book));

        return(j);
    }

    public static void writeBook(Book book, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeBook(book).toJSONString()
        );

        return;
    }

    public static void writeBook(Collection<Book> book, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeBook(book).toJSONString()
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
