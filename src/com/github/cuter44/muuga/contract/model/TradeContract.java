package com.github.cuter44.muuga.contract.model;

import java.io.Serializable;

public class TradeContract extends ContractBase
    implements Serializable
{
  // SERIAL
    public static final long serialVersionUID = 1L;

  // FIELDS
    /** 8, 已支付
     */
    public static final Byte STATUS_PAYED = 8;
    /** 12, 已交付
     */
    public static final Byte STATUS_DELIEVERED = 12;

  // ACCESSOR

  // CONSTRUCT
    public TradeContract()
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

        TradeContract b =(TradeContract)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}
