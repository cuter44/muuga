package com.github.cuter44.muuga.desire.core;

import java.util.Date;

import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import org.hibernate.criterion.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

public class DesireFactory
{
  // CONSTRUCT
    protected DesireDao desireDao;
    protected ProfileDao profileDao;

    public DesireFactory()
    {
        this.desireDao = DesireDao.getInstance();
        this.profileDao = ProfileDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static final DesireFactory INSTANCE = new DesireFactory();
    }

    public static DesireFactory getInstance()
    {
        return(Singleton.INSTANCE);
    }

  // DESIRE
    public BuyDesire createBuyDesire(Long originatorId, String isbn, Double price, Integer qty, String ps, String pos)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(BuyDesire.class)
            .createAlias("originator", "originator")
            .add(Restrictions.eq("originator.id", originatorId))
            .add(Restrictions.eq("isbn", isbn));
        BuyDesire d = (BuyDesire)this.desireDao.get(dc);
        if (d != null)
            return(d);

        Profile p = (Profile)entFound(this.profileDao.get(originatorId));

        d = new BuyDesire();

        d.setOriginator(p);
        d.setIsbn(isbn);
        d.setPrice(price);
        d.setQty(qty);
        d.setPs(ps);
        d.setPos(pos);
        d.setTm(new Date(System.currentTimeMillis()));

        this.desireDao.save(d);

        return(d);
    }

    public SellDesire createSellDesire(Long originatorId, String isbn, Double price, Integer qty, String ps, String pos)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(SellDesire.class)
            .createAlias("originator", "originator")
            .add(Restrictions.eq("originator.id", originatorId))
            .add(Restrictions.eq("isbn", isbn));
        SellDesire d = (SellDesire)this.desireDao.get(dc);
        if (d != null)
            return(d);

        Profile p = (Profile)entFound(this.profileDao.get(originatorId));

        d = new SellDesire();

        d.setOriginator(p);
        d.setIsbn(isbn);
        d.setPrice(price);
        d.setQty(qty);
        d.setPs(ps);
        d.setPos(pos);
        d.setTm(new Date(System.currentTimeMillis()));

        this.desireDao.save(d);

        return(d);
    }

    public void clearDesire(Long originatorId, String isbn)
    {
        this.desireDao.createQuery("DELETE FROM Desire d_0 WHERE d_0.originator.id=:orgnatorId AND d_0.isbn=:isbn")
            .setLong("originatorId", originatorId)
            .setString("isbn", isbn)
            .executeUpdate();

        return;
    }
}
