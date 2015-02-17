package com.github.cuter44.muuga.buddy.core;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.buddy.model.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.core.*;

public class StatDao extends DaoBase<Stat>
{
    protected UserDao   userDao;
    protected FollowDao fDao;
    protected HateDao   hDao;

  // CONSTRUCT
    public StatDao()
    {
        this.userDao = UserDao.getInstance();
        this.fDao    = FollowDao.getInstance();
        this.hDao    = HateDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static StatDao instance = new StatDao();
    }

    public static StatDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(Stat.class);
    }

  // EXTENDED
    protected Stat create(Long uid)
    {
        User u = (User)entFound(this.userDao.get(uid));
        Stat s = new Stat(u);

        s.setFollow(
            this.fDao.countFollow(uid)
        );
        s.setFollowed(
            this.fDao.countFollowed(uid)
        );

        s.setHate(
            this.hDao.countHate(uid)
        );
        s.setHated(
            this.hDao.countHated(uid)
        );

        this.save(s);

        return(s);
    }

    public Stat get(Long uid)
    {
        Stat s = super.get(uid);

        if (s == null)
            s = this.create(uid);

        return(s);
    }

    public Stat updateFollowStat(Long uid)
    {
        Stat s = this.get(uid);

        s.setFollow(
            this.fDao.countFollow(uid)
        );

        this.update(s);

        return(s);
    }

    public Stat updateFollowedStat(Long uid)
    {
        Stat s = this.get(uid);

        s.setFollowed(
            this.fDao.countFollowed(uid)
        );

        this.update(s);

        return(s);
    }

    public Stat updateHateStat(Long uid)
    {
        Stat s = this.get(uid);

        s.setHate(
            this.hDao.countHate(uid)
        );

        this.update(s);

        return(s);
    }

    public Stat updateHatedStat(Long uid)
    {
        Stat s = this.get(uid);

        s.setHated(
            this.hDao.countHated(uid)
        );

        this.update(s);

        return(s);
    }

    public Stat updateStat(Long uid)
    {
        Stat s = this.get(uid);

        s.setFollow(
            this.fDao.countFollow(uid)
        );

        s.setFollowed(
            this.fDao.countFollowed(uid)
        );

        s.setHate(
            this.hDao.countHate(uid)
        );

        s.setHated(
            this.hDao.countHated(uid)
        );

        this.update(s);

        return(s);
    }
}
