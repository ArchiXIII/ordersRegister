package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlOrderDao;
import com.haulmont.testtask.entity.Order;
import com.haulmont.testtask.entity.State;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.Page;
import com.vaadin.ui.*;

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
                "The name must be 1-30 letters",
                1, 30, false));
        descriptionArea.setImmediate(true);
        descriptionArea.setNullRepresentation("");
        descriptionArea.setNullSettingAllowed(true);

        customerField = new TextField("Имя:");
        customerField.addValidator(new StringLengthValidator(
                "The surname must be 1-30 letters",
                1, 30, false));
        customerField.setImmediate(true);
        customerField.setNullRepresentation("");
        customerField.setNullSettingAllowed(true);

        endWorksDateField = new TextField("Дата завершения заказа:");
        endWorksDateField.addValidator(new StringLengthValidator(
                "The patronymic must be 1-30 letters",
                1, 30, false));
        endWorksDateField.setImmediate(true);
        endWorksDateField.setNullRepresentation("");
        endWorksDateField.setNullSettingAllowed(true);

        priceField = new TextField("Стоимость:");
        priceField.addValidator(new StringLengthValidator(
                "The phone must be 1-20 letters",
                1, 20, false));
        priceField.setImmediate(true);
        priceField.setNullRepresentation("");
        priceField.setNullSettingAllowed(true);

        stateField = new TextField("Статус:");
        stateField.addValidator(new StringLengthValidator(
                "The phone must be 1-20 letters",
                1, 20, false));
        stateField.setImmediate(true);
        stateField.setNullRepresentation("");
        stateField.setNullSettingAllowed(true);

        if(order != null){
            descriptionArea.setValue(order.getDescription());
            customerField.setValue(order.getCustomer().getName());
            if(order.getEndWorksDate() != null) endWorksDateField.setValue(order.getEndWorksDate().toString());
            priceField.setValue(order.getPrice().toString());
            stateField.setValue(order.getState().toString());
        }

        parentLayout.addComponent(descriptionArea);
        parentLayout.addComponent(customerField);
        parentLayout.addComponent(endWorksDateField);
        parentLayout.addComponent(priceField);
        parentLayout.addComponent(stateField);
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
                    endWorksDateField.validate();
                    priceField.validate();
                    stateField.validate();

                    if(order != null){
                        editOrder();
                    }else createOrder();

                    Page.getCurrent().reload();
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
