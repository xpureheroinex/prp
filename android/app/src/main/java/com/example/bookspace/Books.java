package com.example.bookspace;


public class Books {
    private int id;
    private String name;
    private double rate;
    private String date;
    private String author;

    public Books(int id, String name, double rate, String date, String author ) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.date = date;
        this.author = author;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
