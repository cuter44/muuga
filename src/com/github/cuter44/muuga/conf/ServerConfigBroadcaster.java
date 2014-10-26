package com.github.cuter44.muuga.conf;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Properties;

import com.alibaba.fastjson.*;

@WebServlet("/config/list.api")
public class ServerConfigBroadcaster extends HttpServlet
{
    Configurator conf = Configurator.getInstance();

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        this.doPost(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        Properties prop = this.conf.getPublicProperties();

        JSONObject j = new JSONObject();
        for (String key:prop.stringPropertyNames())
            j.put(key, prop.get(key));

        out.println(
           j.toString()
        );

        return;
    }
}
