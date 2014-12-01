package com.github.cuter44.muuga.sys.servlet;

import javax.servlet.*;

import java.util.List;
import java.util.ArrayList;

import com.github.cuter44.muuga.conf.*;

public class DeamonRoot
    implements ServletContextListener
{
    protected static String KEY_DEAMONS = "muuga.deamons";

    protected ServletContext servletContext;
    protected List<Thread> deamons;

    @Override
    public void contextInitialized(ServletContextEvent ev)
    {
        this.servletContext = ev.getServletContext();
        this.deamons = new ArrayList<Thread>();

        Configurator conf = Configurator.getInstance();
        String value = conf.get(KEY_DEAMONS);
        String[] classes = value.split(";");

        for (String clazz:classes)
        {
            try
            {
                if (clazz == null || "".equals(clazz))
                    continue;

                Class c = Class.forName(clazz);
                Runnable rDeamon = (Runnable)c.newInstance();

                Thread thDeamon = new Thread(rDeamon, "deamon-"+clazz);
                this.servletContext.log("Start deamon:"+clazz);
                thDeamon.start();

                this.deamons.add(thDeamon);
            }
            catch (Exception ex)
            {
                this.servletContext.log("Error on starting"+clazz, ex);
            }
        }

        return;
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev)
    {
        for (Thread t:this.deamons)
        {
            this.servletContext.log("Stop deamon:"+t.getName());
            t.interrupt();
        }

        return;
    }
}
