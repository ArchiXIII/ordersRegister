package com.haulmont.testtask.dao.hsqldb;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.entity.Order;
import com.haulmont.testtask.entity.State;

import java.sql.*;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Черный on 06.02.2017.
 */
public class HsqlOrderDao implements OrderDao {
    @Override
    public void create(Order order) {
        String sql = "insert into orders values (default, ?, ?, ?, default, ?, ?);";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, order.getDescription());
            preparedStatement.setLong(2, order.getCustomer().getId());
            preparedStatement.setDate(3, new Date(order.getCreatedDate().getTime()));
            preparedStatement.setInt(4, order.getPrice());
            preparedStatement.setString(5, order.getState().toString());
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

        ArrayList<Order> orders = new ArrayList<Order>();
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
        String sql = "update orders set description = ?, customer_id = ?, end_works_date = ?, price = ?, state_order = ? where order_id = ?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, order.getDescription());
            preparedStatement.setLong(2, order.getCustomer().getId());
            preparedStatement.setDate(3, new Date(order.getEndWorksDate().getTime()));
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
        String sql = "delete from orders where order_id = ?;";

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
            order = new Order(resultSet.getString("description"), customerDao.read(resultSet.getLong("customer_id")), resultSet.getInt("price"));
            order.setId(resultSet.getLong("order_id"));
            order.setCreatedDate(resultSet.getDate("created_date"));
            order.setEndWorksDate(resultSet.getDate("end_works_date"));
            order.setState(State.valueOf(resultSet.getString("state_order")));
        } catch (SQLException e) {
            System.out.println("Order parse exception");
            e.printStackTrace();
        }
        return order;
    }
}
