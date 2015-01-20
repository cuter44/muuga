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

public class BorrowerInitedLoanDao extends DaoBase<BorrowerInitedLoan>
{
  // CONSTRUCT
    protected ProfileDao profileDao;
    protected SellDesireDao sDesireDao;

    public BorrowerInitedLoanDao()
    {
        super();

        this.profileDao = ProfileDao.getInstance();
        this.sDesireDao = SellDesireDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static BorrowerInitedLoanDao instance = new BorrowerInitedLoanDao();
    }

    public static BorrowerInitedLoanDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(BorrowerInitedLoan.class);
    }

    public BorrowerInitedLoan getActive(Long desireId, Long buyerId)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(this.classOfT())
            .createAlias("consume", "consume")
            .add(Restrictions.eq("consume.id", buyerId))
            .add(Restrictions.eq("id", desireId))
            .add(Restrictions.ge("status", BorrowerInitedLoan.STATUS_INIT))
            .add(Restrictions.lt("status", BorrowerInitedLoan.STATUS_FINISH));

        BorrowerInitedLoan trade = this.get(dc);

        return(trade);
    }

  // EXTENDED
    public BorrowerInitedLoan create(Long desireId, Long buyerId)
    {
        // return existence if not closed.
        BorrowerInitedLoan trade = this.getActive(desireId, buyerId);
        if (trade != null)
            return(trade);

        Desire desire   = (Desire)entFound(this.sDesireDao.get(desireId));
        Profile buyer   = (Profile)entFound(this.profileDao.get(buyerId));
        // checkpoint

        // else
        trade = new BorrowerInitedLoan();

        trade.setSupply(desire.getOriginator());
        trade.setConsume(buyer);
        trade.setIsbn(desire.getIsbn());
        trade.setExpense(desire.getExpense());

        trade.setStatus(BorrowerInitedLoan.STATUS_INIT);

        Date d = new Date(System.currentTimeMillis());
        trade.setTmCreate(d);
        trade.setTmStatus(d);

        this.save(trade);

        return(trade);
    }
}
