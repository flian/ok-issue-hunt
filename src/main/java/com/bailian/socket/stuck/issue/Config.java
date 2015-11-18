package com.bailian.socket.stuck.issue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by foy on 2015-11-17.
 */
public class Config {
    public static Properties config = null;

    static {
        try {
            InputStream in = Config.class.getResourceAsStream("/config.properties");
            config = new Properties();
            config.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isLogRedirect() {
        return "TRUE".equalsIgnoreCase(config.getProperty("redirect.system.out.to.file")) ? true : false;
    }

    public static String ip() {
        return config.getProperty("server.socket.listen.ip");
    }

    public static int port() {
        return Integer.parseInt(config.getProperty("server.socket.listen.port"));
    }

    public static String sLog() {
        return config.getProperty("server.system.out");
    }

    public static String cLog() {
        return config.getProperty("client.system.out");
    }

    public static String cErrorLog() {
        return config.getProperty("client.system.error");
    }

    public static String jdbcLog() {
        return config.getProperty("jdbc.system.out");
    }

    public static String jdbcErrorLog() {
        return config.getProperty("jdbc.system.error");
    }

    public static final long randomSleep(){
        long maxSleepTime=5*60*1000L;
        return (long)(Math.random()*maxSleepTime);
    }
}
