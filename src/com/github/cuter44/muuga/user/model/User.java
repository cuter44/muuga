package com.github.cuter44.muuga.user.model;

import java.io.Serializable;
import java.util.Date;

public class User
    implements Serializable
{
    public static final long serialVersionUID = 1L;

  // ENUM
    // 帐户状态枚举
    /** 0, 注册
     */
    public static final Byte STATUS_REGISTERED = 0;
    /** 1, 已验证邮箱
     */
    public static final Byte STATUS_ACTIVATED = 1;
    /** -1, 主动注销
     */
    public static final Byte STATUS_CANCELED = -1;
    /** -2, 封号
     */
    public static final Byte STATUS_BANNED = -2;

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
    protected byte[] secret;
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

    protected String clazz;

  // ACCESSOR
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

    public byte[] getSecret()
    {
        return(this.secret);
    }
    public void setSecret(byte[] aSecret)
    {
        this.secret = aSecret;
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
    public User()
    {
        return;
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

        if (o==null || !this.getClass().equals(o.getClass()))
            return(false);

        User u = (User)o;

        return(
            (this.id == u.id) ||
            (this.id != null && this.id.equals(u.id))
        );
    }
}

