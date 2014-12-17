package com.github.cuter44.muuga.contract.core;

import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.shelf.model.*;

public class TradeController
{
    protected TradeContractDao tradeDao;
    protected SellerInitedTradeDao sTradeDao;
    protected BuyerInitedTradeDao bTradeDao;

  // CONSTRUCT
    public TradeController()
    {
        this.tradeDao = TradeContractDao.getInstance();
        this.sTradeDao = SellerInitedTradeDao.getInstance();
        this.bTradeDao = BuyerInitedTradeDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static final TradeController INSTANCE = new TradeController();
    }

    public static TradeController getInstance()
    {
        return(
            Singleton.INSTANCE
        );
    }

  // CREATE
    public BuyerInitedTrade buyerInitTrade(SellDesire desire, Profile buyer)
    {
        BuyerInitedTrade trade = this.bTradeDao.create(desire, buyer);

        // TODO fire event

        return(trade);
    }

    public SellerInitedTrade sellerInitTrade(BuyDesire desire, Profile seller, Book book)
    {
        SellerInitedTrade trade = this.sTradeDao.create(desire, seller, book);

        // TODO fire event

        return(trade);
    }

    // not allowed
    //public void changeExpense(Long tradeId, Double expense)
    //{
        //TradeContract trade = (TradeContract)entFound(this.tradeId.get(tradeId));

        //trade.setExpense(expense);

        //this.tradeDao.update(trade);

        //return;
    //}

  // CANCAL/REJECT
    public void buyerQuitTrade(Long tradeId)
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Illegeal status to quit, resync required."));

        trade.setStatus(ContractBase.STATUS_SUPPLY_QUIT);

        this.tradeDao.update(trade);

        // TODO fire event

        return;
    }

    public void sellerQuitTrade(Long tradeId)
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Illegeal status to quit, resync required."));

        trade.setStatus(ContractBase.STATUS_CONSUME_QUIT);

        this.tradeDao.update(trade);

        // TODO fire event

        return;
    }

  // ACK
    public void buyerAckTrade(Long tradeId)
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Illegeal status to quit, resync required."));

        trade.setStatus(ContractBase.STATUS_ACKED);

        this.tradeDao.update(trade);

        // TODO fire event

        return;
    }

    public void sellerAckTrade(Long tradeId, Book book)
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Illegeal status to quit, resync required."));

        trade.setStatus(ContractBase.STATUS_ACKED);
        trade.setBook(book);

        this.tradeDao.update(trade);

        // TODO fire event

        return;
    }

  // PAYED
    public void buyerPayTrade(Long tradeId)
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (TradeContract.STATUS_INIT >= trade.getStatus())
            throw(new IllegalStateException("Illegeal status to quit, resync required."));

        trade.setStatus(TradeContract.STATUS_PAYED);

        this.tradeDao.update(trade);

        // TODO fire event

        return;
    }

  // DELIEVERED
    public void sellerDelieverTrade(Long tradeId)
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Illegeal status to quit, resync required."));

        trade.setStatus(TradeContract.STATUS_DELIEVERED);

        this.tradeDao.update(trade);

        // TODO fire event

        return;
    }
}
