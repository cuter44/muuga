package com.github.cuter44.muuga.desire.core;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.desire.model.*;

public class TradeDesireDao extends DaoBase<TradeDesire>
{
  // CONSTRUCT
    public TradeDesireDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static TradeDesireDao instance = new TradeDesireDao();
    }

    public static TradeDesireDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(TradeDesire.class);
    }

  // EXTENDED
}
