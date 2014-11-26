package com.github.cuter44.muuga.contract.core;

import java.util.Date;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.contract.model.*;

public class TradeContractDao extends DaoBase<TradeContract>
{
  // CONSTRUCT
    public TradeContractDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static final TradeContractDao INSTANCE = new TradeContractDao();
    }

    public static TradeContractDao getInstance()
    {
        return(Singleton.INSTANCE);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(TradeContract.class);
    }

  // EXTENDED
}
