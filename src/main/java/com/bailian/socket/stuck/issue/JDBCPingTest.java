package com.bailian.socket.stuck.issue;

import oracle.jdbc.driver.OracleConnection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

/**
 * Created by foy on 2015-11-18.
 */
public class JDBCPingTest {

    public static void main(String[] args) throws FileNotFoundException {
        if (Config.isLogRedirect()) {
            PrintStream out = new PrintStream(new FileOutputStream(
                    Config.jdbcLog()));
            PrintStream error = new PrintStream(new FileOutputStream(
                    Config.jdbcErrorLog()));
            System.setErr(error);
            System.setOut(out);
        }

        int pingThreadCount = 40;
        for (int i = 0; i < pingThreadCount; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String preFix = "[" + Thread.currentThread().getId() + "+" + Thread.currentThread().getName() + "]";
                    while (true) {
                        Connection dbcp = null;
                        Connection druid = null;
                        try {
                            System.out.println(preFix+"start get connection and ping database.");
                            dbcp = JDBCPools.getCon();
                            doPing(dbcp, preFix);
                            druid = JDBCPools.getDruidCon();
                            doPing(dbcp, preFix);
                            System.out.println(preFix + "end get connection and ping database.");
                        } catch (SQLException e) {
                            e.printStackTrace();
                        } finally {
                            if (null != dbcp) {
                                try {
                                    dbcp.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (null != druid) {
                                try {
                                    druid.close();
                                } catch (SQLException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        try {
                            Thread.sleep(Config.randomSleep());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }

    private static void doPing(Connection con, String preFix) throws SQLException {
        long startTime = System.currentTimeMillis();
        con.isValid(16*60*1000);
        long endTime = System.currentTimeMillis();
        long spendTime = (endTime - startTime) / 1000L;
        if (1 * 60 * 1000L <= spendTime) {
            StringBuffer sb = new StringBuffer();
            sb.append("#####################ERROR#####################").append("\n");
            sb.append("start dateTime:" + new Date(startTime)).append("\n");
            sb.append("end dateTime:" + new Date(endTime)).append("\n");
            sb.append(preFix + "####ERROR####本次共执行超过一分钟，看起来卡了。" + spendTime).append("\n");
            sb.append("###############################################").append("\n");
            System.err.println(sb.toString());
        }
    }
}
