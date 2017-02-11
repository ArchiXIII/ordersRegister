package com.haulmont.testtask.controller;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlOrderDao;
import com.haulmont.testtask.entity.Customer;
import com.haulmont.testtask.entity.Order;
import com.haulmont.testtask.entity.State;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Черный on 06.02.2017.
 */
public class MainController {
    public static void main(String[] args) throws Exception {
        JdbcUtils.createDB();
        Customer customer = new Customer("name", "surname", "patronymic", "phone");
        CustomerDao customerDao = new HsqlCustomerDao();
        customerDao.create(customer);
        customer = customerDao.read("name");
        customer.setName("name2");
        customerDao.update(customer);
        System.out.println(customer.toString());

        Order order = new Order("description", customer, 1000);
        OrderDao orderDao = new HsqlOrderDao();
        orderDao.create(order);
        ArrayList<Order> orders = orderDao.readAll();
        for(int i = 0; i < orders.size(); i++) {
            order = orders.get(i);
            order.setEndWorksDate(new Date());
            order.setState(State.COMPLETE);
            orderDao.update(order);
            orderDao.delete(order);
        }
        System.out.println(order.toString());

        customerDao.delete(customer);
    }
}
