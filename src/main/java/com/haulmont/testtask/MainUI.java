package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlOrderDao;
import com.haulmont.testtask.entity.Customer;
import com.haulmont.testtask.entity.Order;
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
    private ArrayList<Order> orders;
    private String clickedCustomerTableString;
    private String clickedOrderTableString;
    private CustomerDao customerDao = new HsqlCustomerDao();
    private OrderDao orderDao = new HsqlOrderDao();

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setSizeFull();
        mainLayout.setMargin(true);
        TabSheet tabSheet = new TabSheet();
        mainLayout.addComponent(tabSheet);

        initCustomerTab(tabSheet);

        initOrderTab(tabSheet);

        setContent(mainLayout);
    }

    private void initCustomerTab(TabSheet tabSheet){
        VerticalLayout customerLayout = new VerticalLayout();
        customerLayout.setCaption("Клиенты");
        customerLayout.setSizeFull();
        customerLayout.setMargin(true);
        initCustomerTable(customerLayout);
        initCustomerButtons(customerLayout);
        tabSheet.addComponent(customerLayout);
    }

    private void initOrderTab(TabSheet tabSheet){
        VerticalLayout orderLayout = new VerticalLayout();
        orderLayout.setCaption("Заказы");
        orderLayout.setSizeFull();
        orderLayout.setMargin(true);
        initOrderTable(orderLayout);
        initOrderButtons(orderLayout);
        tabSheet.addComponent(orderLayout);
    }

    private void initCustomerTable(VerticalLayout parentLayout){
        Table table = new Table();
        table.setSelectable(true);
        table.addContainerProperty("Имя", String.class, "");
        table.addContainerProperty("Фамилия", String.class, "");
        table.addContainerProperty("Отчество", String.class, "");
        table.addContainerProperty("Телефон", String.class, "");
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSizeFull();

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
                clickedCustomerTableString = itemClickEvent.getItemId().toString();
            }
        });
    }

    private void initCustomerButtons(VerticalLayout parentLayout){
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
                EditCustomerWindow editCustomerUI = new EditCustomerWindow(customers.get(Integer.parseInt(clickedCustomerTableString)));
                UI.getCurrent().addWindow(editCustomerUI);
            }
        });
        horizontalLayout.addComponent(button2);

        Button button3 = new Button("Удалить");
        button3.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                customerDao.delete(customers.get(Integer.parseInt(clickedCustomerTableString)));
                Page.getCurrent().reload();
            }
        });
        horizontalLayout.addComponent(button3);

        parentLayout.addComponent(horizontalLayout);
    }

    private void initOrderTable(VerticalLayout orderLayout) {
        Table table = new Table();
        table.setSelectable(true);
        table.addContainerProperty("Описание", String.class, "");
        table.addContainerProperty("Клиент", String.class, "");
        table.addContainerProperty("Дата создания", String.class, "");
        table.addContainerProperty("Дата завершения", String.class, "");
        table.addContainerProperty("Стоимость", Integer.class, "");
        table.addContainerProperty("Статус", String.class, "");
        table.setColumnCollapsingAllowed(true);
        table.setColumnReorderingAllowed(true);
        table.setSizeFull();

        orderDao = new HsqlOrderDao();
        orders = orderDao.readAll();
        Order order;
        String customerName = "";
        String endWorksDate = "";
        for(int i = 0; i < orders.size(); i++) {
            order = orders.get(i);
            if(order.getCustomer() != null) customerName = order.getCustomer().getName();
            if(order.getEndWorksDate() != null) endWorksDate = order.getEndWorksDate().toString();
            table.addItem(new Object[]{order.getDescription(), customerName, order.getCreatedDate().toString(), endWorksDate, order.getPrice(), order.getState().toString()},i);
        }

        orderLayout.addComponent(table);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                clickedOrderTableString = itemClickEvent.getItemId().toString();
            }
        });
    }

    private void initOrderButtons(VerticalLayout orderLayout) {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button button1 = new Button("Добавить");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditOrderWindow editOrderUI = new EditOrderWindow(null);
                UI.getCurrent().addWindow(editOrderUI);
            }
        });
        horizontalLayout.addComponent(button1);

        Button button2 = new Button("Изменить");
        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditOrderWindow editOrderUI = new EditOrderWindow(orders.get(Integer.parseInt(clickedOrderTableString)));
                UI.getCurrent().addWindow(editOrderUI);
            }
        });
        horizontalLayout.addComponent(button2);

        Button button3 = new Button("Удалить");
        button3.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                orderDao.delete(orders.get(Integer.parseInt(clickedOrderTableString)));
                Page.getCurrent().reload();
            }
        });
        horizontalLayout.addComponent(button3);

        orderLayout.addComponent(horizontalLayout);
    }
}