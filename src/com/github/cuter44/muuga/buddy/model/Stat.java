package com.github.cuter44.muuga.buddy.model;

import java.io.Serializable;
import java.util.Date;

import com.github.cuter44.muuga.user.model.*;

public class Stat
    implements Serializable
{
  // SERIAL
    public static final long serialVersionUID = 1L;

  // FIELDS
    /** id
     */
    protected Long id;

    /** fkey_user
     */
    protected User user;

    /** 关注数
     */
    protected Long follow;

    /** 关注数
     */
    protected Long followed;

    /** 关注数
     */
    protected Long hate;

    /** 关注数
     */
    protected Long hated;

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

    public User getUser()
    {
        return(this.user);
    }

    public void setUser(User newUser)
    {
        this.user = newUser;

        return;
    }

    public Long getFollow()
    {
        return(this.follow);
    }

    public void setFollow(Long newFollow)
    {
        this.follow = newFollow;
        return;
    }

    public Long getFollowed()
    {
        return(this.followed);
    }

    public void setFollowed(Long newFollowed)
    {
        this.followed = newFollowed;
        return;
    }

    public Long getHate()
    {
        return(this.hate);
    }

    public void setHate(Long newHate)
    {
        this.hate = newHate;
        return;
    }

    public Long getHated()
    {
        return(this.hated);
    }

    public void setHated(Long newHated)
    {
        this.hated = newHated;
        return;
    }

  // CONSTRUCT
    public Stat()
    {
        return;
    }

    public Stat(User u)
    {
        this();

        this.user = u;

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

        Follow b =(Follow)o;

        return(
            (this.id == b.id) ||
            (this.id != null && this.id.equals(b.id))
        );
    }
}
