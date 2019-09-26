package com.example.procurement.models;

public class Requisition {

    private String requisitionNo;
    private String itemNo;
    private String description;
    private String comment;
    private String budget;
    private String purpose;
    private String deliveryDate;
    private String requisitionStatus;
    private String totalAmount;
    private String reason;
    private String proposalDate;
    private String proposedBy;
    private String key;

    public Requisition() {
    }

    public Requisition(String requisitionNo, String comment, String purpose, String deliveryDate, String totalAmount, String requisitionStatus, String reason, String proposalDate, String proposedBy, String budget) {
        this.requisitionNo = requisitionNo;
        this.comment = comment;
        this.purpose = purpose;
        this.deliveryDate = deliveryDate;
        this.totalAmount = totalAmount;
        this.requisitionStatus = requisitionStatus;
        this.reason = reason;
        this.proposalDate = proposalDate;
        this.proposedBy = proposedBy;
        this.budget = budget;
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

    public String getReason() {
        return reason;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public String getRequisitionStatus() {
        return requisitionStatus;
    }

    public String getProposalDate() {
        return proposalDate;
    }

    public String getProposedBy() {
        return proposedBy;
    }

    public String getKey() {
        return key;
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

    public void setRequisitionStatus(String requisitionStatus) {
        this.requisitionStatus = requisitionStatus;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setProposalDate(String proposalDate) {
        this.proposalDate = proposalDate;
    }

    public void setProposedBy(String proposedBy) {
        this.proposedBy = proposedBy;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
