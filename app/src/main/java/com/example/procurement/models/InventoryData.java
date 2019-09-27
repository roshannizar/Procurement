package com.example.procurement.models;

public class InventoryData {

    private String iName;
    private String quantity;
    private String unitPrice;
    private String totalAmount;

    public InventoryData() {
        iName = null;
        quantity = null;
        unitPrice =null;
    }

    public InventoryData(String iName, String quantity, String unitPrice) {
        this.iName = iName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public InventoryData(String iName, String quantity, String unitPrice, String totalAmount) {
        this.iName = iName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.totalAmount = totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public String getiName() {
        return iName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public void setiName(String iname) {
        this.iName = iName;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
