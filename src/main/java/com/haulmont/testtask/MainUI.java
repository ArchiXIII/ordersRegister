package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.entity.Customer;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    private ArrayList<Customer> customers;
    private String clickedTableString;
    private CustomerDao customerDao = new HsqlCustomerDao();

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        TabSheet tabsheet = new TabSheet();
        mainLayout.addComponent(tabsheet);

        VerticalLayout customerTab = new VerticalLayout();
        customerTab.setCaption("Клиенты");
        customerTab.setSizeFull();
        customerTab.setMargin(true);
        initTable(customerTab);
        initButtons(customerTab);
        tabsheet.addComponent(customerTab);

        VerticalLayout orderTab = new VerticalLayout();
        orderTab.setCaption("Заказы");

        tabsheet.addComponent(orderTab);


        setContent(mainLayout);
    }

    private void initTable(VerticalLayout parentLayout){
        Table table = new Table("Клиенты");
        table.setSelectable(true);
        table.addContainerProperty("Имя", String.class, "");
        table.addContainerProperty("Фамилия", String.class, "");
        table.addContainerProperty("Отчество", String.class, "");
        table.addContainerProperty("Телефон", String.class, "");
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSizeFull();

        CustomerDao customerDao = new HsqlCustomerDao();
        customers = customerDao.readAll();
        Customer cust;
        for(int i = 0; i < customers.size(); i++) {
            cust = customers.get(i);
            table.addItem(new Object[]{cust.getName(), cust.getSurname(), cust.getPatronymic(), cust.getPhone()},i);
        }

        parentLayout.addComponent(table);

        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                clickedTableString = itemClickEvent.getItemId().toString();
            }
        });
    }

    private void initButtons(VerticalLayout parentLayout){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button button1 = new Button("Добавить");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditCustomerWindow editCustomerUI = new EditCustomerWindow(null);
                UI.getCurrent().addWindow(editCustomerUI);
            }
        });
        horizontalLayout.addComponent(button1);

        Button button2 = new Button("Изменить");
        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditCustomerWindow editCustomerUI = new EditCustomerWindow(customers.get(Integer.parseInt(clickedTableString)));
                UI.getCurrent().addWindow(editCustomerUI);
            }
        });
        horizontalLayout.addComponent(button2);

        Button button3 = new Button("Удалить");
        button3.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                customerDao.delete(customers.get(Integer.parseInt(clickedTableString)));
                Page.getCurrent().reload();
            }
        });
        horizontalLayout.addComponent(button3);

        parentLayout.addComponent(horizontalLayout);
    }
}