package com.github.cuter44.muuga.contract.servlet;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.contract.model.*;

public class Json
{
    private static final String ID          = "id";
    private static final String SUPPLY      = "supply";
    private static final String CONSUME     = "consume";
    private static final String BOOK        = "book";
    private static final String ISBN        = "isbn";
    private static final String EXPENSE     = "expense";
    private static final String TM_CREATE   = "tmCreate";
    private static final String TM_STATUS   = "tmStatus";
    private static final String STATUS      = "status";
    private static final String CLAZZ       = "clazz";

    public static JSONObject jsonizeContractBase(ContractBase contract)
    {
        if (contract instanceof BuyerInitedTrade)
            return(jsonizeBuyerInitedTrade((BuyerInitedTrade)contract));

        if (contract instanceof SellerInitedTrade)
            return(jsonizeSellerInitedTrade((SellerInitedTrade)contract));

        if (contract instanceof BorrowerInitedLoan)
            return(jsonizeBorrowerInitedLoan((BorrowerInitedLoan)contract));

        if (contract instanceof LenderInitedLoan)
            return(jsonizeLenderInitedLoan((LenderInitedLoan)contract));

        return(new JSONObject());
    }

    public static JSONArray jsonizeContractBase(Collection<ContractBase> collection)
    {
        JSONArray j = new JSONArray();

        for (ContractBase contract:collection)
            j.add(jsonizeContractBase(contract));

        return(j);
    }

    /**
     * <pre style="font-size:12px;">
     * 序列化 ContractBase 对象, 字段如下:
     * id       :long                               , 交易id
     * isbn     :string(13)                         , isbn
     * expense  :double(.2)                         , 价格/押金
     * tmCreate :unix-time-ms                       , 创建时间
     * tmStatus :unix-time-ms                       , 最后一次状态变更时间
     * status   :byte                               , 交易状态, 参见交易类型的常量字段
     * clazz    :class-name=BuyerInitedTrade...     , 交易类型(买卖, 借阅...), 参见交易类型
     * supply   :long                               , 借出/卖出方的uid
     * consume  :long                               , 借入/买入方的uid
     * book     :long                               , 可选, 关联到该交易的书籍, 用于交易完成之后卖方清理库存/防止二次借出
     * </pre>
     * @see com.github.cuter44.muuga.contract.model.ContractBase
     * @see com.github.cuter44.muuga.contract.model.BuyerInitedTrade
     * @see com.github.cuter44.muuga.contract.model.SellerInitedTrade
     * @see com.github.cuter44.muuga.contract.model.LenderInitedLoan
     * @see com.github.cuter44.muuga.contract.model.BorrowerInitedLoan
     * .last-update 2015/2/13
     */
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

    protected static JSONObject jsonizeLoanContract(LoanContract contract, JSONObject j)
    {
        j = jsonizeContractBase(contract, j);

        return(j);
    }

    public static JSONObject jsonizeBorrowerInitedLoan(BorrowerInitedLoan contract)
    {
        JSONObject j = new JSONObject();

        j = jsonizeLoanContract(contract, j);

        return(j);
    }

    public static JSONObject jsonizeLenderInitedLoan(LenderInitedLoan contract)
    {
        JSONObject j = new JSONObject();

        j = jsonizeLoanContract(contract, j);

        return(j);
    }

    public static void writeContractBase(ContractBase contract, HttpServletResponse resp)
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

    public static void writeContractBase(Collection<ContractBase> collection, HttpServletResponse resp)
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
