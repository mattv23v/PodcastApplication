package com.example.matt.podcastapplication;

public class Episode {
    private String audio;
    private String title;
    private String date;

    public String getAudio() {
        return audio;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public Episode(String audio, String title, String date) {
        this.audio = audio;
        this.title = title;

        this.date = date;
    }
}
