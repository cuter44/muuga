package com.github.cuter44.muuga.desire.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

public class BorrowDesireDao extends DaoBase<BorrowDesire>
{
    ProfileDao profileDao;

  // CONSTRUCT
    public BorrowDesireDao()
    {
        super();

        this.profileDao = ProfileDao.getInstance();
    }

  // SINGLETON
    private static class Singleton
    {
        public static BorrowDesireDao instance = new BorrowDesireDao();
    }

    public static BorrowDesireDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(BorrowDesire.class);
    }

    public BorrowDesire get(Long originatorId, String isbn)
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
    public BorrowDesire create(Long originatorId, String isbn, Double expense, Integer qty, String ps, String pos)
    {
        BorrowDesire buyDesire = this.get(originatorId, isbn);
        if (buyDesire != null)
            return(buyDesire);

        Profile profile = (Profile)entFound(this.profileDao.get(originatorId));

        buyDesire = new BorrowDesire();

        buyDesire.setOriginator(profile);
        buyDesire.setIsbn(isbn);
        buyDesire.setExpense(expense);
        buyDesire.setQty(qty);
        buyDesire.setPs(ps);
        buyDesire.setPos(pos);
        buyDesire.setTm(new Date(System.currentTimeMillis()));

        this.save(buyDesire);

        return(buyDesire);
    }
}
