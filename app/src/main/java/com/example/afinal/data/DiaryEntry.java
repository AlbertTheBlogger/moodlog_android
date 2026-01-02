package com.example.afinal.data;

import java.io.Serializable;


public class DiaryEntry implements Serializable {
    private String content;
    private String mood;
    private long timestamp;

    // Gson构造
    public DiaryEntry(String content, String mood, long timestamp) {
        this.content = content;
        this.mood = mood;
        this.timestamp = timestamp;
    }

    // Getters & Setters
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getMood() { return mood; }
    public void setMood(String mood) { this.mood = mood; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
}