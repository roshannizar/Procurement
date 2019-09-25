package com.example.procurement.models;

public class Requisition {

    private String requisitionNo;
    private String itemNo;
    private String description;
    private String comment;
    private String budget;
    private String purpose;

    public Requisition() {
        requisitionNo = null;
        itemNo = null;
        description = null;
        comment = null;
        budget = null;
        purpose = null;
    }

    public Requisition(String requisitionNo, String itemNo, String description, String comment, String budget, String purpose) {
        this. requisitionNo = requisitionNo;
        this.itemNo = itemNo;
        this.description = description;
        this.comment = comment;
        this.budget = budget;
        this.purpose = purpose;
    }

    public String getBudget() {
        return budget;
    }

    public String getComment() {
        return comment;
    }

    public String getDescription() {
        return description;
    }

    public String getItemNo() {
        return itemNo;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getRequisitionNo() {
        return requisitionNo;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItemNo(String itemNo) {
        this.itemNo = itemNo;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setRequisitionNo(String requisitionNo) {
        this.requisitionNo = requisitionNo;
    }
}
