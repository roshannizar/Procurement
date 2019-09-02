package com.example.procurement.status;

import com.example.procurement.utils.CommonConstants;
import com.google.firebase.database.IgnoreExtraProperties;


@IgnoreExtraProperties
public class Order {
    private String orderID;
    private String name;
    private int status;
    private String date;

    public Order() {
    }

    public Order(String orderID, String name, int status, String date) {
        this.orderID = orderID;
        this.name = name;
        this.status = status;
        this.date = date;
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
