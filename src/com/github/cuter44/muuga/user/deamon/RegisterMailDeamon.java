package com.github.cuter44.muuga.user.deamon;

import org.stringtemplate.v4.*;
import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.github.cuter44.nyafx.mail.*;

import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.conf.*;

public class RegisterMailDeamon
    implements Runnable
{
    protected CryptoBase crypto;
    protected Mailer mailer;

    protected STGroup stg;
    protected EventQueue<User> eq;
    protected String strWebBase;

    public RegisterMailDeamon()
    {
        this.crypto = CryptoBase.getInstance();
        this.mailer = Mailer.getInstance();

        this.stg = new STGroupFile("/activate-mail.html.stg");
        this.eq = new EventQueue<User>();

        Configurator conf = Configurator.getInstance();
        this.strWebBase = conf.get("muuga.server.web.baseurl");

        return;
    }

    public void run()
    {
        Authorizer.getInstance().addRegisterListener(this.eq);

        try
        {
            while (!Thread.currentThread().isInterrupted())
            {
                Event<User> ev = this.eq.take();
                User u = ev.getContext();

                ST tContent = this.stg.getInstanceOf("content")
                    .add("user", u)
                    .add("pass", this.crypto.byteToHex(u.getPass()))
                    .add("webbase", this.strWebBase);

                String content = tContent.render();

                ST tTitle = this.stg.getInstanceOf("title");

                String title = tTitle.render();

                this.mailer.sendHTMLMail(
                    u.getMail(), title, content
                );
            }
        }
        catch (InterruptedException ex)
        {
            return;
        }
        catch (Exception ex)
        {
            throw(new RuntimeException("Activate mail sender deamon failed.", ex));
        }
    }
}
