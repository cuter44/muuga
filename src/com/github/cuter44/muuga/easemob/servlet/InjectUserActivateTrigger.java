package com.github.cuter44.muuga.easemob.servlet;

import javax.servlet.*;

import com.github.cuter44.nyafx.event.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.easemob.core.*;

public class InjectUserActivateTrigger
    implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent ev)
    {
        UserActivateTrigger evSink = UserActivateTrigger.getInstance();
        EventHub evHub = EventHub.getInstance();

        evHub.subscribe(EVENTTYPE_USER_ACTIVATE, evSink);

        return;
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev)
    {
        return;
    }
}
