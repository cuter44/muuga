package com.github.cuter44.muuga.contract.core;

import com.github.cuter44.nyafx.dao.EntityNotFoundException;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;

public class TradeController
{

  // CONSTRUCT
    protected TradeContractDao tradeDao;
    protected SellerInitedTradeDao sTradeDao;
    protected BuyerInitedTradeDao bTradeDao;
    protected TradeDesireDao desireDao;
    protected BuyDesireDao bDesireDao;
    protected SellDesireDao sDesireDao;

    public TradeController()
    {
        this.tradeDao   = TradeContractDao.getInstance();
        this.sTradeDao  = SellerInitedTradeDao.getInstance();
        this.bTradeDao  = BuyerInitedTradeDao.getInstance();
        this.desireDao  = TradeDesireDao.getInstance();
        this.bDesireDao = BuyDesireDao.getInstance();
        this.sDesireDao = SellDesireDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static TradeController instance = new TradeController();
    }

    public static TradeController getInstance()
    {
        return(
            Singleton.instance
        );
    }

  // CREATE
    public TradeContract create(Long uid, Long desireId, Long bookId)
    {
        TradeDesire desire  = (TradeDesire)entFound(this.desireDao.get(desireId));

        TradeContract trade = null;

        //if (this.bDesireDao.get(desire) != null)
        if (desire instanceof BuyDesire)
            trade    = this.sTradeDao.create(uid, desireId, bookId);

        //if (this.sDesireDao.get(desire) != null)
        if (desire instanceof SellDesire)
            trade    = this.bTradeDao.create(uid, desireId);

        return(trade);
    }

    public SellerInitedTrade sellerIntiTrade(Long uid, Long desireId, Long bookId)
    {
        SellerInitedTrade trade = this.sTradeDao.create(uid, desireId, bookId);

        // TODO notify

        return(trade);
    }

    public BuyerInitedTrade create(Long uid, Long desireId)
    {
        BuyerInitedTrade trade = this.bTradeDao.create(uid, desireId);

        // TODO notify

        return(trade);
    }
}
