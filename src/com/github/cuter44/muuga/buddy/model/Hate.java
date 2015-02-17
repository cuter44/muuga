package com.github.cuter44.muuga.buddy.model;

import java.io.Serializable;
import java.util.Date;

public class Hate
    implements Serializable
{
  // SERIAL
    public static final long serialVersionUID = 1L;

  // FIELDS
    /** id
     */
    protected Long id;

    /** 己方uid
     */
    protected Long me;

    /** 对方uid
     */
    protected Long op;

    /** 时间戳
     */
    protected Date tm;

  // ACCESSOR
    public Long getId()
    {
        return(this.id);
    }

    public void setId(Long newId)
    {
        this.id = newId;
        return;
    }

    public Long getMe()
    {
        return(this.me);
    }

    public void setMe(Long newMe)
    {
        this.me = newMe;
        return;
    }

    public Long getOp()
    {
        return(this.op);
    }

    public void setOp(Long newOp)
    {
        this.op = newOp;
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
    public Hate()
    {
        return;
    }

    public Hate(Long newMe, Long newOp)
    {
        this();

        this.me = newMe;
        this.op = newOp;
        this.tm = new Date(/*now*/);

        return;
    }
  // MISC

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

        Hate b =(Hate)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}
