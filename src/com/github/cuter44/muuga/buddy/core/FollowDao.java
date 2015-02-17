package com.github.cuter44.muuga.buddy.core;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.buddy.model.*;

public class FollowDao extends DaoBase<Follow>
{
  // CONSTRUCT
    public FollowDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static FollowDao instance = new FollowDao();
    }

    public static FollowDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(Follow.class);
    }

  // EXTENDED
    public Follow get(Long me, Long op)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(Follow.class)
            .add(Restrictions.eq("me", me))
            .add(Restrictions.eq("op", op));

        return(
            this.get(dc)
        );
    }

    public Follow create(Long me, Long op)
    {
        Follow f = new Follow(me, op);

        this.save(f);

        return(f);
    }

    public Long countFollow(Long me)
    {
        return(
            this.count(
                DetachedCriteria.forClass(Follow.class)
                    .add(Restrictions.eq("me", me))
            )
        );
    }

    public Long countFollowed(Long op)
    {
        return(
            this.count(
                DetachedCriteria.forClass(Follow.class)
                    .add(Restrictions.eq("op", op))
            )
        );
    }
}
