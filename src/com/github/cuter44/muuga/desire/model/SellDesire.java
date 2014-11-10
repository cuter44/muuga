package com.github.cuter44.muuga.desire.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.User;

public class SellDesire extends TradeDesire
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // FIELD

  // GETTER/SETTER

  // CONSTRUCT
    public SellDesire()
    {
        super();
        return;
    }

    public SellDesire(String isbn, User originator)
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

        SellDesire b =(SellDesire)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}

