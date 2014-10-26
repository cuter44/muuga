package com.github.cuter44.muuga.user.dao;

import java.io.Serializable;
import java.util.Date;

public class User
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // ENUM
    // 帐户状态枚举
    /** 注册
     */
    public static final Byte REGISTERED = 0;
    /** 已验证邮箱
     */
    public static final Byte ACTIVATED = 1;
    /** 主动注销
     */
    public static final Byte CANCELED = -1;
    /** 封号
     */
    public static final Byte BANNED = -2;

  // FIELDS
    protected Long id;

    /** 邮件地址, unique
     */
    protected String mail;
    /** 用户名, unique
     */
    protected String uname;

    /** session
     */
    protected byte[] skey;
    /** 盐
     */
    protected byte[] salt;
    /** 加盐散列后密码
     */
    protected byte[] pass;

    /** 帐户状态
     */
    protected Byte status;
    /** 注册日期
     */
    protected Date regDate;


  // GETTER/SETTER
    public Long getId()
    {
        return(id);
    }
    public void setId(Long aId)
    {
        this.id = aId;
    }

    public String getMail()
    {
        return(this.mail);
    }
    public void setMail(String aMail)
    {
        this.mail = aMail;
    }

    public String getUname()
    {
        return(this.uname);
    }
    public void setUname(String aUname)
    {
        this.uname = aUname;
    }

    public byte[] getSkey()
    {
        return(this.skey);
    }
    public void setSkey(byte[] aSkey)
    {
        this.skey = aSkey;
    }

    public byte[] getSalt()
    {
        return(this.salt);
    }
    public void setSalt(byte[] aSalt)
    {
        this.salt = aSalt;
    }

    public byte[] getPass()
    {
        return(this.pass);
    }
    public void setPass(byte[] aPass)
    {
        this.pass = aPass;
    }

    public Byte getStatus()
    {
        return(this.status);
    }
    public void setStatus(Byte aStatus)
    {
        this.status = aStatus;
    }

    public Date getRegDate()
    {
        return(regDate);
    }
    public void setRegDate(Date aRegDate)
    {
        this.regDate = aRegDate;
    }

  // CONSTRUCT
    public User()
    {
        this.status = REGISTERED;
    }

    public User(String aMail)
    {
        this();
        this.mail = aMail;
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

        if (o!=null && !this.getClass().equals(o.getClass()))
            return(false);

        User u = (User)o;

        return(
            (this.id == u.id) ||
            (this.id != null && this.id.equals(u.id))
        );
    }
}

