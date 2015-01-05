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
        public abstract void onGet(Profile profile);
    }

    protected ArrayList<ProfileListener> profileListeners = new ArrayList<ProfileListener>();

    public synchronized void addListener(ProfileListener l)
    {
        profileListeners.add(l);

        return;
    }

  // CONSTRUCT
    protected UserDao userDao;

    public ProfileDao()
    {
        super();

        this.userDao = UserDao.getInstance();
    }

  // Singleton
    private static class Singleton
    {
        public static ProfileDao instance = new ProfileDao();
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
        Profile profile = super.get(id);

        if (profile == null)
        {
            if (this.userDao.get(id) != null)
                profile = this.create(id);
        }

        for (ProfileListener pl:this.profileListeners)
            pl.onGet(profile);

        return(profile);
    }

    public Profile create(Long id)
        throws EntityNotFoundException
    {
        User u = (User)entFound(this.userDao.get(id));

        Profile profile = new Profile(u);

        this.save(profile);

        return(profile);
    }
}
