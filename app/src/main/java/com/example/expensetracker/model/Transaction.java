package com.example.expensetracker.model;

public class Transaction {
    private int id;
    private double amount;
    private String type;
    private String category;
    private long timestamp;
    private String note;

    // Constructor
    public Transaction(int id, double amount, String type, String category, long timestamp, String note) {
        this.id = id;
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.timestamp = timestamp;
        this.note = note;
    }

    // Constructor
    public Transaction(double amount, String type, String category, long timestamp, String note) {
        this.amount = amount;
        this.type = type;
        this.category = category;
        this.timestamp = timestamp;
        this.note = note;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
}