package com.example.procurement.models;

public class Enquire {
    private String enquireID;
    private String enquiry;
    private String timestamp;
    private String enquiryType;

    public Enquire() {
    }

    public Enquire(String enquireID, String enquiry, String timestamp, String enquiryType) {
        this.enquireID = enquireID;
        this.enquiry = enquiry;
        this.timestamp = timestamp;
        this.enquiryType = enquiryType;
    }

    public String getEnquireID() {
        return enquireID;
    }

    public void setEnquireID(String enquireID) {
        this.enquireID = enquireID;
    }

    public String getEnquiry() {
        return enquiry;
    }

    public void setEnquiry(String enquiry) {
        this.enquiry = enquiry;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getEnquiryType() {
        return enquiryType;
    }

    public void setEnquiryType(String enquiryType) {
        this.enquiryType = enquiryType;
    }
}
