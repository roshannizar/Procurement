package com.example.procurement.models;

public class Notification {
    private String ID;
    private String status;
    private String orderKey;
    private String notificationKey;
    private String requisitionKey;

    public Notification() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderKey() {
        return orderKey;
    }

    public void setOrderKey(String orderKey) {
        this.orderKey = orderKey;
    }

    public String getNotificationKey() {
        return notificationKey;
    }

    public void setNotificationKey(String notificationKey) {
        this.notificationKey = notificationKey;
    }

    public String getRequisitionKey() {
        return requisitionKey;
    }

    public void setRequisitionKey(String requisitionKey) {
        this.requisitionKey = requisitionKey;
    }
}
