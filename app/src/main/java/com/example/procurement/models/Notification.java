package com.example.procurement.models;

public class Notification {
    private String notificationID;
    private String orderID;
    private String orderStatus;
    private String orderDate;

    public Notification() {
    }

    public Notification(String notificationID, String orderID, String orderStatus, String orderDate) {
        this.notificationID = notificationID;
        this.orderID = orderID;
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

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
