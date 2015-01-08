package com.github.cuter44.muuga.user.core;

import java.nio.ByteBuffer;
import java.util.Date;
import java.util.Arrays;
import java.util.ArrayList;
//import java.util.concurrent.LinkedBlockingQueue;

import com.github.cuter44.nyafx.dao.*;
import static com.github.cuter44.nyafx.dao.EntityNotFoundException.entFound;
import com.github.cuter44.nyafx.crypto.*;
import com.github.cuter44.nyafx.event.*;
import org.hibernate.criterion.*;

import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.user.exception.*;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.Constants;

/** 身份验证及访问控制模块
 * 提供基于帐号密码的身份验证实现:
 * * 注册(register)
 *   要求用户提供邮件地址, 然后为该邮件地址分配用户ID, 及发送激活邮件
 * * 激活(activate)
 *   用户获得并回答(从其他信道获得的)激活码, 申请密钥对并指定(RSA加密的)登录密码
 *   用户给定的密码在服务器端解密, 并与随机生成的盐拼接, 散列后写入数据库
 *
 * 以拦截器的方式提供基于密码或session key的验证服务
 */
public class Authorizer
{
    protected static final String EVENT_TYPE_REGISTER = Constants.EVENT_TYPE_REGISTER;

  // CONSTRUCT
    protected static Integer SECRET_LENGTH;
    protected static Integer SALT_LENGTH;

    protected UserDao userDao;
    protected RSACrypto rsa;
    protected EventHub eventHub;

    public Authorizer()
    {
        Configurator config = Configurator.getInstance();

        SECRET_LENGTH = config.getInt("muuga.user.secretlength", 8);
        SALT_LENGTH = config.getInt("muuga.user.saltlength", 4);

        this.userDao = UserDao.getInstance();
        this.rsa = RSACrypto.getInstance();
        this.eventHub = EventHub.getInstance();

        return;
    }

  // SINGLETON
    private static class Singleton
    {
        public static Authorizer instance = new Authorizer();
    }

    public static Authorizer getInstance()
    {
        return(Singleton.instance);
    }




  // BASE
    /** 变更密码
     * @param uid
     * @param pass 新密码, UTF-8编码
     * @exception EntityNotFoundException 指定的uid不存在时
     */
    public User setPassword(Long uid, byte[] pass)
    {
        User user = (User)entFound(this.userDao.get(uid));

        byte[] salt = this.rsa.randomBytes(SALT_LENGTH);
        user.setSalt(salt);

        byte[] buf = ByteBuffer.allocate(pass.length + salt.length)
            .put(pass)
            .put(salt)
            .array();
        user.setPass(this.rsa.MD5Digest(buf));

        this.userDao.update(user);

        return(user);
    }

    /** 验证登录密码
     * @param uid
     * @param pass 登录密码, UTF-8编码
     * @return boolean 密码正确与否
     * @exception IllegalArgumentException 当用户名或密码为空时
     * @exception EntityNotFoundException 当 uid 不存在时
     */
    public boolean verifyPassword(Long uid, byte[] pass)
        throws IllegalArgumentException, EntityNotFoundException
    {
        User user = (User)entFound(this.userDao.get(uid));

        byte[] salt = user.getSalt();
        byte[] buf = ByteBuffer.allocate(pass.length + salt.length)
            .put(pass)
            .put(salt)
            .array();
        buf = this.rsa.MD5Digest(buf);

        if (Arrays.equals(user.getPass(), buf))
            return(true);
        else
            return(false);
    }

    ///** 验证 session key
     //* @param uid
     //* @param skey session key
     //* @return boolean session key 正确与否
     //* @exception NullPointerException 当uid为null
     //*/
    //public boolean verifySkey(Long uid, byte[] skey)
        //throws IllegalArgumentException, EntityNotFoundException
    //{
        //User user = (User)entFound(this.userDao.get(uid));

        //if (Arrays.equals(user.getSkey(), skey))
            //return(true);
        //else
            //return(false);
    //}

    /** 清除 secret, 即注销登录, 通过登录密码
     * @param uid , 需要注销登录的 uid
     * @param pass , UTF-8 编码的登录密码
     * @exception EntityNotFoundException 当指定的uid不存在时
     * @exception UnauthorizedException 当pass错误时
     */
    public User logoutViaPass(Long uid, byte[] pass)
    {
        if (!this.verifyPassword(uid, pass))
            throw(new UnauthorizedException());

        User user = this.userDao.get(uid);

        user.setSecret(null);

        this.userDao.update(user);

        return(user);
    }

    ///** 清除 session key, 即注销登录
     //* @param uid , 需要注销登录的 uid
     //* @param skey , session key
     //* @exception EnetityNotFoundException 当指定的uid不存在时
     //* @exception UnauthorizedException 当skey不正确时
     //*/
    //public void logoutViaSkey(Long uid, byte[] skey)
    //{
        //if (!this.verifySkey(uid, skey))
            //throw(new UnauthorizedException());

        //User user = this.userDao.get(uid);

        //user.setSkey(null);

        //this.userDao.update(user);

        //return;
    //}

    public User logoutViaSecret(Long uid)
    {
        User user = (User)entFound(this.userDao.get(uid));

        user.setSecret(null);

        this.userDao.update(user);

        return(user);
    }

  // EX
    /** 注册新帐号
     * @param mail 邮件地址
     * @return 对于未注册过的地址或注册但未激活地址返回状态为 REGISTER 的 User 对象
     * @exception EntityDuplicatedException 除上述状况时
     */
    public User register(String mail)
    {
        User user = this.userDao.forMail(mail);

        if (user != null)
        {
            if (!User.STATUS_REGISTERED.equals(user.getStatus()))
                throw(new EntityDuplicatedException("Mail address is occupied:"+mail));
        }
        else
        {
            user = new IndividualUser(mail);

            user.setStatus(User.STATUS_REGISTERED);
            user.setRegDate(new Date(System.currentTimeMillis()));

            byte[] pass = ByteBuffer.allocate(16).put(this.rsa.randomBytes(16)).array();

            user.setPass(pass);

            this.userDao.save(user);

        }

        this.eventHub.dispatch(this.EVENT_TYPE_REGISTER, new Event(user));

        return(user);
    }

    /** 激活帐号并设定登录密码
     * @param uid 注册时邮件发送的 uid
     * @param activateCode 注册时通过邮件发送的验证码
     * @param newPass 登录密码的UTF-8编码, 实际为该用户的 pass 域
     * @exception EntityNotFoundException 当指定的uid不存在时
     */
    public User activate(Long uid, byte[] activateCode, byte[] newPass)
    {
        User user = (User)entFound(this.userDao.get(uid));

        if (!User.STATUS_REGISTERED.equals(user.getStatus()))
            throw(new LoginBlockedException());

        if (!Arrays.equals(user.getPass(), activateCode))
            throw(new UnauthorizedException());

        this.setPassword(uid, newPass);

        user.setStatus(User.STATUS_ACTIVATED);

        this.userDao.update(user);

        return(user);
    }

    /** 登录
     * @param uid
     * @param pass UTF-8 编码的登录密码
     * @exception EntityNotFoundException 当指定uid不存在时
     * @exception UnauthorizedException 当pass不正确时
     */
    public User login(Long uid, byte[] pass)
        throws UnauthorizedException, EntityNotFoundException
    {
        User user = (User)entFound(this.userDao.get(uid));

        // 验证状态
        if (!User.STATUS_ACTIVATED.equals(user.getStatus()))
            throw(new LoginBlockedException());

        // 验证密码
        if (!this.verifyPassword(uid, pass))
            throw(new UnauthorizedException());

        if (user.getSecret() == null)
        {
            user.setSecret(this.rsa.randomBytes(SECRET_LENGTH));
            this.userDao.update(user);
        }

        return(user);
    }

    public User passwd(Long uid, byte[] pass, byte[] newpass)
        throws UnauthorizedException, EntityNotFoundException
    {
        // 验证密码
        if (!this.verifyPassword(uid, pass))
            throw(new UnauthorizedException());

        return(
            this.setPassword(uid, newpass)
        );
    }
}
