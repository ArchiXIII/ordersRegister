package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.entity.Customer;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Validator;
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by admin on 12.02.2017.
 */
@Theme(ValoTheme.THEME_NAME)
public class EditCustomerWindow extends Window {
    private Customer customer;
    private TextField nameField;
    private TextField surnameField;
    private TextField patronymicField;
    private TextField phoneField;

    public EditCustomerWindow(Customer customer) {
        super("Редактор клиента");
        center();

        this.customer = customer;

        VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);

        initTextFields(layout);
        initButtons(layout);
        setContent(layout);
    }

    private void initTextFields(VerticalLayout parentLayout){
        nameField = new TextField("Имя:");
        nameField.addValidator(new StringLengthValidator(
                "Имя должно быть длинной 2-30 символов",
                2, 30, false));
        nameField.setImmediate(true);
        nameField.setNullRepresentation("");
        nameField.setNullSettingAllowed(true);

        surnameField = new TextField("Фамилия:");
        surnameField.addValidator(new StringLengthValidator(
                "Фамилия должна быть длинной 2-30 символов",
                2, 30, false));
        surnameField.setImmediate(true);
        surnameField.setNullRepresentation("");
        surnameField.setNullSettingAllowed(true);

        patronymicField = new TextField("Отчество:");
        patronymicField.addValidator(new StringLengthValidator(
                "Отчество должно быть длинной 2-30 символов",
                2, 30, false));
        patronymicField.setImmediate(true);
        patronymicField.setNullRepresentation("");
        patronymicField.setNullSettingAllowed(true);

        phoneField = new TextField("Телефон:");
        phoneField.addValidator(new StringLengthValidator(
                "Телефон должен состоять из цифр и быть длинной 2-11 символов",
                2, 11, false));
        phoneField.setImmediate(true);
        phoneField.setNullRepresentation("");
        phoneField.setNullSettingAllowed(true);

        if(customer != null){
            nameField.setValue(customer.getName());
            surnameField.setValue(customer.getSurname());
            patronymicField.setValue(customer.getPatronymic());
            phoneField.setValue(customer.getPhone());
        }

        parentLayout.addComponent(nameField);
        parentLayout.addComponent(surnameField);
        parentLayout.addComponent(patronymicField);
        parentLayout.addComponent(phoneField);
    }

    private void initButtons(VerticalLayout parentLayout){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button button1 = new Button("ОК");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                nameField.setValidationVisible(false);
                try {
                    nameField.validate();
                    surnameField.validate();
                    patronymicField.validate();
                    phoneField.validate();
                    Long.parseLong(phoneField.getValue());

                    if(customer != null){
                        editCustomer();
                    }else createCustomer();

                    close();
                } catch (Validator.InvalidValueException e) {
                    Notification.show(e.getMessage());
                    nameField.setValidationVisible(true);
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

    private void editCustomer(){
        customer.setName(nameField.getValue());
        customer.setSurname(surnameField.getValue());
        customer.setPatronymic(patronymicField.getValue());
        customer.setPhone(phoneField.getValue());
        CustomerDao customerDao = new HsqlCustomerDao();
        customerDao.update(customer);
    }

    private  void createCustomer(){
        customer = new Customer(nameField.getValue(), surnameField.getValue(), patronymicField.getValue(), phoneField.getValue());
        CustomerDao customerDao = new HsqlCustomerDao();
        customerDao.create(customer);
    }
}
