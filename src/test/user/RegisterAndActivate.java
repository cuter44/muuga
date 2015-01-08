package test.user;

import java.util.Scanner;
import java.util.Arrays;
import java.security.PublicKey;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.security.interfaces.RSAPrivateKey;

import org.junit.*;
import org.apache.http.*;
import org.apache.http.client.fluent.*;
import com.alibaba.fastjson.*;
import com.github.cuter44.nyafx.crypto.*;

import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.conf.*;

public class RegisterAndActivate
{
    protected Long      uid;
    protected String    mail;
    protected String    code;
    protected String    pass;
    protected byte[]    sm;
    protected byte[]    se;
    protected PublicKey sPublic;
    protected KeyPair   cKeyPair;

    protected static String     apibase;
    protected static RSACrypto  rsa;

    @BeforeClass
    public static void conf()
    {
        apibase = Configurator.getInstance().get("muuga.server.api.baseurl");
        System.out.println("Using apibase:"+apibase);
        rsa = RSACrypto.getInstance();
    }

    public PublicKey getServerKey()
        throws Exception
    {
        System.out.println("ENTER:getServerKey");

        String resp = Request.Post(apibase+"/security/get-rsa-key.api")
            .bodyForm(
                Form.form()
                .add("uid", uid.toString())
                .build()
            )
            //.viaProxy(new HttpHost("localhost", 8888))
            .execute()
            .returnContent()
            .asString();

        System.out.println("response:"+resp);
        JSONObject json = (JSONObject)JSONObject.parse(resp);

        this.sm = this.rsa.hexToBytes(json.getString("m"));
        this.se = this.rsa.hexToBytes(json.getString("e"));
        this.sPublic = this.rsa.generatePublic(sm, se);
        System.out.println("parsed modulus:"+((RSAPublicKey)this.sPublic).getModulus().toString(16));
        System.out.println("parsed public exponent:"+((RSAPublicKey)this.sPublic).getPublicExponent().toString(16));

        return(this.sPublic);
    }

    public KeyPair genClientKey()
        throws Exception
    {
        System.out.println("ENTER:genClientKey");

        this.cKeyPair = this.rsa.generateKey();

        return(this.cKeyPair);
    }


    public void register()
        throws Exception
    {
        System.out.println("ENTER:register");

        String resp = Request.Post(apibase+"/user/register.api")
            .bodyForm(
                Form.form()
                .add("mail", this.mail)
                .build()
            )
            //.viaProxy(new HttpHost("localhost", 8888))
            .execute()
            .returnContent()
            .asString();

        System.out.println("response:"+resp);
        JSONObject json = (JSONObject)JSONObject.parse(resp);

        this.uid = json.getLong("id");

        System.out.println("uid:"+uid);

        return;
    }

    public void activate()
        throws Exception
    {
        System.out.println("ENTER:activate");

        System.out.println("plain pass:"+this.rsa.byteToHex(this.pass.getBytes("utf-8")));
        System.out.println("plain data byte-length:"+this.pass.getBytes("utf-8").length);
        System.out.println("key modulus bit-length:"+((RSAPublicKey)this.sPublic).getModulus().bitLength());

        String encryptedPass = this.rsa.byteToHex(
            this.rsa.encrypt(this.pass.getBytes("utf-8"), this.sPublic)
        );

        String resp = Request.Post(apibase+"/user/activate.api")
            .bodyForm(
                Form.form()
                .add("uid", this.uid.toString())
                .add("code", this.code)
                .add("pass", encryptedPass)
                .build()
            )
            //.viaProxy(new HttpHost("localhost", 8888))
            .execute()
            .returnContent()
            .asString();

        System.out.println("response:"+resp);
        JSONObject json = (JSONObject)JSONObject.parse(resp);

        return;
    }

    @Test
    public void testRegisterAndActivate()
        throws Exception
    {
        Scanner scn = new Scanner(System.in);

        System.out.println("mail?");
        this.mail = scn.nextLine();

        this.register();

        System.out.println("code?");
        this.code = scn.nextLine();

        System.out.println("pass?");
        this.pass = scn.nextLine();

        this.getServerKey();
        this.activate();
    }

    public static void main(String[] args)
    {
        try
        {
            conf();
            new RegisterAndActivate().testRegisterAndActivate();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
