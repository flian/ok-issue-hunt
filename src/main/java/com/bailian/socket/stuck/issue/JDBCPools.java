package com.bailian.socket.stuck.issue;

import com.alibaba.druid.pool.DruidDataSource;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by foy on 2015-11-18.
 */
public class JDBCPools {

    private static final String DBCP_KEY = "dbcp";
    private static final String DRUID_KEY = "druid";

    private static Map<String, DataSource> ds = new HashMap<String, DataSource>(3);

    static {
        BasicDataSource dbcpDS = new BasicDataSource();
        dbcpDS.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        dbcpDS.setUrl("jdbc:oracle:thin:@192.168.140.203:1521:okstage");
        dbcpDS.setUsername("ok_wallet");
        dbcpDS.setPassword("ok_wallet");
        dbcpDS.setMaxTotal(500);
        DruidDataSource druidDS = new DruidDataSource();
        druidDS.setDriverClassName("oracle.jdbc.driver.OracleDriver");
        druidDS.setUrl("jdbc:oracle:thin:@192.168.140.203:1521:okstage");
        druidDS.setUsername("ok_login");
        druidDS.setPassword("ok_login");
        druidDS.setMaxActive(500);

        ds.put(DBCP_KEY, dbcpDS);

        ds.put(DRUID_KEY, druidDS);
    }

    public static DataSource  get(String key) {
        return ds.get(key);
    }

    public static DataSource get() {
        return ds.get(DBCP_KEY);
    }

    public static Connection getCon() throws SQLException {
        return getCon(DBCP_KEY);
    }

    public static Connection getDruidCon() throws SQLException {
        return getCon(DRUID_KEY);
    }

    public static Connection getCon(String dataSource) throws SQLException {
        return get(dataSource).getConnection();
    }
}
