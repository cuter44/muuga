package com.github.cuter44.muuga.easemob.core;

import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.alibaba.fastjson.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.buddy.model.*;

/**
 * 同步事件触发器 BuddyUnfollowTrigger
 *
 * 删除好友
 */
public class BuddyUnfollowTrigger implements EventSink<Follow>
{
    protected Easemob easemob;

    public BuddyUnfollowTrigger()
    {
        this.easemob = Easemob.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static BuddyUnfollowTrigger instance = new BuddyUnfollowTrigger();
    }

    public static BuddyUnfollowTrigger getInstance()
    {
        return(Singleton.instance);
    }

  // EVENT
    public boolean dispatch(Event<Follow> ev)
    {
        try
        {
            Follow f = ev.getContext();

            String me = f.getMe().toString();
            String op = f.getOp().toString();

            JSONObject result = this.easemob.deleteContact(me, op);

            System.out.println(result);

        }
        catch (Exception ex)
        {
            throw(new RuntimeException(ex));
        }
        return(true);
    }

}
