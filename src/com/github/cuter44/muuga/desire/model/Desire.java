package com.github.cuter44.muuga.desire.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.Profile;

public class Desire
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // FIELD
    protected Long id;

    protected String isbn;
    protected Profile originator;
    protected Integer qty;

    protected String ps;
    protected String pos;

    protected Date tm;

  // GETTER/SETTER
    public Long getId()
    {
        return(this.id);
    }

    public void setId(Long aId)
    {
        this.id = aId;
        return;
    }

    public String getIsbn()
    {
        return(this.isbn);
    }

    public void setIsbn(String aIsbn)
    {
        this.isbn = aIsbn;
        return;
    }

    public Profile getOriginator()
    {
        return(this.originator);
    }

    public void setOriginator(Profile aOriginator)
    {
        this.originator = aOriginator;
        return;
    }

    public Integer getQty()
    {
        return(this.qty);
    }

    public void setQty(Integer newQty)
    {
        this.qty = newQty;
        return;
    }

    public String getPs()
    {
        return(this.ps);
    }

    public void setPs(String newPs)
    {
        this.ps = newPs;
        return;
    }

    public String getPos()
    {
        return(this.pos);
    }

    public void setPos(String newPos)
    {
        this.pos = newPos;
        return;
    }

    public Date getTm()
    {
        return(this.tm);
    }

    public void setTm(Date newTm)
    {
        this.tm = newTm;
        return;
    }

  // CONSTRUCT
    public Desire()
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

        Desire b =(Desire)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}

