package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlOrderDao;
import com.haulmont.testtask.entity.Order;
import com.haulmont.testtask.entity.State;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

import java.util.Date;

/**
 * Created by Черный on 16.02.2017.
 */
public class EditOrderWindow extends Window {
    private Order order;
    private TextArea descriptionArea;
    private TextField customerField;
    private TextField endWorksDateField;
    private TextField priceField;
    private TextField stateField;

    public EditOrderWindow(Order order){
        super("Редактирование заказа");
        center();

        this.order = order;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        initTextFields(layout);
        initButtons(layout);
        setContent(layout);
    }

    private void initTextFields(VerticalLayout layout) {

    }

    private void initButtons(VerticalLayout layout){

    }

    private void editOrder(){
        CustomerDao customerDao = new HsqlCustomerDao();

        order.setDescription(descriptionArea.getValue());
        order.setCustomer(customerDao.read(customerField.getValue()));
        order.setEndWorksDate(new Date(endWorksDateField.getValue()));
        order.setPrice(Integer.parseInt(priceField.getValue()));
        order.setState(State.valueOf(stateField.getValue()));
        OrderDao orderDao = new HsqlOrderDao();
        orderDao.update(order);
    }

    private  void createOrder(){
        CustomerDao customerDao = new HsqlCustomerDao();
        order = new Order(descriptionArea.getValue(), customerDao.read(customerField.getValue()), Integer.parseInt(priceField.getValue()));
        OrderDao orderDao = new HsqlOrderDao();
        orderDao.create(order);
    }
}
