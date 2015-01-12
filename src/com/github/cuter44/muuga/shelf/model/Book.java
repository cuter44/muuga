package com.github.cuter44.muuga.shelf.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.Profile;

/** 表示书的实体
 * 书被以单本的方式加入到收藏中, i.e. 同一个账户可以加入相同isbn的书
 */
public class Book
    implements Serializable, Cloneable
{
    public static final long serialVersionUID = 1L;

  // FIELD
    protected Long id;

    protected String isbn;
    protected Profile owner;

    protected Date regDate;
    protected Byte status;

    /** 1, 正常
     */
    public static final Byte STATUS_NORMAL = 1;
    /** -1, 已删除
     */
    public static final Byte STATUS_REMOVED = -1;

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

    public Profile getOwner()
    {
        return(this.owner);
    }

    public void setOwner(Profile aOwner)
    {
        this.owner = aOwner;
        return;
    }

    public Date getRegDate()
    {
        return(this.regDate);
    }

    public void setRegDate(Date newRegDate)
    {
        this.regDate = newRegDate;

        return;
    }

    public Byte getStatus()
    {
        return(this.status);
    }

    public void setStatus(Byte newStatus)
    {
        this.status = newStatus;
        return;
    }

  // CONSTRUCT
    public Book()
    {
        return;
    }

    public Book(String aIsbn, Profile aOwner)
    {
        this();

        this.isbn = aIsbn;
        this.owner = aOwner;

        return;
    }

  // CLONE

    /** clone Book without id nor owner
     */
    @Override
    public Object clone()
    {
        Book b = new Book();

        b.setIsbn(this.isbn);

        return(b);
    }

  // EXTENDED
    public boolean ownBy(Long userId)
    {
        try
        {
            return(
                userId.equals(this.owner.getId())
            );
        }
        catch (NullPointerException ex)
        {
        }

        return(false);
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

        Book b =(Book)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}

