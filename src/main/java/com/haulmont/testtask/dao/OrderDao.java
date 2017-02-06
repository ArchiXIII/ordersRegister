package com.haulmont.testtask.dao;

import com.haulmont.testtask.entity.Order;

import java.sql.ResultSet;
import java.util.ArrayList;

/**
 * Created by Черный on 02.02.2017.
 */
public interface OrderDao {

    void create(Order order);

    ArrayList<Order> readAll();

    void update(Order order);

    void delete(Order order);

    Order parseResultSet(ResultSet resultSet);
}
