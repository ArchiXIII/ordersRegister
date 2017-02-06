package com.haulmont.testtask.dao.hsqldb;

import com.haulmont.testtask.dao.DaoFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Черный on 06.02.2017.
 */
public class HsqlDaoFactory implements DaoFactory {
    private static DaoFactory daoFactory;
    private String user = "SA";
    private String password = "";
    private String url = "jdbc:hsqldb:file:base/ordersRegisterDB";
    private String driver = "org.hsqldb.jdbc.JDBCDriver";


    private HsqlDaoFactory() throws Exception {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC driver exception");
            e.printStackTrace();
        }
    }

    public static DaoFactory getInstance(){
        if (null == daoFactory) {
            try {
                daoFactory = new HsqlDaoFactory();
            } catch (Exception e) {
                System.out.println("Don't created DaoFactory");
                e.printStackTrace();
            }
        }
        return daoFactory;
    }

    public Connection getConnection() throws Exception {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println("Get connection exception");
            e.printStackTrace();
        }
        return connection;
    }
}
