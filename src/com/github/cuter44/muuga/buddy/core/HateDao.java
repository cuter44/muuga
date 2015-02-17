package com.github.cuter44.muuga.buddy.core;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.buddy.model.*;

public class HateDao extends DaoBase<Hate>
{
  // CONSTRUCT
    public HateDao()
    {
        super();
    }

  // SINGLETON
    private static class Singleton
    {
        public static HateDao instance = new HateDao();
    }

    public static HateDao getInstance()
    {
        return(Singleton.instance);
    }

  // GET
    @Override
    public Class classOfT()
    {
        return(Hate.class);
    }

  // EXTENDED
    public Hate get(Long me, Long op)
    {
        DetachedCriteria dc = DetachedCriteria.forClass(Hate.class)
            .add(Restrictions.eq("me", me))
            .add(Restrictions.eq("op", op));

        return(
            this.get(dc)
        );
    }

    public Hate create(Long me, Long op)
    {
        Hate h = new Hate(me, op);

        this.save(h);

        return(h);
    }

    public Long countHate(Long me)
    {
        return(
            this.count(
                DetachedCriteria.forClass(Hate.class)
                    .add(Restrictions.eq("me", me))
            )
        );
    }

    public Long countHated(Long op)
    {
        return(
            this.count(
                DetachedCriteria.forClass(Hate.class)
                    .add(Restrictions.eq("op", op))
            )
        );
    }
}

