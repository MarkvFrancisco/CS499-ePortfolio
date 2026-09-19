package com.example.cs360_projectthree.model;


public class Event {
    // Event fields
    private int id;
    private String date;
    private String title;
    private String description;
    private String phone;
    private int smsSent;

    // Event Constructor
    public Event(int id, String date, String title, String description, String phone, int smsSent) {
        this.id = id;
        this.date = date;
        this.title = title;
        this.description = description;
        this.phone = phone;
        this.smsSent = smsSent;
    }

    // Getter methods for the fields
    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPhone() {
        return phone;
    }

    public int getSmsSent() {
        return smsSent;
    }
}