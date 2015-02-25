package com.github.cuter44.muuga.easemob.core;

import java.util.concurrent.atomic.AtomicInteger;
import java.io.IOException;

import org.apache.http.HttpHost;
import org.apache.http.client.fluent.*;
import org.apache.http.entity.ContentType;
import com.alibaba.fastjson.*;
import com.github.cuter44.nyafx.ssl.*;

import com.github.cuter44.muuga.conf.*;

public class Easemob
{
    protected static final String URL_SERVER_BASE = "https://a1.easemob.com";

    protected Configurator conf;

    protected String token;
    protected long expire;

    protected static final String KEY_ORG_NAME = "easemob.orgname";
    protected String orgName;
    protected static final String KEY_APP_NAME = "easemob.appname";
    protected String appName;
    protected String apiBase;

    protected static final String KEY_CLIENT_ID = "easemob.clientid";
    protected String clientId;
    protected static final String KEY_CLIENT_SECRET = "easemob.clientsecret";
    protected String clientSecret;

    public Easemob()
    {
        this.conf = Configurator.getInstance();

        this.orgName = this.conf.get(KEY_ORG_NAME);
        this.appName = this.conf.get(KEY_APP_NAME);
        this.apiBase = URL_SERVER_BASE + "/" + orgName + "/" + appName;

        this.clientId       = this.conf.get(KEY_CLIENT_ID);
        this.clientSecret   = this.conf.get(KEY_CLIENT_SECRET);

        this.expire = System.currentTimeMillis();

        return;
    }

    private static class Singleton
    {
        public static Easemob instance = new Easemob();
    }

    public static Easemob getInstance()
    {
        return(Singleton.instance);
    }

    public JSONObject getToken()
        throws IOException
    {
        String path = this.apiBase + "/token";

        JSONObject reqBody = new JSONObject();
        reqBody.put("grant_type"    , "client_credentials"  );
        reqBody.put("client_id"     , this.clientId         );
        reqBody.put("client_secret" , this.clientSecret     );

        JSONObject respBody = JSONObject.parseObject(
            Request.Post(path)
                .bodyString(
                    reqBody.toString(),
                    ContentType.APPLICATION_JSON
                )
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        this.token  = respBody.getString("access_token");
        //System.out.println(this.token);
        this.expire = respBody.getLong("expires_in")*1000L + System.currentTimeMillis() - 10000L;
        //System.out.println(respBody.getLong("expires_in"));
        //System.out.println(this.expire);

        return(respBody);
    }

    public JSONObject postUser(String username, String password)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/users";

        JSONObject reqBody = new JSONObject();
        reqBody.put("username"  , username  );
        reqBody.put("password"  , password  );

        JSONObject respBody = JSONObject.parseObject(
            Request.Post(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .bodyString(
                    reqBody.toString(),
                    ContentType.APPLICATION_JSON
                )
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);
    }

    public JSONObject putPassword(String username, String newpassword)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/users/"+username+"/password";

        JSONObject reqBody = new JSONObject();
        reqBody.put("newpassword", newpassword);

        JSONObject respBody = JSONObject.parseObject(
            Request.Put(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .bodyString(
                    reqBody.toString(),
                    ContentType.APPLICATION_JSON
                )
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);
    }

    public JSONObject deleteUser(String username)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/users/"+username;

        JSONObject respBody = JSONObject.parseObject(
            Request.Delete(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);
    }

    public JSONObject postContact(String ownerUsername, String friendUsername)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/users/"+ownerUsername+"/cotancts/users/"+friendUsername;

        JSONObject respBody = JSONObject.parseObject(
            Request.Post(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);

    }

    public JSONObject deleteContact(String ownerUsername, String friendUsername)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/users/"+ownerUsername+"/cotancts/users/"+friendUsername;

        JSONObject respBody = JSONObject.parseObject(
            Request.Delete(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);

    }

    public JSONObject postBlock(String ownerUsername, String ... blockedUsername)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        JSONObject reqBody = new JSONObject();
        reqBody.put("usernames", blockedUsername);

        String path = this.apiBase + "/users/"+ownerUsername+"/blocks/users";

        JSONObject respBody = JSONObject.parseObject(
            Request.Post(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .bodyString(
                    reqBody.toString(),
                    ContentType.APPLICATION_JSON
                )
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);

    }

    public JSONObject deleteBlock(String ownerUsername, String blockedUsername)
        throws IOException
    {
        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/users/"+ownerUsername+"/blocks/users/"+blockedUsername;

        JSONObject respBody = JSONObject.parseObject(
            Request.Delete(path)
                .setHeader("Authorization", "Bearer " + this.token)
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);

    }

    private static AtomicInteger msgCount = new AtomicInteger(0);
    private static long msgDuration = System.currentTimeMillis();
    protected static boolean isMsgFrequencyExcess()
    {
        if (msgCount.incrementAndGet() < 30)
            return(false);

        if ((System.currentTimeMillis() - msgDuration) < 1000L)
            return(true);

        // else
        synchronized(msgCount)
        {
            if ((System.currentTimeMillis() - msgDuration) > 1000L)
            {
                msgCount.set(0);
                msgDuration = System.currentTimeMillis();
            }

            // make thread retry
            return(true);
        }
    }

    private static final JSONObject POST_MSG_CMD_MSG = JSON.parseObject("{\"type\":\"cmd\",\"action\":\"action1\"}");

    /** 发送透传消息
     * @param target 目标用户名
     * @param ext 附加的json
     */
    public JSONObject postMsgCmd(JSONObject ext, String ... target)
        throws IOException
    {
        while (isMsgFrequencyExcess())
            Thread.yield();

        if (System.currentTimeMillis() > this.expire)
            this.getToken();

        String path = this.apiBase + "/messages";

        JSONObject reqBody = new JSONObject();
        reqBody.put("target_type", "users");
        reqBody.put("target", target);
        reqBody.put("msg", POST_MSG_CMD_MSG);
        reqBody.put("ext", ext.toJSONString());

        JSONObject respBody = JSONObject.parseObject(
            Request.Post(path)
                //.viaProxy(new HttpHost("127.0.0.1", 8888))
                .setHeader("Authorization", "Bearer " + this.token)
                .bodyString(
                    reqBody.toString(),
                    ContentType.APPLICATION_JSON
                )
                .execute()
                .returnContent()
                .asString()
        );

        //System.out.println(respBody);

        return(respBody);
    }

    public static void main(String[] args)
    {
        try
        {
            Configurator conf = Configurator.getInstance();
            String confValue = conf.get("nyafx.ssl.certificates");
            if (confValue == null)
                return;

            String[] certs = confValue.split(",");

            new CertificateLoader()
                .loadX509CertResource(certs)
                .overrideDefaultSSLContext("TLSv1");

            Easemob easemob = Easemob.getInstance();

            System.out.println(easemob.getToken());
            System.out.println(easemob.postUser("19232", "1238432"));
            System.out.println(easemob.putPassword("19232", "123wefce"));
            System.out.println(easemob.postMsgCmd(new JSONObject(), "19232"));
            System.out.println(easemob.deleteUser("19232"));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

}
