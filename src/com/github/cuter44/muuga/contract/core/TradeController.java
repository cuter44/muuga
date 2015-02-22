package com.github.cuter44.muuga.contract.core;

import com.github.cuter44.nyafx.dao.EntityNotFoundException;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.event.*;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;
import com.github.cuter44.muuga.user.exception.*;
import com.github.cuter44.muuga.Constants;

public class TradeController
{
    protected static final String EVENTTYPE_TRADE_BUYERINIT    = Constants.EVENTTYPE_TRADE_BUYERINIT;
    protected static final String EVENTTYPE_TRADE_SELLERINIT   = Constants.EVENTTYPE_TRADE_SELLERINIT;
    protected static final String EVENTTYPE_TRADE_BUYERACCEPT  = Constants.EVENTTYPE_TRADE_BUYERACCEPT;
    protected static final String EVENTTYPE_TRADE_SELLERACCEPT = Constants.EVENTTYPE_TRADE_SELLERACCEPT;
    protected static final String EVENTTYPE_TRADE_CONSUMEQUIT  = Constants.EVENTTYPE_TRADE_CONSUMEQUIT;
    protected static final String EVENTTYPE_TRADE_SUPPLYQUIT   = Constants.EVENTTYPE_TRADE_SUPPLYQUIT;
    protected static final String EVENTTYPE_TRADE_DELIVERED    = Constants.EVENTTYPE_TRADE_DELIVERED;
    protected static final String EVENTTYPE_TRADE_FINISH       = Constants.EVENTTYPE_TRADE_FINISH;

  // CONSTRUCT
    protected TradeContractDao       tradeDao;
    protected SellerInitedTradeDao  sTradeDao;
    protected BuyerInitedTradeDao   bTradeDao;
    protected TradeDesireDao         desireDao;
    protected BuyDesireDao          bDesireDao;
    protected SellDesireDao         sDesireDao;
    protected BookDao               bookDao;
    protected EventHub              eventHub;

    public TradeController()
    {
        this.tradeDao   = TradeContractDao.getInstance();
        this.sTradeDao  = SellerInitedTradeDao.getInstance();
        this.bTradeDao  = BuyerInitedTradeDao.getInstance();
        this.desireDao  = TradeDesireDao.getInstance();
        this.bDesireDao = BuyDesireDao.getInstance();
        this.sDesireDao = SellDesireDao.getInstance();
        this.bookDao    = BookDao.getInstance();
        this.eventHub   = EventHub.getInstance();

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
    public TradeContract create(Long desireId, Long uid, Long bookId)
    {
        TradeDesire desire  = (TradeDesire)entFound(this.desireDao.get(desireId));

        TradeContract trade = null;

        //if (this.bDesireDao.get(desire) != null)
        if (desire instanceof BuyDesire)
            trade    = this.sellerInitTrade(desireId, uid, bookId);

        //if (this.sDesireDao.get(desire) != null)
        if (desire instanceof SellDesire)
            trade    = this.buyerInitTrade(desireId, uid);

        return(trade);
    }

    protected SellerInitedTrade sellerInitTrade(Long desireId, Long uid, Long bookId)
    {
        SellerInitedTrade trade = this.sTradeDao.create(desireId, uid, bookId);

        this.eventHub.dispatch(EVENTTYPE_TRADE_SELLERINIT, new Event(trade));

        return(trade);
    }

    protected BuyerInitedTrade buyerInitTrade(Long desireId, Long uid)
    {
        BuyerInitedTrade trade = this.bTradeDao.create(desireId, uid);

        this.eventHub.dispatch(EVENTTYPE_TRADE_BUYERINIT, new Event(trade));

        return(trade);
    }

  // ACCEPT
    public TradeContract accept(Long tradeId, Long uid, Long bookId)
        throws IllegalStateException, UnauthorizedException
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Trade not on STATUS_INIT:tradeId="+tradeId));

        if (trade instanceof BuyerInitedTrade)
            this.sellerAccept(tradeId, uid, bookId);

        if (trade instanceof SellerInitedTrade)
            this.buyerAccept(tradeId, uid);

        return(trade);
    }

    protected BuyerInitedTrade sellerAccept(Long tradeId, Long uid, Long bookId)
        throws IllegalStateException, UnauthorizedException
    {
        BuyerInitedTrade trade = (BuyerInitedTrade)entFound(this.bTradeDao.get(tradeId));

        if (!BuyerInitedTrade.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Trade not on STATUS_INIT:tradeId="+tradeId));

        if (!trade.isSuppliedBy(uid))
            throw(new UnauthorizedException("Trade not supplied by current user:tradeId="+tradeId+",uid="+uid));

        Book book = bookId!=null ? this.bookDao.get(bookId) : null;
        if ((book!=null) && !book.isOwnedBy(uid))
            throw(new UnauthorizedException("Book not owned by current user:bookId="+bookId+",uid="+uid));

        trade.setStatus(BuyerInitedTrade.STATUS_ACKED);
        trade.setBook(book);

        this.bTradeDao.update(trade);

        this.eventHub.dispatch(EVENTTYPE_TRADE_SELLERACCEPT, new Event(trade));

        return(trade);
    }

    protected SellerInitedTrade buyerAccept(Long tradeId, Long uid)
        throws IllegalStateException, UnauthorizedException
    {
        SellerInitedTrade trade = (SellerInitedTrade)entFound(this.sTradeDao.get(tradeId));

        if (!SellerInitedTrade.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Trade not on STATUS_INIT:tradeId="+tradeId));

        if (!trade.isConsumedBy(uid))
            throw(new UnauthorizedException("Trade not consumed by current user:tradeId="+tradeId+",uid="+uid));

        trade.setStatus(SellerInitedTrade.STATUS_ACKED);

        this.sTradeDao.update(trade);

        this.eventHub.dispatch(EVENTTYPE_TRADE_BUYERACCEPT, new Event(trade));

        return(trade);
    }

  // QUIT
    public TradeContract quit(Long tradeId, Long uid)
        throws IllegalStateException
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!TradeContract.STATUS_INIT.equals(trade.getStatus()))
            throw(new IllegalStateException("Trade not on STATUS_INIT:tradeId="+tradeId));

        if (trade.isConsumedBy(uid))
        {
            trade.setStatus(TradeContract.STATUS_CONSUME_QUIT);

            this.tradeDao.update(trade);

            this.eventHub.dispatch(EVENTTYPE_TRADE_CONSUMEQUIT, new Event(trade));
        }

        if (trade.isSuppliedBy(uid))
        {
            trade.setStatus(TradeContract.STATUS_SUPPLY_QUIT);

            this.tradeDao.update(trade);

            this.eventHub.dispatch(EVENTTYPE_TRADE_SUPPLYQUIT, new Event(trade));
        }

        return(trade);
    }

  // DELIVERED
    public TradeContract deliver(Long tradeId, Long uid)
        throws UnauthorizedException, IllegalStateException
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!trade.isSuppliedBy(uid))
            throw(new UnauthorizedException("Trade not supplied by current user:tradeId="+tradeId+",uid="+uid));

        Byte status = trade.getStatus();
        if (
            !TradeContract.STATUS_ACKED.equals(status)
            && !TradeContract.STATUS_PAYED.equals(status)
        )
            throw(new IllegalStateException("Trade not on STATUS_ACKED or STATUS_PAYED:tradeId="+tradeId));

        trade.setStatus(TradeContract.STATUS_DELIVERED);

        this.tradeDao.update(trade);

        this.eventHub.dispatch(EVENTTYPE_TRADE_DELIVERED, new Event(trade));

        return(trade);
    }

  // FINISH
    public TradeContract finish(Long tradeId, Long uid)
        throws UnauthorizedException, IllegalStateException
    {
        TradeContract trade = (TradeContract)entFound(this.tradeDao.get(tradeId));

        if (!trade.isConsumedBy(uid))
            throw(new UnauthorizedException("Trade not consumed by current user:tradeId="+tradeId+",uid="+uid));

        Byte status = trade.getStatus();
        if (!TradeContract.STATUS_DELIVERED.equals(trade.getStatus()))
            throw(new IllegalStateException("Trade not on STATUS_DELIVERED:tradeId="+tradeId));

        trade.setStatus(TradeContract.STATUS_FINISH);

        this.tradeDao.update(trade);

        this.eventHub.dispatch(EVENTTYPE_TRADE_FINISH, new Event(trade));

        return(trade);
    }
}
