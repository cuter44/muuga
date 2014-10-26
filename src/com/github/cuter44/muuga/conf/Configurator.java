package com.github.cuter44.muuga.conf;

import java.util.Properties;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

/**
 */
public class Configurator
{
  // CONSTRUCT
    private Properties publicProp;
    private Properties prop;

    private Configurator()
    {
        this.load();
    }

  // SINGLETON
    private static class Singleton
    {
        public static final Configurator instance = new Configurator();
    }

    public static Configurator getInstance()
    {
        return(Singleton.instance);
    }

  //
    /** 加载配置文件
     */
    private void load()
    {
        try
        {
            InputStreamReader is;

            is = new InputStreamReader(
                this.getClass()
                    .getResourceAsStream("/muuga.public.properties"),
                "utf-8"
            );

            this.publicProp = new Properties();
            this.publicProp.load(is);
            is.close();

            is = new InputStreamReader(
                this.getClass()
                    .getResourceAsStream("/muuga.properties"),
                "utf-8"
            );

            this.prop = new Properties(this.publicProp);
            this.prop.load(is);
            is.close();
        }
        catch (Exception ex)
        {
            throw(new RuntimeException(ex.getMessage(), ex));
        }
    }

    /** 获得整个属性库的只读副本
     * 本质上是将原有 prop 作为默认表创建新表并返回.
     * 因此, 对得到的表作出的修改不会影响到原表.
     */
    public Properties getProperties()
    {
        Properties prop = new Properties(this.prop);

        return(prop);
    }

    /** 获得公开属性库的只读副本
     * 本质上是将原有 prop 作为默认表创建新表并返回.
     * 因此, 对得到的表作出的修改不会影响到原表.
     * 主要用于将服务器端设置值告诉客户端.
     */
    public Properties getPublicProperties()
    {
        Properties prop = new Properties(this.publicProp);

        return(prop);
    }


    /**
     * 提取配置文件中的参数
     */
    public String get(String name)
    {
        return(
            this.prop.getProperty(name)
        );
    }

    public String get(String name, String defaultValue)
    {
        String v = this.prop.getProperty(name);

        return(
            v!=null ? v : null
        );
    }


    public Integer getInt(String name)
    {
        try
        {
            String v = this.get(name);
            return(
                v!=null ? Integer.valueOf(v) : null
            );
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return(null);
        }
    }

    public Integer getInt(String name, Integer defaultValue)
    {
        Integer v = this.getInt(name);
        return(
            v!=null ? v : defaultValue
        );
    }

    public Long getLong(String name)
    {
        try
        {
            String v = this.get(name);
            return(
                v!=null ? Long.valueOf(v) : null
            );
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return(null);
        }
    }

    public Long getLong(String name, Long defaultValue)
    {
        Long v = this.getLong(name);
        return(
            v!=null ? v : defaultValue
        );
    }

    public Double getDouble(String name)
    {
        try
        {
            String v = this.get(name);
            return(
                v!=null ? Double.valueOf(v) : null
            );
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return(null);
        }
    }

    public Double getDouble(String name, Double defaultValue)
    {
        Double v = this.getDouble(name);
        return(
            v!=null ? v : defaultValue
        );
    }
}
