package com.haulmont.testtask.controller;

import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.dao.hsqldb.HsqlDaoFactory;

import java.sql.Connection;

/**
 * Created by Черный on 06.02.2017.
 */
public class MainController {
    public static void main(String[] args) throws Exception {
        JdbcUtils.createDB();
    }
}
