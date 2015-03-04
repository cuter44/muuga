package com.github.cuter44.muuga.user.core;

import net.sf.ehcache.*;
import com.github.cuter44.nyafx.crypto.*;

import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;

import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.user.model.*;

/** 缓存用户的 secret 以为签名验证提供加速
 */
public class UserSecretCache
{
  // CONSTRUCT
    protected Cache cache;
    protected CryptoBase crypto;
    protected UserDao userDao;

    public UserSecretCache()
    {
        this.cache = CacheManager.getInstance().getCache("UserSecretCache");
        if (this.cache == null)
            throw(new RuntimeException("Get UserSecret failed, ehcache.xml missing or incorrect."));

        this.crypto = CryptoBase.getInstance();
        this.userDao = UserDao.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static final UserSecretCache instance = new UserSecretCache();
    }

    public static UserSecretCache getInstance()
    {
        return(Singleton.instance);
    }

  //
    protected void put(User u)
    {
        this.put(
            u.getId(),
            this.crypto.bytesToHex(u.getSecret())
        );

        return;
    }

    protected void put(Long uid, String secret)
    {
        this.cache.put(
            new Element(uid, secret)
        );

        return;
    }

    public String get(Long id)
    {
        Element e = this.cache.get(id);
        if (e != null)
            return((String)e.getObjectValue());

        // else
        User u = (User)entFound(this.userDao.get(id));
        byte[] bSecret = u.getSecret();
        if (bSecret == null)
            return(null);

        String sSecret = this.crypto.bytesToHex(bSecret);
        this.put(id, sSecret);

        return(sSecret);
    }

    public void drop(Long id)
    {
        this.cache.remove(id);
    }
}
