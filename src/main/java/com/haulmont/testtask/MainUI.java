package com.haulmont.testtask;

import com.haulmont.testtask.dao.CustomerDao;
import com.haulmont.testtask.dao.JdbcUtils;
import com.haulmont.testtask.dao.OrderDao;
import com.haulmont.testtask.dao.hsqldb.HsqlCustomerDao;
import com.haulmont.testtask.dao.hsqldb.HsqlOrderDao;
import com.haulmont.testtask.entity.Customer;
import com.haulmont.testtask.entity.Order;
import com.vaadin.annotations.Theme;
import com.vaadin.event.ItemClickEvent;
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
    private VerticalLayout customerLayout;
    private VerticalLayout orderLayout;

    @Override
    protected void init(VaadinRequest request) {
        JdbcUtils.createDB();

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
        customerLayout = new VerticalLayout();
        customerLayout.setCaption("Клиенты");
        customerLayout.setSizeFull();
        customerLayout.setMargin(true);
        initCustomerTable();
        initCustomerButtons();
        tabSheet.addComponent(customerLayout);
    }

    private void initOrderTab(TabSheet tabSheet){
        orderLayout = new VerticalLayout();
        orderLayout.setCaption("Заказы");
        orderLayout.setSizeFull();
        orderLayout.setMargin(true);
        initOrderTable();
        initOrderButtons();
        tabSheet.addComponent(orderLayout);
    }

    private void initCustomerTable(){
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

        customerLayout.addComponent(table);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                clickedCustomerTableString = itemClickEvent.getItemId().toString();
            }
        });
    }

    private void reloadCustomerTable(){
        customerLayout.removeAllComponents();
        initCustomerTable();
        initCustomerButtons();
    }

    private void initCustomerButtons(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button button1 = new Button("Добавить");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditCustomerWindow editCustomerUI = new EditCustomerWindow(null);
                editCustomerUI.setModal(true);
                editCustomerUI.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent closeEvent) {
                        reloadCustomerTable();
                    }
                });
                UI.getCurrent().addWindow(editCustomerUI);
            }
        });
        horizontalLayout.addComponent(button1);

        Button button2 = new Button("Изменить");
        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditCustomerWindow editCustomerUI = new EditCustomerWindow(customers.get(Integer.parseInt(clickedCustomerTableString)));
                editCustomerUI.setModal(true);
                editCustomerUI.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent closeEvent) {
                        reloadCustomerTable();
                    }
                });
                UI.getCurrent().addWindow(editCustomerUI);
            }
        });
        horizontalLayout.addComponent(button2);

        Button button3 = new Button("Удалить");
        button3.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                customerDao.delete(customers.get(Integer.parseInt(clickedCustomerTableString)));
                reloadCustomerTable();
            }
        });
        horizontalLayout.addComponent(button3);
        horizontalLayout.addComponent(new Label("*Вы не можете удалить клиента для которого есть заказы."));

        customerLayout.addComponent(horizontalLayout);
    }

    private void initOrderTable() {
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
        String endWorksDate;
        for(int i = 0; i < orders.size(); i++) {
            order = orders.get(i);
            if(order.getCustomer() != null) customerName = order.getCustomer().getName();
            if(order.getEndWorksDate() != null) {
                endWorksDate = order.getEndWorksDate().toString();
            }else endWorksDate = "";
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

    private void reloadOrderTable(){
        orderLayout.removeAllComponents();
        initOrderTable();
        initOrderButtons();
    }

    private void initOrderButtons() {
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        Button button1 = new Button("Добавить");
        button1.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditOrderWindow editOrderUI = new EditOrderWindow(null);
                editOrderUI.setModal(true);
                editOrderUI.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent closeEvent) {
                        reloadOrderTable();
                    }
                });
                UI.getCurrent().addWindow(editOrderUI);
            }
        });
        horizontalLayout.addComponent(button1);

        Button button2 = new Button("Изменить");
        button2.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                EditOrderWindow editOrderUI = new EditOrderWindow(orders.get(Integer.parseInt(clickedOrderTableString)));
                editOrderUI.setModal(true);
                editOrderUI.addCloseListener(new Window.CloseListener() {
                    @Override
                    public void windowClose(Window.CloseEvent closeEvent) {
                        reloadOrderTable();
                    }
                });
                UI.getCurrent().addWindow(editOrderUI);
            }
        });
        horizontalLayout.addComponent(button2);

        Button button3 = new Button("Удалить");
        button3.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                orderDao.delete(orders.get(Integer.parseInt(clickedOrderTableString)));
                reloadOrderTable();
            }
        });
        horizontalLayout.addComponent(button3);

        orderLayout.addComponent(horizontalLayout);
    }
}