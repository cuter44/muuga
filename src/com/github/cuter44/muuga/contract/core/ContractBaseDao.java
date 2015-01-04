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
        public static ContractBaseDao instance = new ContractBaseDao();
    }

    public static ContractBaseDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(ContractBase.class);
    }

  // EXTENDED
    public boolean isSupplier(Long contractId, Long profileId)
    {
        TradeContract trade = (TradeContract)this.get(contractId);

        try
        {
            return(
                profileId.equals(
                    trade.getSupply().getId()
            ));
        }
        catch (NullPointerException ex)
        {
            return(false);
        }
    }
}
