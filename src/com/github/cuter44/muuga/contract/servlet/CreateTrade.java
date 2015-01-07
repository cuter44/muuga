package com.github.cuter44.muuga.contract.servlet;

import java.io.*;
import java.security.PrivateKey;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.servlet.*;
import static com.github.cuter44.nyafx.servlet.Params.notNull;
import static com.github.cuter44.nyafx.servlet.Params.needLong;
import static com.github.cuter44.nyafx.servlet.Params.needByteArray;

//import com.github.cuter44.muuga.util.conf.*;
import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.desire.core.*;

/** 应征别人的心愿, 发起交易
 * <pre style="font-size:12px">

   <strong>请求</strong>
   POST /contract/trade/create.api

   <strong>参数</strong>
   uid      :long, 自己的 uid, 作为交易的参与方
   desire   :long, 应答的 desire id,
   book     :long, 在应答一个想买心愿时必需, 要出售的书的id
   <i>鉴权</i>
   uid  :long   , 必需, uid
   s    :hex    , 必需, session key

   <strong>响应</strong>
   application/json, class=contract.model.TradeContract
   attributes refer to {@link Json#jsonizeUserPrivate(ContractBase) Json}

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>暂无
 * </pre>
 *
 */
@WebServlet("/contract/trade/create.api")
public class CreateTrade extends HttpServlet
{
    private static final String UID     = "uid";
    private static final String DESIRE  = "desire";
    private static final String BOOK    = "book";

    protected TradeContractDao tradeDao         = TradeContractDao.getInstance();
    protected SellerInitedTradeDao sTradeDao    = SellerInitedTradeDao.getInstance();
    protected BuyerInitedTradeDao bTradeDao     = BuyerInitedTradeDao.getInstance();
    protected TradeDesireDao desireDao          = TradeDesireDao.getInstance();
    protected BuyDesireDao bDesireDao           = BuyDesireDao.getInstance();
    protected SellDesireDao sDesireDao          = SellDesireDao.getInstance();

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");

        try
        {
            Long    uid     = needLong(req, UID);
            Long    desire  = needLong(req, DESIRE);

            this.tradeDao.begin();

            entFound(this.desireDao.get(desire));

            TradeContract contract = null;

            if (this.bDesireDao.get(desire) != null)
            {
                Long book   = needLong(req, BOOK);
                contract    = this.sTradeDao.create(uid, desire, book);
            }

            if (this.sDesireDao.get(desire) != null)
            {
                contract    = this.bTradeDao.create(uid, desire);
            }

            this.tradeDao.commit();

            Json.writeContractBase(contract, resp);
        }
        catch (Exception ex)
        {
            req.setAttribute(Constants.KEY_EXCEPTION, ex);
            req.getRequestDispatcher(Constants.URI_ERROR_HANDLER).forward(req, resp);
        }
        finally
        {
            this.tradeDao.close();
        }

        return;
    }
}
