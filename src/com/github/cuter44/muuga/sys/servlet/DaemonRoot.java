package com.github.cuter44.muuga.sys.servlet;

import javax.servlet.*;

import java.util.List;
import java.util.ArrayList;

import com.github.cuter44.muuga.conf.*;

/** 守护线程加载器
 */
public class DaemonRoot
    implements ServletContextListener
{
    protected static String KEY_DAEMONS = "muuga.daemons";

    protected ServletContext servletContext;
    protected List<Thread> daemons;

    @Override
    public void contextInitialized(ServletContextEvent ev)
    {
        this.servletContext = ev.getServletContext();
        this.daemons = new ArrayList<Thread>();

        Configurator conf = Configurator.getInstance();
        String value = conf.get(KEY_DAEMONS);
        String[] classes = value.split(";");

        for (String clazz:classes)
        {
            try
            {
                if (clazz == null || "".equals(clazz))
                    continue;

                Class c = Class.forName(clazz);
                Runnable rDeamon = (Runnable)c.newInstance();

                Thread thDaemon = new Thread(rDeamon, "daemon-"+clazz);
                this.servletContext.log("Start daemon:"+clazz);
                thDaemon.start();

                this.daemons.add(thDaemon);
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
        for (Thread t:this.daemons)
        {
            this.servletContext.log("Stop daemon:"+t.getName());
            t.interrupt();
        }

        return;
    }
}
