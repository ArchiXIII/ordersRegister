package com.haulmont.testtask.entity;

import java.util.Date;

/**
 * Created by Черный on 02.02.2017.
 */
public class Order {
    private Long id;
    private String description;
    private Customer customer;
    private Date createdDate;
    private Date endWorksDate;
    private Integer price;
    private State state;

    public Order(String description, Customer customer, Integer price) {
        this.description = description;
        this.customer = customer;
        this.createdDate = new Date();
        this.price = price;
        this.state = State.PLANNED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Date getEndWorksDate() {
        return endWorksDate;
    }

    public void setEndWorksDate(Date endWorksDate) {
        this.endWorksDate = endWorksDate;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Order{" +
                "description='" + description + '\'' +
                ", customer=" + customer +
                ", createdDate=" + createdDate +
                ", endWorksDate=" + endWorksDate +
                ", price=" + price +
                ", state=" + state +
                '}';
    }
}
