package com.github.cuter44.muuga.user.core;

import com.github.cuter44.nyafx.dao.DaoBase;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.user.dao.User;

public class UserDao extends DaoBase<User>
{
  // CONSTRUCT
    public UserDao()
    {
        super();
    }

  // Singleton
    private static class Singleton
    {
        public static final UserDao instance = new UserDao();
    }

    public static UserDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(User.class);
    }

  // EXTENDED
    public User forMail(String mail)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(User.class)
            .add(Restrictions.eq("mail", mail));

        return(
            this.get(dc)
        );
    }

    public User forUname(String uname)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(User.class)
            .add(Restrictions.eq("uname", uname));

        return(
            this.get(dc)
        );
    }

    public boolean isLoginable(Long id)
    {
        User u = this.get(id);
        return(
            User.ACTIVATED.equals(u.getStatus())
        );
    }

    public boolean isActivatable(Long id)
    {
        User u = this.get(id);
        return(
            User.REGISTERED.equals(u.getStatus())
        );
    }
}
