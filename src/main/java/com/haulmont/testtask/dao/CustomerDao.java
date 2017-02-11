package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Customer;

import java.sql.ResultSet;

/**
 * Created by Черный on 02.02.2017.
 */
public interface CustomerDao {

    void create(Customer customer);

    Customer read(String login);

    Customer read(Long id);

    void update(Customer customer);

    void delete(Customer customer);

    Customer parseResultSet(ResultSet resultSet);
}
