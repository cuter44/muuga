package com.github.cuter44.muuga.contract.model;

import java.io.Serializable;

public class LoanContract extends ContractBase
    implements Serializable
{
  // SERIAL
    public static final long serialVersionUID = 1L;

  // FIELDS
    /** 12, 已交付
     */
    public static final Byte STATUS_DELIEVERED = 12;

    /** 13, 借阅方收讫
     */
    public static final Byte STATUS_DELIEVER_ACK = 13;

    /** 16, 借阅方提请归还
     */
    public static final Byte STATUS_RETURN = 16;

    /** 17, 借出方收讫
     */
    public static final Byte STATUS_RETURN_ACK = 17;

  // ACCESSOR

  // CONSTRUCT
    public LoanContract()
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
