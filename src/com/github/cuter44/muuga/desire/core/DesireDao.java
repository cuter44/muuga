package com.github.cuter44.muuga.desire.core;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.desire.model.*;

public class DesireDao extends DaoBase<Desire>
{
  // CONSTRUCT
    public DesireDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static DesireDao instance = new DesireDao();
    }

    public static DesireDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(Desire.class);
    }

  // EXTENDED
}
