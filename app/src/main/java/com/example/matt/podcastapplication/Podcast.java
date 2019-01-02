package com.example.matt.podcastapplication;

public class Podcast {
    private String title;
    private String description;
    private String id;

    public Podcast(String title, String description, String audio) {
        this.title = title;
        this.description = description;
        this.id = audio;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getID() {
        return id;
    }
}
