package com.github.cuter44.muuga.desire.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.desire.model.*;

class Json
{
    private static final String ID  = "id";
    private static final String ORIGINATOR = "originator";
    private static final String ISBN = "isbn";
    private static final String PRICE = "price";
    private static final String QTY = "qty";
    private static final String PS = "ps";
    private static final String POS = "pos";
    private static final String TM = "tm";

    public static JSONObject jsonizeDesire(Desire d)
    {
        return(null);
    }

    public static JSONObject jsonizeDesire(BuyDesire d)
    {
        return(null);
    }

    public static void writeDesire(Desire d, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeDesire(d)
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
