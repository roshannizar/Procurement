package com.example.procurement.models;

public class Supplier {

    private String supplierId;
    private String supplierName;
    private String expectedDate;
    private String offer;
    private String status;

    public Supplier() {

    }

    public Supplier(String supplierName, String expectedDate, String offer, String status) {
        this.supplierName = supplierName;
        this.expectedDate = expectedDate;
        this.offer = offer;
        this.status = status;
    }

    public String getExpectedDate() {
        return expectedDate;
    }

    public String getOffer() {
        return offer;
    }

    public String getSupplierStatus() {
        return status;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setExpectedDate(String expectedDate) {
        this.expectedDate = expectedDate;
    }

    public void setOffer(String offer) {
        this.offer = offer;
    }

    public void setSupplierStatus(String status) {
        this.status = status;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
