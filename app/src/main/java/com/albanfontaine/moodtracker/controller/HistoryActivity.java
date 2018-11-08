package com.albanfontaine.moodtracker.controller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.albanfontaine.moodtracker.R;
import com.albanfontaine.moodtracker.model.Mood;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ArrayList<Mood> mMoodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }
}
