package com.github.cuter44.muuga.desire.core;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.desire.model.*;

public class LoanDesireDao extends DaoBase<LoanDesire>
{
  // CONSTRUCT
    public LoanDesireDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static LoanDesireDao instance = new LoanDesireDao();
    }

    public static LoanDesireDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(LoanDesire.class);
    }

  // EXTENDED
}
