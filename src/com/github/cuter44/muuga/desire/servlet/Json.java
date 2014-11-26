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

    protected static JSONObject jsonizeDesire(Desire d)
    {
        if (d instanceof BuyDesire)
            return(jsonizeBuyDesire((BuyDesire)d));

        if (d instanceof SellDesire)
            return(jsonizeSellDesire((SellDesire)d));

        //if (d instanceof BorrowDesire)
            //return(jsonizeBuyDesire((BuyDesire)d));

        //if (d instanceof LendDesire)
            //return(jsonizeBuyDesire((LendDesire)d));

        return(new JSONObject());
    }

    protected static JSONObject jsonizeDesire(Desire d, JSONObject j)
    {
        j.put(ID            , d.getId());
        j.put(ISBN          , d.getIsbn());
        j.put(QTY           , d.getQty());
        j.put(PS            , d.getPs());
        j.put(POS           , d.getPos());

        if (d.getOriginator()!=null)
            j.put(ORIGINATOR, d.getOriginator().getId());

        return(j);
    }

    protected static JSONObject jsonizeTradeDesire(TradeDesire d, JSONObject j)
    {
        j.put(PRICE, d.getPrice());

        j = jsonizeDesire(d, j);

        return(j);
    }

    public static JSONObject jsonizeBuyDesire(BuyDesire d)
    {
        JSONObject j = new JSONObject();

        j = jsonizeTradeDesire(d, j);

        return(j);
    }

    public static JSONObject jsonizeSellDesire(SellDesire d)
    {
        JSONObject j = new JSONObject();

        j = jsonizeTradeDesire(d, j);

        return(j);
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
