package com.github.cuter44.muuga.contract.core;

import com.github.cuter44.nyafx.dao.EntityNotFoundException;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.contract.core.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;
import com.github.cuter44.muuga.user.exception.*;

public class TradeController
{

  // CONSTRUCT
    protected TradeContractDao       tradeDao;
    protected SellerInitedTradeDao  sTradeDao;
    protected BuyerInitedTradeDao   bTradeDao;
    protected TradeDesireDao         desireDao;
    protected BuyDesireDao          bDesireDao;
    protected SellDesireDao         sDesireDao;
    protected BookDao               bookDao;

    public TradeController()
    {
        this.tradeDao   = TradeContractDao.getInstance();
        this.sTradeDao  = SellerInitedTradeDao.getInstance();
        this.bTradeDao  = BuyerInitedTradeDao.getInstance();
        this.desireDao  = TradeDesireDao.getInstance();
        this.bDesireDao = BuyDesireDao.getInstance();
        this.sDesireDao = SellDesireDao.getInstance();
        this.bookDao    = BookDao.getInstance();

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

        // TODO notify

        return(trade);
    }

    protected BuyerInitedTrade buyerInitTrade(Long desireId, Long uid)
    {
        BuyerInitedTrade trade = this.bTradeDao.create(desireId, uid);

        // TODO notify

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

        // TODO notify

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

        // TODO notify

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
            // TODO notify
        }

        if (trade.isSuppliedBy(uid))
        {
            trade.setStatus(TradeContract.STATUS_SUPPLY_QUIT);
            // TODO notify
        }

        this.tradeDao.update(trade);

        return(trade);
    }

  // DELIVERED
    public TradeContract deliever(Long tradeId, Long uid)
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

        trade.setStatus(TradeContract.STATUS_DELIEVERED);

        this.tradeDao.update(trade);

        // TODO notify

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
        if (!TradeContract.STATUS_DELIEVERED.equals(trade.getStatus()))
            throw(new IllegalStateException("Trade not on STATUS_DELIVERED:tradeId="+tradeId));

        trade.setStatus(TradeContract.STATUS_FINISH);

        this.tradeDao.update(trade);

        // TODO notify

        return(trade);
    }
}
