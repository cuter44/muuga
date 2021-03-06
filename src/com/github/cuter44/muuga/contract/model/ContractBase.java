package com.github.cuter44.muuga.contract.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.shelf.model.*;

public class ContractBase
    implements Serializable
{
  // SERIAL
    public static final long serialVersionUID = 1L;

  // FIELDS
    /** id
     */
    protected Long id;

    /** 供应方, 借出/卖出
     */
    protected Profile supply;
    /** 消耗方, 借入/买入
     */
    protected Profile consume;
    /** isbn
     */
    protected String isbn;
    /** 关联到该交易的书籍, 用于交易完成之后卖方清理库存/防止二次借出
     */
    protected Book book;

    /** 价格/押金, 按总价计算
     */
    protected Double expense;

    /** 交易创建时间
     */
    protected Date tmCreate;
    /** 交易状态变更时间
     */
    protected Date tmStatus;

    /** 辨别符
     */
    protected String clazz;

    /**
     * 子类具有不同的状态, 可能需要沿继承树翻阅多个文档才能得到全部可能的值
     */
    protected Byte status;
    /** 0, 初始状态
     */
    public static final Byte STATUS_INIT = 0;
    /** -1, 供给方放弃
     */
    public static final Byte STATUS_SUPPLY_QUIT = -1;
    /** -2, 需求方放弃
     */
    public static final Byte STATUS_CONSUME_QUIT = -2;
    /** 1, 对方已接受该交易
     */
    public static final Byte STATUS_ACKED = 1;
    /** 127, 完成
     */
    public static final Byte STATUS_FINISH = 127;

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

    public Profile getSupply()
    {
        return(this.supply);
    }

    public void setSupply(Profile newSupply)
    {
        this.supply = newSupply;
        return;
    }

    public Profile getConsume()
    {
        return(this.consume);
    }

    public void setConsume(Profile newConsume)
    {
        this.consume = newConsume;
        return;
    }

    public String getIsbn()
    {
        return(this.isbn);
    }

    public void setIsbn(String newIsbn)
    {
        this.isbn = newIsbn;
        return;
    }

    public Book getBook()
    {
        return(this.book);
    }

    public void setBook(Book newBook)
    {
        this.book = newBook;
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

    public Date getTmCreate()
    {
        return(this.tmCreate);
    }

    public void setTmCreate(Date newTmCreate)
    {
        this.tmCreate = newTmCreate;
        return;
    }

    public Date getTmStatus()
    {
        return(this.tmStatus);
    }

    public void setTmStatus(Date newTmStatus)
    {
        this.tmStatus = newTmStatus;
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

    public Byte getStatus()
    {
        return(this.status);
    }

    public void setStatus(Byte newStatus)
    {
        this.status = newStatus;
        this.tmStatus = new Date(System.currentTimeMillis());

        return;
    }

  // CONSTRUCT
    public ContractBase()
    {
        return;
    }
  // MISC
    public boolean isConsumedBy(Long uid)
    {
        if (uid==null || this.getConsume()==null)
            return(false);

        // else
        return(uid.equals(this.getConsume().getId()));
    }

    public boolean isSuppliedBy(Long uid)
    {
        if (uid == null || this.getSupply()==null)
            return(false);

        // else
        return(uid.equals(this.getSupply().getId()));
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

        ContractBase b =(ContractBase)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}
