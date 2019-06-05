package com.example.bookspace;

public class NoteClass {
    private int id;
    private String title;
    private String text;
    private String created;

    public NoteClass(int id, String title, String text , String created) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.created = created;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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