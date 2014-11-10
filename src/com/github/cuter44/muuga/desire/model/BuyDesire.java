package com.github.cuter44.muuga.desire.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.*;

public class BuyDesire extends TradeDesire
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // FIELD

  // GETTER/SETTER

  // CONSTRUCT
    public BuyDesire()
    {
        super();
        return;
    }

    public BuyDesire(String isbn, Profile originator)
    {
        this();

        this.setIsbn(isbn);
        this.setOriginator(originator);
        this.setTm(new Date(System.currentTimeMillis()));

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

        BuyDesire b =(BuyDesire)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}

