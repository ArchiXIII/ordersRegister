package com.haulmont.testtask.dao;

import com.haulmont.testtask.dao.hsqldb.HsqlDaoFactory;

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
        String sql = "";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.execute();
        } catch (Exception e) {
            System.out.println("Create DB exception");
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }
}
