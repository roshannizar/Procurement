package com.example.procurement.models;

public class Inventory {

    private String itemNo;
    private String name;
    private String description;
    private int quantity;
    private double unitprice;

    public Inventory() {
    }

    public Inventory(String itemNo, String name, String description, int quantity, double unitprice) {
        this.itemNo = itemNo;
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.unitprice = unitprice;
    }

    public String getItemNo() {
        return itemNo;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public String getItemName() {
        return name;
    }

    public void setItemName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }
}
