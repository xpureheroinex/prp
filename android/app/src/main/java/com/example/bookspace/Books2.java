package com.example.bookspace;

public class Books2 {
    private int id;
    private String name;
    private double rate;
    private String author;
    private String genre;

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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Books2(int id, String name, double rate, String author, String genre ) {
        this.id = id;
        this.name = name;
        this.rate = rate;
        this.author = author;
        this.genre = genre;

    }


}
