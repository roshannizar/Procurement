package com.example.procurement.models;

public class InventoryData {

    private String name;
    private String progressLevel;
    private String count;

    public InventoryData() {
        name = null;
        progressLevel = null;
        count =null;
    }

    public InventoryData(String name, String progressLevel, String count) {
        this.name = name;
        this.progressLevel = progressLevel;
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    public String getIName() {
        return name;
    }

    public String getProgressLevel() {
        return progressLevel;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public void setIName(String name) {
        this.name = name;
    }

    public void setProgressLevel(String progressLevel) {
        this.progressLevel = progressLevel;
    }
}
