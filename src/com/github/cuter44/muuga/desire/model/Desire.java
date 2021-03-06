package com.github.cuter44.muuga.desire.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.Profile;

public class Desire
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // FIELD
    /** id
     */
    protected Long id;

    /** ISBN-13
     */
    protected String isbn;
    /** 发起人
     */
    protected Profile originator;
    /** 请求数量
     */
    protected Integer qty;
    /** 费用, 卖出的要价/买入的出价, 按单价计算
     */
    protected Double expense;

    /** PostScript, 附言
     */
    protected String ps;
    /** 地理位置标记
     */
    protected String pos;

    /** 创建时间
     */
    protected Date tm;

    /** 辨别符
     */
    protected String clazz;

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

    public Double getExpense()
    {
        return(this.expense);
    }

    public void setExpense(Double newExpense)
    {
        this.expense = newExpense;
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

    public String getClazz()
    {
        return(this.clazz);
    }

    public void setClazz(String newClazz)
    {
        this.clazz = newClazz;
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

