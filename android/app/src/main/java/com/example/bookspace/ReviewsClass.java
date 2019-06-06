package com.example.bookspace;

public class ReviewsClass {
    private int id;
    private String name;
    private String text;
    private String created;

    public ReviewsClass(int id, String name, String text , String created) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.created = created;
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

    public String getText() {
        return text;
    }

    public void setText(String author) {
        this.text = author;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}