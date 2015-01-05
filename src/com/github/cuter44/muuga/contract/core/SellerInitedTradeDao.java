package com.github.cuter44.muuga.contract.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.shelf.model.*;

public class SellerInitedTradeDao extends DaoBase<SellerInitedTrade>
{
  // CONSTRUCT
    public SellerInitedTradeDao()
    {
        super();
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

    public SellerInitedTrade getActive(Long desireId, Long buyerId)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(this.classOfT())
            .createAlias("consume", "consume")
            .add(Restrictions.eq("consume.id", buyerId))
            .add(Restrictions.eq("id", desireId))
            .add(Restrictions.ge("status", SellerInitedTrade.STATUS_INIT))
            .add(Restrictions.lt("status", SellerInitedTrade.STATUS_FINISH));

        SellerInitedTrade trade = this.get(dc);

        return(trade);
    }

  // EXTENDED
    public SellerInitedTrade create(BuyDesire desire, Profile seller, Book book)
    {
        SellerInitedTrade trade = this.getActive(desire.getId(), seller.getId());
        if (trade != null)
            return(trade);

        // else
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
