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
        public static final SellerInitedTradeDao INSTANCE = new SellerInitedTradeDao();
    }

    public static SellerInitedTradeDao getInstance()
    {
        return(Singleton.INSTANCE);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(SellerInitedTrade.class);
    }

    public SellerInitedTrade getActive(Long desireId, Long buyerId)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(SellerInitedTrade.class)
            .createAlias("consume", "consume")
            .add(Restrictions.eq("consume.id", buyerId))
            .add(Restrictions.eq("id", desireId))
            .add(Restrictions.ge("status", SellerInitedTrade.STATUS_INIT))
            .add(Restrictions.lt("status", SellerInitedTrade.STATUS_FINISH));

        SellerInitedTrade t = this.get(dc);

        return(t);
    }

  // EXTENDED
    public SellerInitedTrade create(BuyDesire desire, Book book, Profile seller)
    {
        SellerInitedTrade t = this.getActive(desire.getId(), seller.getId());
        if (t != null)
            return(t);

        // else
        t = new SellerInitedTrade();

        t.setSupply(seller);
        t.setConsume(desire.getOriginator());
        t.setIsbn(desire.getIsbn());
        t.setExpense(desire.getExpense());

        t.setStatus(SellerInitedTrade.STATUS_INIT);

        Date d = new Date(System.currentTimeMillis());
        t.setTmCreate(d);
        t.setTmStatus(d);

        this.save(t);

        return(t);
    }
}
