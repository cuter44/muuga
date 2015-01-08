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

public class Login
{
    protected Long      uid;
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

        return(this.sPublic);
    }

    public KeyPair genClientKey()
        throws Exception
    {
        System.out.println("ENTER:genClientKey");

        this.cKeyPair = this.rsa.generateKey();

        return(this.cKeyPair);
    }

    public byte[] login()
        throws Exception
    {
        System.out.println("ENTER:login");

        String encryptedPass = this.rsa.byteToHex(
            this.rsa.encrypt(this.pass.getBytes("utf-8"), this.sPublic)
        );

        byte[] m = ((RSAPublicKey)this.cKeyPair.getPublic()).getModulus().toByteArray();
        if (m[0] == 0)
            m = Arrays.copyOfRange(m, 1, m.length);

        byte[] e = ((RSAPublicKey)this.cKeyPair.getPublic()).getPublicExponent().toByteArray();
        if (e[0] == 0)
            e = Arrays.copyOfRange(e, 1, e.length);

        String resp = Request.Post(apibase+"/user/login.api")
            .bodyForm(
                Form.form()
                .add("uid", this.uid.toString())
                .add("pass", encryptedPass)
                .add("m", this.rsa.byteToHex(m))
                .add("e", this.rsa.byteToHex(e))
                .build()
            )
            //.viaProxy(new HttpHost("localhost", 8888))
            .execute()
            .returnContent()
            .asString();

        System.out.println("response:"+resp);
        JSONObject json = (JSONObject)JSONObject.parse(resp);

        byte[] secret = this.rsa.hexToBytes(json.getString("secret"));

        String decryptedSecret = this.rsa.byteToHex(
            this.rsa.decrypt(secret, this.cKeyPair.getPrivate())
        );

        System.out.println("decrypted secret:"+decryptedSecret);

        return(secret);
    }

    @Test
    public void testLogin()
        throws Exception
    {
        Scanner scn = new Scanner(System.in);

        System.out.println("uid?");
        this.uid = scn.nextLong();
        scn.nextLine();

        System.out.println("pass?");
        this.pass = scn.nextLine();

        this.getServerKey();
        this.genClientKey();
        this.login();
    }

    public static void main(String[] args)
    {
        try
        {
            conf();
            new Login().testLogin();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
