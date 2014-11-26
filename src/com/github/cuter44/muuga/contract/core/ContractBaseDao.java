package com.github.cuter44.muuga.contract.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;

public class ContractBaseDao extends DaoBase<ContractBase>
{
  // CONSTRUCT
    public ContractBaseDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static final ContractBaseDao INSTANCE = new ContractBaseDao();
    }

    public static ContractBaseDao getInstance()
    {
        return(Singleton.INSTANCE);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(ContractBase.class);
    }

  // EXTENDED
}
