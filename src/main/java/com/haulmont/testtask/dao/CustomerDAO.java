package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Customer;

/**
 * Created by Черный on 02.02.2017.
 */
public interface CustomerDAO {

    void create(Customer customer);

    Customer read(String login);

    void update(Customer customer);

    void delete(Customer customer);
}
