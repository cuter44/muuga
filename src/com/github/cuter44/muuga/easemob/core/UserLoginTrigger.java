package com.github.cuter44.muuga.easemob.core;

import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.alibaba.fastjson.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.user.model.*;

/**
 * 同步事件触发器 UserLoginTrigger
 *
 * 侦听用户激活
 */
public class UserLoginTrigger implements EventSink<User>
{
    protected Easemob easemob;
    protected CryptoBase crypto;

    public UserLoginTrigger()
    {
        this.easemob = Easemob.getInstance();
        this.crypto = CryptoBase.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static UserLoginTrigger instance = new UserLoginTrigger();
    }

    public static UserLoginTrigger getInstance()
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
                u.getSecret()
            );

            JSONObject result = this.easemob.putPassword(username, password);

            System.out.println(result);

        }
        catch (Exception ex)
        {
            throw(new RuntimeException(ex));
        }
        return(true);
    }

}
