package com.github.cuter44.muuga.easemob.core;

import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.alibaba.fastjson.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.buddy.model.*;

/**
 * 同步事件触发器 BuddyUnhateTrigger
 *
 * 删除黑名单
 */
public class BuddyUnhateTrigger implements EventSink<Hate>
{
    protected Easemob easemob;

    public BuddyUnhateTrigger()
    {
        this.easemob = Easemob.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static BuddyUnhateTrigger instance = new BuddyUnhateTrigger();
    }

    public static BuddyUnhateTrigger getInstance()
    {
        return(Singleton.instance);
    }

  // EVENT
    public boolean dispatch(Event<Hate> ev)
    {
        try
        {
            Hate h = ev.getContext();

            String me = h.getMe().toString();
            String op = h.getOp().toString();

            JSONObject result = this.easemob.deleteBlock(me, op);

            System.out.println(result);

        }
        catch (Exception ex)
        {
            throw(new RuntimeException(ex));
        }
        return(true);
    }

}
