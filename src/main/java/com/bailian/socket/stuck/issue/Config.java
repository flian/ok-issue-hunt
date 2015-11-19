package com.bailian.socket.stuck.issue;

import java.io.*;
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

    public  static void checkLog(Class clz) throws FileNotFoundException {
        if(isLogRedirect()){
            redirectSysOutErr(clz);
        }
    }
    public static void redirectSysOutErr(Class clz) throws FileNotFoundException {
        String outLog=clz.getName()+"Out.log";
        String errorLog=clz.getName()+"Error.log";
        PrintStream out = new PrintStream(new FileOutputStream(
                outLog));
        PrintStream error = new PrintStream(new FileOutputStream(
                errorLog));
        System.setErr(error);
        System.setOut(out);
    }

    public static final long randomSleep(){
        long maxSleepTime=5*60*1000L;
        return (long)(Math.random()*maxSleepTime);
    }
}
