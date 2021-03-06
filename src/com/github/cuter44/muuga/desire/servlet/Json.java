package com.github.cuter44.muuga.desire.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.desire.model.*;

class Json
{
    private static final String ID          = "id";
    private static final String ORIGINATOR  = "originator";
    private static final String ISBN        = "isbn";
    private static final String EXPENSE     = "expense";
    private static final String QTY         = "qty";
    private static final String PS          = "ps";
    private static final String POS         = "pos";
    private static final String TM          = "tm";
    private static final String CLAZZ       = "clazz";

    protected static JSONObject jsonizeDesire(Desire d)
    {
        if (d instanceof BuyDesire)
            return(jsonizeBuyDesire((BuyDesire)d));

        if (d instanceof SellDesire)
            return(jsonizeSellDesire((SellDesire)d));

        if (d instanceof BorrowDesire)
            return(jsonizeBorrowDesire((BorrowDesire)d));

        if (d instanceof LendDesire)
            return(jsonizeLendDesire((LendDesire)d));

        return(new JSONObject());
    }

    public static JSONArray jsonizeDesire(Collection<Desire> coll)
    {
        JSONArray j = new JSONArray();

        for (Desire d:coll)
            j.add(jsonizeDesire(d));

        return(j);
    }
    /**
     * <pre style="font-size:12px;">
     * 序列化 ContractBase 对象, 字段如下:
     * id           :long                       , 需求id
     * isbn         :string(13)                 , isbn
     * originator   :long                       , 需求发起者的id
     * qty          :int                        , 期望数量
     * ps           :string(255)                , PostScript, 附言
     * pos          :geohash(24)                , 地理位置标记
     * expense      :double(.2)                 , 卖出的最低期望价格, 买入的最高期望价格
     * tm           :unix-time-ms               , Last-Modified
     * clazz        :class-name=BuyDesire...    , 交易类型(买卖, 借阅...), 参见以下链接
     * </pre>
     * @see com.github.cuter44.muuga.desire.model.Desire
     * @see com.github.cuter44.muuga.desire.model.BorrowDesire
     * @see com.github.cuter44.muuga.desire.model.LendDesire
     * @see com.github.cuter44.muuga.desire.model.BuyDesire
     * @see com.github.cuter44.muuga.desire.model.SellDesire
     * .last-update 2015/2/13
     */
    protected static JSONObject jsonizeDesire(Desire d, JSONObject j)
    {
        j.put(ID            , d.getId());
        j.put(ISBN          , d.getIsbn());
        j.put(QTY           , d.getQty());
        j.put(PS            , d.getPs());
        j.put(POS           , d.getPos());
        j.put(EXPENSE       , d.getExpense());
        j.put(TM            , d.getTm());
        j.put(CLAZZ         , d.getClazz());

        if (d.getOriginator()!=null)
            j.put(ORIGINATOR, d.getOriginator().getId());

        return(j);
    }

    protected static JSONObject jsonizeTradeDesire(TradeDesire d, JSONObject j)
    {
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

    protected static JSONObject jsonizeLoanDesire(LoanDesire d, JSONObject j)
    {
        j = jsonizeDesire(d, j);

        return(j);
    }

    public static JSONObject jsonizeBorrowDesire(BorrowDesire d)
    {
        JSONObject j = new JSONObject();

        j = jsonizeLoanDesire(d, j);

        return(j);
    }

    public static JSONObject jsonizeLendDesire(LendDesire d)
    {
        JSONObject j = new JSONObject();

        j = jsonizeLoanDesire(d, j);

        return(j);
    }

    public static void writeDesire(Desire d, HttpServletResponse resp)
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

    public static void writeDesire(Collection<Desire> coll, HttpServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeDesire(coll)
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
