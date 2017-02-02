package com.haulmont.testtask.entity;

/**
 * Created by Черный on 02.02.2017.
 */
public class Customer {
    private Long id;
    private String name;
    private String surname;
    private String middleName;
    private String phone;

    public Customer(Long id, String name, String surname, String middleName, String phone) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.phone = phone;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}
