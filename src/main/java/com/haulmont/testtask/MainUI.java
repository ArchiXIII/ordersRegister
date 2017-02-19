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

    private VerticalLayout customerTableLayout;
    private VerticalLayout mainOrderLayout;
    private VerticalLayout orderTableLayout;

    private TextField descriptionFilterField;
    private TextField customerFilterField;
    private TextField stateFilterField;

    @Override
    protected void init(VaadinRequest request) {
        //database creation
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

    //init clients tab
    private void initCustomerTab(TabSheet tabSheet){
        customerTableLayout = new VerticalLayout();
        customerTableLayout.setCaption("Клиенты");
        customerTableLayout.setSizeFull();
        customerTableLayout.setMargin(true);
        initCustomerTable();
        initCustomerButtons();
        tabSheet.addComponent(customerTableLayout);
    }

    //init orders tab
    private void initOrderTab(TabSheet tabSheet){
        mainOrderLayout = new VerticalLayout();
        orderTableLayout = new VerticalLayout();
        mainOrderLayout.setCaption("Заказы");
        mainOrderLayout.setSizeFull();
        mainOrderLayout.setMargin(true);
        initOrderFilter();
        mainOrderLayout.addComponent(orderTableLayout);
        initOrderTable();
        initOrderButtons();
        tabSheet.addComponent(mainOrderLayout);
    }

    //creating and filling of customers table from DB
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

        customerTableLayout.addComponent(table);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                clickedCustomerTableString = itemClickEvent.getItemId().toString();
            }
        });
    }

    private void reloadCustomerTable(){
        customerTableLayout.removeAllComponents();
        initCustomerTable();
        initCustomerButtons();
    }

    //Creating buttons for clients table
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

        customerTableLayout.addComponent(horizontalLayout);
    }

    //Creating filters for orders table
    private void initOrderFilter(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        descriptionFilterField = new TextField("Описание:");
        customerFilterField = new TextField("Клиент:");
        stateFilterField = new TextField("Статус:");
        horizontalLayout.addComponent(descriptionFilterField);
        horizontalLayout.addComponent(customerFilterField);
        horizontalLayout.addComponent(stateFilterField);
        mainOrderLayout.addComponent(horizontalLayout);

        Button filterButton = new Button("Применить");
        filterButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                reloadOrderTab();
            }
        });
        mainOrderLayout.addComponent(filterButton);
        mainOrderLayout.addComponent(new Label(""));

    }

    //creating and filling of orders table from DB
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

        filteringOrdersList();

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

        orderTableLayout.addComponent(table);
        table.addItemClickListener(new ItemClickEvent.ItemClickListener() {
            @Override
            public void itemClick(ItemClickEvent itemClickEvent) {
                clickedOrderTableString = itemClickEvent.getItemId().toString();
            }
        });
    }

    //Filtering data from DB
    private void filteringOrdersList(){
        if(!"".equals(descriptionFilterField.getValue())){
            for(int i = orders.size()-1; i >= 0; i--){
                if(!orders.get(i).getDescription().toLowerCase().contains(descriptionFilterField.getValue().toLowerCase())) orders.remove(i);
            }
        }
        if(!"".equals(customerFilterField.getValue())){
            for(int i = orders.size()-1; i >= 0; i--){
                if(!orders.get(i).getCustomer().getName().toLowerCase().contains(customerFilterField.getValue().toLowerCase())) orders.remove(i);
            }
        }
        if(!"".equals(stateFilterField.getValue())){
            for(int i = orders.size()-1; i >= 0; i--){
                if(!orders.get(i).getState().toString().toLowerCase().contains(stateFilterField.getValue().toLowerCase())) orders.remove(i);
            }
        }
    }

    private void reloadOrderTab(){
        orderTableLayout.removeAllComponents();
        initOrderTable();
        initOrderButtons();
    }

    //Creating buttons for orders table
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
                        reloadOrderTab();
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
                        reloadOrderTab();
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
                reloadOrderTab();
            }
        });
        horizontalLayout.addComponent(button3);

        orderTableLayout.addComponent(horizontalLayout);
    }
}