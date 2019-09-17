package com.example.procurement.models;

public class Enquire {
    private String key;
    private String enquiry;
    private String timestamp;
    private boolean isReplied = false;
    private String reply;

    public Enquire() {
    }

    public Enquire(String key, String enquiry, String timestamp) {
        this.key = key;
        this.enquiry = enquiry;
        this.timestamp = timestamp;
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

}
