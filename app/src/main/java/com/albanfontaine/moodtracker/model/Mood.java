package com.albanfontaine.moodtracker.model;

public class Mood {
    private int mMood;
    private int mColor;
    private String mComment;

    // Getters and setters
    public int getMood() {
        return mMood;
    }
    public void setMood(int mood) {
        mMood = mood;
    }
    public int getColor() {
        return mColor;
    }
    public void setColor(int color) {
        mColor = color;
    }
    public String getComment() {
        return mComment;
    }
    public void setComment(String comment) {
        this.mComment = comment;
    }

    // Constructors
    public Mood(int mood, int color){
        this.mMood = mood;
        this.mColor = color;
    }
    public Mood(int mood, int color, String comment){
        this.mMood = mood;
        this.mColor = color;
        this.mComment = comment;
    }
}
