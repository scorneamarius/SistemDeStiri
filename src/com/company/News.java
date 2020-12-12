package com.company;

import java.io.Serializable;
import java.util.Date;

public class News implements Serializable {
    private String author;
    private String publicationDate;
    private String domain;
    private String title;
    private String text;


    public String getAuthor() {
        return author;
    }

    public News(String domain, String title, String author, String text){
        this.domain = domain;
        this.title=title;
        this.author = author;
        this.publicationDate = new Date().toString();
        this.text = text;
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

    public String getTitle(){
        return title;
    }
}