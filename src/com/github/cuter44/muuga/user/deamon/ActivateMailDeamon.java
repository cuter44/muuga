package com.github.cuter44.muuga.user.deamon;

import org.stringtemplate.v4.*;
import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.github.cuter44.nyafx.mail.*;

import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.Constants;

public class ActivateMailDeamon
    implements Runnable
{
    protected static final String EVENT_TYPE_REGISTER = Constants.EVENT_TYPE_REGISTER;

    protected CryptoBase crypto;
    protected Mailer mailer;

    protected STGroup stg;
    protected EventQueue<User> eq;
    protected String strWebBase;

    public ActivateMailDeamon()
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
        EventHub.getInstance().addListener(this.EVENT_TYPE_REGISTER, this.eq);

        while (!Thread.currentThread().isInterrupted())
        {
            try
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
            catch (Exception ex)
            {
                if (!(ex instanceof InterruptedException))
                    ex.printStackTrace();
            }
        }
    }
}
