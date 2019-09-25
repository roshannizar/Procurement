package com.example.procurement.models;


public class Order {
    private String orderID;
    private String name;
    private String status;
    private String date;
    private String description;
    private String key;

    public Order() {
    }

    public Order(String orderID, String name, String description, String status, String date) {
        this.orderID = orderID;
        this.name = name;
        this.description = description;
        this.status = status;
        this.date = date;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
