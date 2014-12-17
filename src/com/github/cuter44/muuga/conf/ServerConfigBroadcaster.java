package com.github.cuter44.muuga.conf;

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.util.Properties;

import com.alibaba.fastjson.*;

/** 列出所有服务器端参数
 * 包含的值参见 src/muuga.public.properties (随着开发进度会有所不同)
 * <pre style="font-size:12px">

   <strong>请求</strong>
   GET/POST /config/list.api

   <strong>参数</strong>
   <i>无参数</i>

   <strong>响应</strong>
   application/json

   <strong>例外</strong>
   parsed by {@link com.github.cuter44.muuga.sys.servlet.ExceptionHandler ExceptionHandler}

   <strong>样例</strong>
    GET http://localhost:8080/muuga/config/list.api HTTP/1.1
    User-Agent: Fiddler
    Host: localhost:8080


    HTTP/1.1 200 OK
    Server: Apache-Coyote/1.1
    Content-Type: application/json;charset=UTF-8
    Content-Length: 547
    Date: Tue, 16 Dec 2014 16:44:32 GMT

    {"librarica.douban.client_id":"08287f0ae3a20dfd18797d20aae629f7",...}


 * </pre>
 */
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
            j.put(key, prop.getProperty(key));

        out.println(
           j.toString()
        );

        return;
    }
}
