package com.github.cuter44.muuga.easemob.servlet;

import javax.servlet.*;

import com.github.cuter44.nyafx.event.*;

import com.github.cuter44.muuga.Constants;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.easemob.core.*;

public class InjectLoginTrigger
    implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent ev)
    {
        UserLoginTrigger evSink = UserLoginTrigger.getInstance();
        EventHub evHub = EventHub.getInstance();

        evHub.subscribe(Constants.EVENTTYPE_USER_LOGIN, evSink);

        return;
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev)
    {
        return;
    }
}
