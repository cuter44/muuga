package com.github.cuter44.muuga.desire.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import org.hibernate.criterion.*;
import com.github.cuter44.muuga.desire.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

public class LendDesireDao extends DaoBase<LendDesire>
{
  // CONSTRUCT
    protected ProfileDao profileDao;

    public LendDesireDao()
    {
        super();

        this.profileDao = ProfileDao.getInstance();
    }

  // SINGLETON
    private static class Singleton
    {
        public static LendDesireDao instance = new LendDesireDao();
    }

    public static LendDesireDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(LendDesire.class);
    }

    public LendDesire get(Long originatorId, String isbn)
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
    public LendDesire create(Long originatorId, String isbn, Double expense, Integer qty, String ps, String pos)
    {
        LendDesire lendDeisre = this.get(originatorId, isbn);
        if (lendDeisre != null)
            return(lendDeisre);

        Profile profile = (Profile)entFound(this.profileDao.get(originatorId));

        lendDeisre = new LendDesire();

        lendDeisre.setOriginator(profile);
        lendDeisre.setIsbn(isbn);
        lendDeisre.setExpense(expense);
        lendDeisre.setQty(qty);
        lendDeisre.setPs(ps);
        lendDeisre.setPos(pos);
        lendDeisre.setTm(new Date(System.currentTimeMillis()));

        this.save(lendDeisre);

        return(lendDeisre);
    }
}
