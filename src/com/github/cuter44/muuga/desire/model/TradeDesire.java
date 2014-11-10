package com.github.cuter44.muuga.desire.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.User;

public class TradeDesire extends Desire
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // FIELD
    protected Double price;

  // GETTER/SETTER
    public Double getPrice()
    {
        return(this.price);
    }

    public void setPrice(Double newPrice)
    {
        this.price = newPrice;
        return;
    }

  // CONSTRUCT
    public TradeDesire()
    {
        super();
        return;
    }

    public TradeDesire(String isbn, User orginator)
    {
        this();

        this.setIsbn(isbn);
        this.setOriginator(orginator);
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

        TradeDesire b =(TradeDesire)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}

