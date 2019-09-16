package com.example.procurement.models;

public class Enquire {
    private String key;
    private String enquiry;
    private String timestamp;
    private String enquiryType;
    private boolean isReplied = false;
    private String reply;
    private String timestampReplied;

    public Enquire() {
    }

    public Enquire(String reply, String timestampReplied) {
        this.reply = reply;
        this.timestampReplied = timestampReplied;
    }

    public Enquire(String key, String enquiry, String timestamp, String enquiryType) {
        this.key = key;
        this.enquiry = enquiry;
        this.timestamp = timestamp;
        this.enquiryType = enquiryType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public boolean isReplied() {
        return isReplied;
    }

    public void setReplied(boolean replied) {
        isReplied = replied;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getTimestampReplied() {
        return timestampReplied;
    }

    public void setTimestampReplied(String timestampReplied) {
        this.timestampReplied = timestampReplied;
    }
}
