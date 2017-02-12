package com.haulmont.testtask;

import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by admin on 12.02.2017.
 */
@Theme(ValoTheme.THEME_NAME)
public class OrdersUI extends UI {
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(new Label("Редактирование клиента"));
        layout.setMargin(true);
        setContent(layout);
    }
}
