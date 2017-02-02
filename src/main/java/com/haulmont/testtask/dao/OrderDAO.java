package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Order;

/**
 * Created by Черный on 02.02.2017.
 */
public interface OrderDAO {

    void create(Order order);

    Order readAll();

    void update(Order order);

    void delete(Order order);
}
