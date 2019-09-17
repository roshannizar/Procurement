package com.example.procurement.models;

public class Notification {
    private String orderID;
    private String orderKey;
    private String orderStatus;
    private String notificationKey;

    public Notification() {
    }

    public Notification(String orderID, String orderKey, String orderStatus) {
        this.orderID = orderID;
        this.orderKey = orderKey;
        this.orderStatus = orderStatus;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }
}
