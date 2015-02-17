package com.github.cuter44.muuga.buddy.core;

import java.util.Date;

import com.github.cuter44.muuga.buddy.model.*;

public class BuddyMgr
{
  // CONSTRUCT
    protected FollowDao fDao;
    protected HateDao   hDao;
    protected StatDao   sDao;

    public BuddyMgr()
    {
        this.fDao = FollowDao.getInstance();
        this.hDao = HateDao.getInstance();
        this.sDao = StatDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static BuddyMgr instance = BuddyMgr.getInstance();
    }

    public static BuddyMgr getInstance()
    {
        return(Singleton.instance);
    }

  // FOLLOW
    public Follow follow(Long me, Long op)
    {
        Follow f = this.fDao.get(me, op);
        if (f!=null)
            return(f);

        this.unhate(me, op);

        f = this.fDao.create(me, op);

        this.sDao.updateFollowStat(me);
        this.sDao.updateFollowedStat(op);

        return(f);
    }

    public void unfollow(Long me, Long op)
    {
        Follow f = this.fDao.get(me, op);
        if (f!=null)
            this.fDao.delete(f);

        return;
    }

  // HATE
    public Hate hate(Long me, Long op)
    {
        Hate h = this.hDao.get(me, op);
        if (h!=null)
            return(h);

        this.unfollow(me, op);

        h = this.hDao.create(me, op);

        this.sDao.updateHateStat(me);
        this.sDao.updateHatedStat(op);

        return(h);
    }

    public void unhate(Long me, Long op)
    {
        Hate h = this.hDao.get(me, op);
        if (h!=null)
            this.hDao.delete(h);

        return;
    }

  // STAT
    public Stat updateStat(Long id)
    {
        return(
            this.sDao.updateStat(id)
        );
    }

}
