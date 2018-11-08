package com.albanfontaine.moodtracker.model;

public class Mood {
    private int mMood;
    private String mComment;

    // Getters and setters
    public int getMood() {
        return mMood;
    }
    public void setMood(int mood) {
        mMood = mood;
    }
    public String getComment() {
        return mComment;
    }
    public void setComment(String comment) {
        this.mComment = comment;
    }

    // Constructors
    public Mood(int mood, String comment){
        this.mMood = mood;
        this.mComment = comment;
    }
}
