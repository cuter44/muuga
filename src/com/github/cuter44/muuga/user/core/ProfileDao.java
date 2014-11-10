package com.github.cuter44.muuga.user.core;

import java.util.ArrayList;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.user.model.*;

public class ProfileDao extends DaoBase<Profile>
{
  // EVENT CALLBACK
    public static interface ProfileListener
    {
        public abstract void onGet(Profile p);
    }

    protected ArrayList<ProfileListener> profileListeners = new ArrayList<ProfileListener>();

    public synchronized void addListener(ProfileListener l)
    {
        profileListeners.add(l);

        return;
    }

  // CONSTRUCT
    protected UserDao userDao = UserDao.getInstance();
    public ProfileDao()
    {
        super();
    }

  // Singleton
    private static class Singleton
    {
        public static final ProfileDao instance = new ProfileDao();
    }

    public static ProfileDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(Profile.class);
    }

  // EXTENDED
    public Profile get(Long id)
    {
        Profile p = super.get(id);

        if (p == null)
        {
            if (this.userDao.get(id) != null)
                p = this.create(id);
        }

        for (ProfileListener pl:this.profileListeners)
            pl.onGet(p);

        return(p);
    }

    public Profile create(Long id)
        throws EntityNotFoundException
    {
        User u = (User)entFound(this.userDao.get(id));

        Profile p = new Profile(u);

        this.save(p);

        return(p);
    }
}
