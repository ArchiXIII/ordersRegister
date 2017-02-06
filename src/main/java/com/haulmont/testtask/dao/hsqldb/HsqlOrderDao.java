package com.haulmont.testtask.dao.hsqldb;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Черный on 06.02.2017.
 */
public class HsqlOrderDao implements OrderDao {
    @Override
    public void create(Order order) {

    }

    @Override
    public Order readAll() {
        return null;
    }

    @Override
    public void update(Order order) {

    }

    @Override
    public void delete(Order order) {

    }

    @Override
    public Order parseResultSet(ResultSet resultSet) {
        CustomerDao customerDao = new HsqlCustomerDao();
        Order order = null;
        try {
            order = new Order(resultSet.getString("description"), customerDao.read(resultSet.getString("customer")), resultSet.getInt("price"));
            order.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            System.out.println("Order parse exeption");
            e.printStackTrace();
        }
        return order;
    }
}
