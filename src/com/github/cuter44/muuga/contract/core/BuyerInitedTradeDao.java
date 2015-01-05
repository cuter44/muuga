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
        public static BuyerInitedTradeDao instance = new BuyerInitedTradeDao();
    }

    public static BuyerInitedTradeDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(BuyerInitedTrade.class);
    }

    public BuyerInitedTrade getActive(Long desireId, Long buyerId)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(this.classOfT())
            .createAlias("consume", "consume")
            .add(Restrictions.eq("consume.id", buyerId))
            .add(Restrictions.eq("id", desireId))
            .add(Restrictions.ge("status", BuyerInitedTrade.STATUS_INIT))
            .add(Restrictions.lt("status", BuyerInitedTrade.STATUS_FINISH));

        BuyerInitedTrade trade = this.get(dc);

        return(trade);
    }

  // EXTENDED
    public BuyerInitedTrade create(SellDesire desire, Profile buyer)
    {
        // return existence if not closed.
        BuyerInitedTrade trade = this.getActive(desire.getId(), buyer.getId());
        if (trade != null)
            return(trade);

        // else
        trade = new BuyerInitedTrade();

        trade.setSupply(desire.getOriginator());
        trade.setConsume(buyer);
        trade.setIsbn(desire.getIsbn());
        trade.setExpense(desire.getExpense());

        trade.setStatus(BuyerInitedTrade.STATUS_INIT);

        Date d = new Date(System.currentTimeMillis());
        trade.setTmCreate(d);
        trade.setTmStatus(d);

        this.save(trade);

        return(trade);
    }
}
