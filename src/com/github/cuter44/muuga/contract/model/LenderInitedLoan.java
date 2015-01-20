package com.github.cuter44.muuga.contract.model;

import java.io.Serializable;

public class LenderInitedLoan extends LoanContract
    implements Serializable
{
  // SERIAL
    public static final long serialVersionUID = 1L;

  // FIELDS

  // ACCESSOR

  // CONSTRUCT
    public LenderInitedLoan()
    {
        return;
    }

  // HASH
    @Override
    public int hashCode()
    {
        int hash = 17;

        if (this.id != null)
            hash = hash * 31 + this.id.hashCode();

        return(hash);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
            return(true);

        if (o==null || !this.getClass().equals(o.getClass()))
            return(false);

        SellerInitedTrade b =(SellerInitedTrade)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}
