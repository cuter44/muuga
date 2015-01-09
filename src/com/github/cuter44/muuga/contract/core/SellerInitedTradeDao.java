package com.github.cuter44.muuga.contract.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.desire.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.shelf.model.*;
import com.github.cuter44.muuga.shelf.core.*;
import com.github.cuter44.muuga.user.exception.*;

public class SellerInitedTradeDao extends DaoBase<SellerInitedTrade>
{
  // CONSTRUCT
    protected BuyDesireDao bDesireDao;
    protected ProfileDao profileDao;
    protected BookDao bookDao;

    public SellerInitedTradeDao()
    {
        super();

        this.bDesireDao = BuyDesireDao.getInstance();
        this.profileDao = ProfileDao.getInstance();
        this.bookDao    = BookDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static SellerInitedTradeDao instance = new SellerInitedTradeDao();
    }

    public static SellerInitedTradeDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(SellerInitedTrade.class);
    }

    public SellerInitedTrade getActive(Long desireId, Long sellerId)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(this.classOfT())
            .createAlias("supply", "supply")
            .add(Restrictions.eq("supply.id", sellerId))
            .add(Restrictions.eq("id", desireId))
            .add(Restrictions.ge("status", SellerInitedTrade.STATUS_INIT))
            .add(Restrictions.lt("status", SellerInitedTrade.STATUS_FINISH));

        SellerInitedTrade trade = this.get(dc);

        return(trade);
    }

  // EXTENDED
    public SellerInitedTrade create(Long desireId, Long sellerId, Long bookId)
    {
        SellerInitedTrade trade = this.getActive(desireId, sellerId);
        if (trade != null)
            return(trade);

        // else

        BuyDesire desire    = this.bDesireDao.get(desireId);
        Profile seller      = this.profileDao.get(sellerId);
        Book book           = this.bookDao.get(bookId);
        if ((book!=null) && (!book.ownedBy(sellerId)))
            throw(
                new UnauthorizedException(
                    String.format("Not book owner: book=%d, uid=%d", bookId, sellerId)
            ));

        trade = new SellerInitedTrade();

        trade.setSupply(seller);
        trade.setConsume(desire.getOriginator());
        trade.setIsbn(desire.getIsbn());
        trade.setExpense(desire.getExpense());
        trade.setBook(book);

        trade.setStatus(SellerInitedTrade.STATUS_INIT);

        Date d = new Date(System.currentTimeMillis());
        trade.setTmCreate(d);
        trade.setTmStatus(d);

        this.save(trade);

        return(trade);
    }
}
