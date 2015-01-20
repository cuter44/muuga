package com.github.cuter44.muuga.desire.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.*;

public class LoanDesire extends Desire
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // FIELD

  // GETTER/SETTER

  // CONSTRUCT
    public LoanDesire()
    {
        super();
        return;
    }

    public LoanDesire(String isbn, Profile orginator)
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

        LoanDesire b =(LoanDesire)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}

