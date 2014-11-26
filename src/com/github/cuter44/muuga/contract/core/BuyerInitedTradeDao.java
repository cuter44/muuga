package com.github.cuter44.muuga.contract.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.contract.model.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;

public class BuyerInitedTradeDao extends DaoBase<BuyerInitedTrade>
{
  // CONSTRUCT
    public BuyerInitedTradeDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static final BuyerInitedTradeDao INSTANCE = new BuyerInitedTradeDao();
    }

    public static BuyerInitedTradeDao getInstance()
    {
        return(Singleton.INSTANCE);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(BuyerInitedTrade.class);
    }

    public BuyerInitedTrade getActive(Long desireId, Long buyerId)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(BuyerInitedTrade.class)
            .createAlias("consume", "consume")
            .add(Restrictions.eq("consume.id", buyerId))
            .add(Restrictions.eq("id", desireId))
            .add(Restrictions.ge("status", BuyerInitedTrade.STATUS_INIT))
            .add(Restrictions.lt("status", BuyerInitedTrade.STATUS_FINISH));

        BuyerInitedTrade t = this.get(dc);

        return(t);
    }

  // EXTENDED
    public BuyerInitedTrade create(SellDesire desire, Profile buyer)
    {
        // return existence if not closed.
        BuyerInitedTrade t = this.getActive(desire.getId(), buyer.getId());
        if (t != null)
            return(t);

        // else
        t = new BuyerInitedTrade();

        t.setSupply(desire.getOriginator());
        t.setConsume(buyer);
        t.setIsbn(desire.getIsbn());
        t.setExpense(desire.getExpense());

        t.setStatus(BuyerInitedTrade.STATUS_INIT);

        Date d = new Date(System.currentTimeMillis());
        t.setTmCreate(d);
        t.setTmStatus(d);

        this.save(t);

        return(t);
    }
}
