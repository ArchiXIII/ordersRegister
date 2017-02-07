package com.haulmont.testtask.dao.hsqldb;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.entity.Order;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Черный on 06.02.2017.
 */
public class HsqlOrderDao implements OrderDao {
    @Override
    public void create(Order order) {
        String sql = "insert into orders values (default, ?, ?, sysdate, default, ?, ?);";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, order.getDescription());
            preparedStatement.setString(2, order.getCustomer().getName());
            preparedStatement.setInt(3, order.getPrice());
            preparedStatement.setString(4, order.getState().toString());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Order insert exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    @Override
    public ArrayList<Order> readAll() {
        String sql = "select * from orders;";

        ArrayList<Order> orders = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                orders.add(parseResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.out.println("AllOrders select exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
        return orders;
    }

    @Override
    public void update(Order order) {
        String sql = "update orders set description = ?, customer = ?, end_works_date = ?, price = ?, state = ? where id = ?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, order.getDescription());
            preparedStatement.setString(2, order.getCustomer().getName());
            preparedStatement.setDate(3, (Date) order.getEndWorksDate());
            preparedStatement.setInt(4, order.getPrice());
            preparedStatement.setString(5, order.getState().toString());
            preparedStatement.setLong(6, order.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Order update exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    @Override
    public void delete(Order order) {
        String sql = "delete from orders where id = ?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, order.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Order delete exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    @Override
    public Order parseResultSet(ResultSet resultSet) {
        CustomerDao customerDao = new HsqlCustomerDao();
        Order order = null;
        try {
            order = new Order(resultSet.getString("description"), customerDao.read(resultSet.getString("customer")), resultSet.getInt("price"));
            order.setId(resultSet.getLong("id"));
        } catch (SQLException e) {
            System.out.println("Order parse exception");
            e.printStackTrace();
        }
        return order;
    }
}
