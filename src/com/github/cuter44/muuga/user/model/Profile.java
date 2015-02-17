package com.github.cuter44.muuga.user.model;

import java.util.Properties;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.github.cuter44.muuga.conf.*;

public class Profile
{
    public static final long serialVersionUID = 1L;
    private static final String gravatarPattern = Configurator.getInstance().get("muuga.user.avatar.defaulturl");

  // fields
    /** id
     */
    protected Long id;
    /** fkey ref
     */
    protected User user;

    /** 显示名(display-name)
     * 由用户自行决定
     */
    protected String dname;
    /** <del>Trusted Name
     * 由系统认证功能产生</del>
     * @deprecated
     */
    protected String tname;
    /** 签名
     */
    protected String motto;
    /** 头像
     */
    protected String avatar;
    /** 预留 geohash 字段, 也可以自愿性地提供
     */
    protected String pos;

    /** :"%s[,%0.2d]", 用户首页的背景图像, 取 isbn 为 %s 的书的封面, 从高度的 %d (比例, 缺省为0)开始的矩形区域.
     */
    protected String bg;

    /** 额外字段, 接受其他模块填入
     * 通常是一些导出值
     */
    public Properties others;

  // GET/SET
    public Long getId()
    {
        return(this.id);
    }
    public void setId(Long aId)
    {
        this.id = aId;
    }

    public User getUser()
    {
        return(this.user);
    }
    public void setUser(User aUser)
    {
        this.user = aUser;
    }

    public String getDname()
    {
        return(this.dname);
    }
    public void setDname(String aDname)
    {
        this.dname = aDname;
    }

    public String getTname()
    {
        return(this.tname);
    }
    public void setTname(String aTname)
    {
        this.tname = aTname;
    }

    public String getMotto()
    {
        return(this.motto);
    }
    public void setMotto(String aMotto)
    {
        this.motto = aMotto;
    }

    public String getAvatar()
    {
        return(this.avatar);
    }
    public void setAvatar(String aAvatar)
    {
        this.avatar = aAvatar;
    }

    public String getPos()
    {
        return(this.pos);
    }
    public void setPos(String aPos)
    {
        this.pos = aPos;
    }

    public String getBg()
    {
        return(this.bg);
    }
    public void setBg(String aBg)
    {
        this.bg = aBg;
    }

  // UTIL
    // based on com.github.cuter44.util.crypto.CryptoUtil
    private static String md5(String in)
    {
        try
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] bytes = md.digest(in.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder(bytes.length*2);
            for (int i=0; i<bytes.length; i++)
                sb.append(
                    String.format("%02x", bytes[i] & 0xff)
                );
            return(sb.toString());
        }
        catch (NoSuchAlgorithmException ex)
        {
            // never occur
            ex.printStackTrace();
            return(null);
        }
        catch (UnsupportedEncodingException ex)
        {
            // never occur
            ex.printStackTrace();
            return(null);
        }
    }

  // CONSTRUCT
    public Profile()
    {
        this.others = new Properties();

        return;
    }

    public Profile(User u)
    {
        this();

        this.user = u;

        if (u.getMail() != null)
        {
            // generate default dname
            this.dname = u.getMail().replaceAll("@.+", "");

            // generate default avatar
            this.avatar = gravatarPattern.replace(":key", md5(u.getMail()));
        }

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

        Profile p = (Profile)o;

        return(
            (this.id == p.id) ||
            (this.id != null && this.id.equals(p.id))
        );
    }
}

