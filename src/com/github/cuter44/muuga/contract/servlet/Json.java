package com.github.cuter44.muuga.contract.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.contract.model.*;

class Json
{
    private static final String ID          = "id";
    private static final String SUPPLY      = "supply";
    private static final String CONSUME     = "consume";
    private static final String BOOK        = "consume";
    private static final String ISBN        = "isbn";
    private static final String EXPENSE     = "expense";
    private static final String TM_CREATE   = "tmCreate";
    private static final String TM_STATUS   = "tmStatus";
    private static final String STATUS      = "isbn";
    private static final String CLAZZ       = "clazz";

    protected static JSONObject jsonizeContractBase(ContractBase contract)
    {
        if (contract instanceof BuyerInitedTrade)
            return(jsonizeBuyerInitedTrade((BuyerInitedTrade)contract));

        if (contract instanceof SellerInitedTrade)
            return(jsonizeSellerInitedTrade((SellerInitedTrade)contract));

        return(new JSONObject());
    }

    public static JSONArray jsonizeContractBase(Collection<ContractBase> collection)
    {
        JSONArray j = new JSONArray();

        for (ContractBase contract:collection)
            j.add(jsonizeContractBase(contract));

        return(j);
    }

    protected static JSONObject jsonizeContractBase(ContractBase contract, JSONObject j)
    {
        j.put(ID            , contract.getId());
        j.put(ISBN          , contract.getIsbn());
        j.put(EXPENSE       , contract.getExpense());
        j.put(TM_CREATE     , contract.getTmCreate());
        j.put(TM_STATUS     , contract.getTmStatus());
        j.put(STATUS        , contract.getStatus());
        j.put(CLAZZ         , contract.getClazz());

        if (contract.getSupply() != null)
            j.put(SUPPLY    , contract.getSupply().getId());

        if (contract.getConsume() != null)
            j.put(CONSUME   , contract.getConsume().getId());

        if (contract.getBook() != null)
            j.put(BOOK      , contract.getBook().getId());

        return(j);
    }

    protected static JSONObject jsonizeTradeContract(TradeContract contract, JSONObject j)
    {
        j = jsonizeContractBase(contract, j);

        return(j);
    }

    public static JSONObject jsonizeBuyerInitedTrade(BuyerInitedTrade contract)
    {
        JSONObject j = new JSONObject();

        j = jsonizeTradeContract(contract, j);

        return(j);
    }

    public static JSONObject jsonizeSellerInitedTrade(SellerInitedTrade contract)
    {
        JSONObject j = new JSONObject();

        j = jsonizeTradeContract(contract, j);

        return(j);
    }

    public static void writeContractBase(ContractBase contract, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeContractBase(contract)
        );

        return;
    }

    public static void writeContractBase(Collection<ContractBase> collection, ServletResponse resp)
        throws IOException
    {
        resp.setContentType("application/json; charset=utf-8");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();

        out.println(
            jsonizeContractBase(collection)
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
