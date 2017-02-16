package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Customer;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Черный on 02.02.2017.
 */
public interface CustomerDao {

    void create(Customer customer);

    Customer read(String name);

    Customer read(Long id);

    ArrayList<Customer> readAll();

    void update(Customer customer);

    void delete(Customer customer);

    Customer parseResultSet(ResultSet resultSet);
}
