package com.haulmont.testtask.dao.hsqldb;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.entity.Customer;

import java.sql.*;

/**
 * Created by Черный on 06.02.2017.
 */
public class HsqlCustomerDao implements CustomerDao {
    @Override
    public void create(Customer customer) {
        String sql = "insert into customers values (default, ?, ?, ?, ?);";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            preparedStatement.setString(3, customer.getPatronymic());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Customer insert exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    @Override
    public Customer read(String name) {
        String sql = "select * from customers where name = ?;";

        Customer customer = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customer = parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Customer read exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
        return customer;
    }

    @Override
    public Customer read(Long id) {
        String sql = "select * from customers where customer_id = ?;";

        Customer customer = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                customer = parseResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Customer select exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(resultSet);
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
        return customer;
    }

    @Override
    public void update(Customer customer) {
        String sql = "update customers set name = ?, surname = ?, patronymic = ?, phone = ? where customer_id = ?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            preparedStatement.setString(3, customer.getPatronymic());
            preparedStatement.setString(4, customer.getPhone());
            preparedStatement.setLong(5, customer.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Customer update exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    @Override
    public void delete(Customer customer) {
        String sql = "delete from customers where customer_id = ?;";

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = HsqlDaoFactory.getInstance().getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, customer.getId());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Customer delete exception");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JdbcUtils.closeQuietly(preparedStatement);
            JdbcUtils.closeQuietly(connection);
        }
    }

    @Override
    public Customer parseResultSet(ResultSet resultSet) {
        Customer customer = null;
        try {
            customer = new Customer(resultSet.getString("name"), resultSet.getString("surname"), resultSet.getString("patronymic"), resultSet.getString("phone"));
            customer.setId(resultSet.getLong("customer_id"));
        } catch (SQLException e) {
            System.out.println("Customer parse exeption");
            e.printStackTrace();
        }
        return customer;
    }
}
