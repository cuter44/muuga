package com.github.cuter44.muuga.easemob.core;

import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.alibaba.fastjson.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.user.model.*;

/**
 * 同步事件触发器 UserActivateTrigger
 *
 * 侦听用户激活
 */
public class UserActivateTrigger implements EventSink<User>
{
    protected Easemob easemob;
    protected CryptoBase crypto;

    public UserActivateTrigger()
    {
        this.easemob = Easemob.getInstance();
        this.crypto = CryptoBase.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static UserActivateTrigger instance = new UserActivateTrigger();
    }

    public static UserActivateTrigger getInstance()
    {
        return(Singleton.instance);
    }

  // EVENT
    public boolean dispatch(Event<User> ev)
    {
        try
        {
            User u = ev.getContext();

            String username = u.getId().toString();
            String password = this.crypto.bytesToHex(
                this.crypto.randomBytes(8)
            );

            JSONObject result = this.easemob.postUser(username, password);

            System.out.println(result);

        }
        catch (Exception ex)
        {
            throw(new RuntimeException(ex));
        }
        return(true);
    }

}
