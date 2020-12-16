package com.company;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {

    private String author;
    private String publicationDate;
    private String domain;
    private String text;
    public EventType type;

    public enum EventType {
        ADD,DELETE,MODIFY
    }

    public String getAuthor() {
        return author;
    }

    public News(String domain, String text,EventType type){
        this.domain = domain;
        this.publicationDate = new Date().toString();
        this.text = text;
        this.type = type;
    }

    public void setAuthor(String author){
        this.author=author;
    }

    public String getText() {
        return text;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getDomain() {
        return domain;
    }

    private void setText(String text){
        this.text = text;
    }
}