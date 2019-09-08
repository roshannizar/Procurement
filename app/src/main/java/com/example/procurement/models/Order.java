package com.example.procurement.models;

import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Order {
    private String orderID;
    private String name;
    private int status;
    private String date;
    private String description;

    public Order() {
    }

    public Order(String orderID, String name, String description, int status, String date) {
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatusText() {
        switch (this.status) {
            case CommonConstants.ORDER_STATUS_APPROVED:
                return "Approved";
            case CommonConstants.ORDER_STATUS_PENDING:
                return "Pending";
            case CommonConstants.ORDER_STATUS_PLACED:
                return "Placed";
            case CommonConstants.ORDER_STATUS_HOLD:
                return "Hold";
            default:
                return "Declined";
        }
    }
}
