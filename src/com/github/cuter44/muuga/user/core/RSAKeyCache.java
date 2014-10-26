package com.github.cuter44.muuga.user.core;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import net.sf.ehcache.*;
import com.github.cuter44.nyafx.crypto.*;

import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.user.dao.*;

/** 生成及缓存 RSA 密钥对
 * 生成的公钥将直接返回, 以 Long 值为键稍后取回私钥
 */
public class RSAKeyCache
{
  // CONSTRUCT
    protected Cache cache;

    protected RSACrypto rsa;

    public RSAKeyCache()
    {
        this.cache = CacheManager.getInstance().getCache("RSAKeyCache");
        if (this.cache == null)
            throw(new RuntimeException("Get RSAKeyCache failed, ehcache.xml missing or incorrect."));

        Long ttl = Configurator.getInstance().getLong("muuga.user.rsakeyttl");
        if (ttl != null)
        {
            this.cache.getCacheConfiguration()
                .setTimeToLiveSeconds(ttl);
        }

        this.rsa = RSACrypto.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static final RSAKeyCache instance = new RSAKeyCache();
    }

    public static RSAKeyCache getInstance()
    {
        return(Singleton.instance);
    }

  //
    public void put(Long id, PrivateKey key)
    {
        this.cache.put(new Element(id, key));

        return;
    }

    public PrivateKey get(Long id)
    {
        Element e = this.cache.get(id);
        return(
            e!=null ? (PrivateKey)e.getObjectValue() : null
        );
    }

    /** 生成一对密钥, 并返回其中的公钥
     */
    public PublicKey genKey(Long id)
    {
        KeyPair kp = this.rsa.generateKey();
        this.put(id, kp.getPrivate());

        return(kp.getPublic());
    }
}
