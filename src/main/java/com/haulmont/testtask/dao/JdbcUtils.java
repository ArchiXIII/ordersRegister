package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.hsqldb.HsqlDaoFactory;
import com.vaadin.server.VaadinService;
import org.hsqldb.cmdline.SqlFile;

import java.io.*;
import java.sql.*;

/**
 * Created by Черный on 06.02.2017.
 */
public class JdbcUtils {
    public static void closeQuietly(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.out.println("Cannot close result set");
            }
        }
    }

    public static void closeQuietly(Statement ps) {
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.out.println("Cannot close statement");
            }
        }
    }

    public static void closeQuietly(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Cannot close connection");
            }
        }
    }

    public static void createDB() {
        String basePath = VaadinService.getCurrent()
                .getBaseDirectory().getAbsolutePath();
        Connection connection = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            SqlFile userDbSetup = new SqlFile( new File(basePath + "/WEB-INF/createDB.sql"));
            userDbSetup.setConnection(connection);
            userDbSetup.setContinueOnError(true);
            userDbSetup.execute();
        } catch (Exception e) {
            System.out.println("Create DB exception");
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(connection);
        }
    }
}
