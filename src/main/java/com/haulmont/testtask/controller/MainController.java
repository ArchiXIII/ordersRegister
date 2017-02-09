package com.haulmont.testtask.controller;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.DaoFactory;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlDaoFactory;
import com.haulmont.testtask.entity.Customer;

import java.sql.Connection;

/**
 * Created by Черный on 06.02.2017.
 */
public class MainController {
    public static void main(String[] args) throws Exception {
        JdbcUtils.createDB();
        Customer customer = new Customer("name", "surname", "patronymic", "phone");
        CustomerDao customerDao = new HsqlCustomerDao();
        customerDao.create(customer);
    }
}
