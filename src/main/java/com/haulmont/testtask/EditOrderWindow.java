package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlOrderDao;
import com.haulmont.testtask.entity.Customer;
import com.haulmont.testtask.entity.Order;
import com.haulmont.testtask.entity.State;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by Черный on 16.02.2017.
 */
public class EditOrderWindow extends Window {
    private Order order;
    private TextArea descriptionArea;
    private TextField customerField;
    private TextField priceField;
    private ComboBox stateBox;

    public EditOrderWindow(Order order){
        super("Редактор заказа");
        center();

        this.order = order;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        initTextFields(layout);
        initButtons(layout);
        setContent(layout);
    }

    private void initTextFields(VerticalLayout parentLayout) {
        descriptionArea = new TextArea("Описание:");
        descriptionArea.addValidator(new StringLengthValidator(
                "Описание должно быть длинной 3-50 символов",
                3, 50, false));
        descriptionArea.setImmediate(true);
        descriptionArea.setNullRepresentation("");
        descriptionArea.setNullSettingAllowed(true);
        parentLayout.addComponent(descriptionArea);

        customerField = new TextField("Имя клиента:");
        customerField.addValidator(new StringLengthValidator(
                "Должно быть именем клиента зарегистрированного в системе",
                2, 30, false));
        customerField.setImmediate(true);
        customerField.setNullRepresentation("");
        customerField.setNullSettingAllowed(true);
        parentLayout.addComponent(customerField);

        priceField = new TextField("Стоимость:");
        priceField.addValidator(new StringLengthValidator(
                "Стоимость должна быть числом длинной 1-9 символов",
                1, 9, false));
        priceField.setImmediate(true);
        priceField.setNullRepresentation("");
        priceField.setNullSettingAllowed(true);
        parentLayout.addComponent(priceField);

        if(order != null){
            List<State> states = Arrays.asList(
                    State.PLANNED,
                    State.COMPLETE,
                    State.ADOPTED
            );
            stateBox = new ComboBox("Статус:", states);
            stateBox.setNullSelectionAllowed(false);
            parentLayout.addComponent(stateBox);

            descriptionArea.setValue(order.getDescription());
            customerField.setValue(order.getCustomer().getName());
            priceField.setValue(order.getPrice().toString());
            stateBox.setValue(order.getState());
        }

    }

    private void initButtons(VerticalLayout parentLayout){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button button1 = new Button("ОК");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                descriptionArea.setValidationVisible(false);
                try {
                    descriptionArea.validate();
                    customerField.validate();
                    priceField.validate();
                    Integer.parseInt(priceField.getValue());

                    if(order != null){
                        editOrder();
                    }else createOrder();
                } catch (Validator.InvalidValueException e) {
                    Notification.show(e.getMessage());
                    descriptionArea.setValidationVisible(true);
                }
            }
        });
        horizontalLayout.addComponent(button1);

        Button button2 = new Button("Отменить");
        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                close();
            }
        });
        horizontalLayout.addComponent(button2);

        parentLayout.addComponent(horizontalLayout);
    }

    private void editOrder(){
        CustomerDao customerDao = new HsqlCustomerDao();
        Customer customer = customerDao.read(customerField.getValue());
        if (customer != null) {
            order.setDescription(descriptionArea.getValue());
            order.setCustomer(customer);
            order.setEndWorksDate(new Date());
            if(stateBox.getValue().equals(State.COMPLETE)) order.setEndWorksDate(new Date());
            order.setPrice(Integer.parseInt(priceField.getValue()));
            order.setState((State) stateBox.getValue());
            OrderDao orderDao = new HsqlOrderDao();
            orderDao.update(order);
            close();
        }
    }

    private  void createOrder(){
        CustomerDao customerDao = new HsqlCustomerDao();
        Customer customer = customerDao.read(customerField.getValue());
        if (customer != null) {
            order = new Order(descriptionArea.getValue(), customer, Integer.parseInt(priceField.getValue()));
            OrderDao orderDao = new HsqlOrderDao();
            orderDao.create(order);
            close();
        }
    }
}
