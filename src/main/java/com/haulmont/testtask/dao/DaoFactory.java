package com.haulmont.testtask.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Черный on 06.02.2017.
 */
public interface DaoFactory {
    static DaoFactory getInstance(){return null;}

    Connection getConnection() throws Exception;
}
