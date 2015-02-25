package com.github.cuter44.muuga.user.daemon;

import org.stringtemplate.v4.*;
import com.github.cuter44.nyafx.event.*;
import com.github.cuter44.nyafx.crypto.*;
import com.github.cuter44.nyafx.mail.*;

import com.github.cuter44.muuga.user.core.*;
import com.github.cuter44.muuga.user.model.*;
import com.github.cuter44.muuga.conf.*;
import static com.github.cuter44.muuga.Constants.*;

public class ActivateMailDaemon
    implements Runnable
{
    protected CryptoBase crypto;
    protected Mailer mailer;

    protected STGroup stg;
    protected EventQueue<User> eq;
    protected String strWebBase;

    public ActivateMailDaemon()
    {
        this.crypto = CryptoBase.getInstance();
        this.mailer = Mailer.getInstance();

        this.stg = new STGroupFile("/activate-mail.html.stg");
        this.eq = new EventQueue<User>();

        Configurator conf = Configurator.getInstance();
        this.strWebBase = conf.get("muuga.server.web.baseurl");

        EventHub.getInstance().subscribe(EVENTTYPE_USER_REGISTER, this.eq);

        return;
    }

    public void run()
    {

        while (!Thread.currentThread().isInterrupted())
        {
            try
            {
                Event<User> ev = this.eq.take();
                User u = ev.getContext();

                ST tContent = this.stg.getInstanceOf("content")
                    .add("user", u)
                    .add("code", this.crypto.byteToHex(u.getPass()))
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
