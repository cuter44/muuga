package com.github.cuter44.muuga.desire.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import org.hibernate.criterion.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

public class SellDesireDao extends DaoBase<SellDesire>
{
  // CONSTRUCT
    protected ProfileDao profileDao;

    public SellDesireDao()
    {
        super();

        this.profileDao = ProfileDao.getInstance();
    }

  // SINGLETON
    private static class Singleton
    {
        public static SellDesireDao instance = new SellDesireDao();
    }

    public static SellDesireDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(SellDesire.class);
    }

    public SellDesire get(Long originatorId, String isbn)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(this.classOfT())
            .createAlias("originator", "originator")
            .add(Restrictions.eq("originator.id", originatorId))
            .add(Restrictions.eq("isbn", isbn));

        return(
            this.get(dc)
        );
    }

  // EXTENDED
    public SellDesire create(Long originatorId, String isbn, Double expense, Integer qty, String ps, String pos)
    {
        SellDesire sellDesire = this.get(originatorId, isbn);
        if (sellDesire != null)
            return(sellDesire);

        Profile profile = (Profile)entFound(this.profileDao.get(originatorId));

        sellDesire = new SellDesire();

        sellDesire.setOriginator(profile);
        sellDesire.setIsbn(isbn);
        sellDesire.setExpense(expense);
        sellDesire.setQty(qty);
        sellDesire.setPs(ps);
        sellDesire.setPos(pos);
        sellDesire.setTm(new Date(System.currentTimeMillis()));

        this.save(sellDesire);

        return(sellDesire);
    }
}
