package com.example.procurement.models;

public class Note {
    private String key;
    private String note;
    private String timestamp;

    public Note() {
    }

    public Note(String key, String note, String timestamp) {
        this.key = key;
        this.note = note;
        this.timestamp = timestamp;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
