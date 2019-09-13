package com.example.procurement.models;

public class Notification {
    private String notificationID;
    private String orderID;
    private String orderName;
    private int orderStatus;
    private String orderDate;

    public Notification() {
    }

    public Notification(String notificationID, String orderID, String orderName, int orderStatus, String orderDate) {
        this.notificationID = notificationID;
        this.orderID = orderID;
        this.orderName = orderName;
        this.orderStatus = orderStatus;
        this.orderDate = orderDate;
    }

    public String getNotificationID() {
        return notificationID;
    }

    public void setNotificationID(String notificationID) {
        this.notificationID = notificationID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
