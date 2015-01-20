package com.github.cuter44.muuga.contract.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;

public class LoanContractDao extends DaoBase<LoanContract>
{
  // CONSTRUCT
    public LoanContractDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static LoanContractDao instance = new LoanContractDao();
    }

    public static LoanContractDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(LoanContract.class);
    }

  // EXTENDED
}
