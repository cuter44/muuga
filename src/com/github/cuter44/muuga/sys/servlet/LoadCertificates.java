package com.github.cuter44.muuga.sys.servlet;

import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;

import com.github.cuter44.nyafx.ssl.*;

import com.github.cuter44.muuga.conf.*;

/** 证书加载器
 * 加载证书并正确设置 apache-hc
 */
public class LoadCertificates implements ServletContextListener
{
    @Override
    public void contextDestroyed(ServletContextEvent sce)
    {
        return;
    }

    @Override
    public void contextInitialized(ServletContextEvent sce)
    {
        try
        {
            Configurator conf = Configurator.getInstance();
            String confValue = conf.get("nyafx.ssl.certificates");
            if (confValue == null)
                return;

            String[] certs = confValue.split(";");

            new CertificateLoader()
                .loadX509CertResource(certs)
                .overrideDefaultSSLContext("TLSv1");

            return;
        }
        catch (Exception ex)
        {
            sce.getServletContext().log("Error loading certificates", ex);
        }
    }

}
