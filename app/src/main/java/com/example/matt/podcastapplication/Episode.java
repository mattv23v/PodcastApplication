package com.example.matt.podcastapplication;

public class Episode {
    private String audio;
    private String title;
    private String date;
    private String image;

    public Episode(String audio, String title, String date, String image) {
        this.audio = audio;
        this.title = title;
        this.date = date;
        this.image = image;
    }
    public String getAudio() {
        return audio;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getImage() {
        return image;
    }

}
