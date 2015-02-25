package com.github.cuter44.muuga.easemob.servlet;

import javax.servlet.*;

import com.github.cuter44.nyafx.event.*;

import static com.github.cuter44.muuga.Constants.*;
import com.github.cuter44.muuga.conf.*;
import com.github.cuter44.muuga.easemob.core.*;

public class InjectBuddyHateTrigger
    implements ServletContextListener
{
    @Override
    public void contextInitialized(ServletContextEvent ev)
    {
        BuddyHateTrigger evSink = BuddyHateTrigger.getInstance();
        EventHub evHub = EventHub.getInstance();

        evHub.subscribe(EVENTTYPE_BUDDY_HATE, evSink);

        return;
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev)
    {
        return;
    }
}
