package com.example.procurement.models;

public class Note {
    private String noteID;
    private String note;
    private String timestamp;

    public Note() {
    }

    public Note(String noteID, String note, String timestamp) {
        this.noteID = noteID;
        this.note = note;
        this.timestamp = timestamp;
    }

    public String getNoteID() {
        return noteID;
    }

    public void setNoteID(String noteID) {
        this.noteID = noteID;
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
