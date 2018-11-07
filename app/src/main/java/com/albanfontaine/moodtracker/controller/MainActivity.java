package com.albanfontaine.moodtracker.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.albanfontaine.moodtracker.OnSlidingTouchListener;
import com.albanfontaine.moodtracker.R;
import com.albanfontaine.moodtracker.model.Mood;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mBackground;
    private ImageView mSmiley;
    private ImageView mAddNote;
    private ImageView mHistory;
    private int mCurrentMood;
    private String mComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackground = (RelativeLayout) findViewById(R.id.main_activity_background);
        mSmiley = (ImageView) findViewById(R.id.main_activity_smiley);
        mAddNote = (ImageView) findViewById(R.id.main_activity_add_note);
        mHistory = (ImageView) findViewById(R.id.main_activity_history);
        mCurrentMood = 3;
        mComment = "";

        mAddNote.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mAddNote.setTag(0);
        mHistory.setTag(1);

        View myView = findViewById(R.id.main_activity_background);
        myView.setOnTouchListener(new OnSlidingTouchListener(this) {
            @Override
            public boolean onSlideUp() {
                if (mCurrentMood < 4){
                    mCurrentMood++;
                    changeMood();
                }
                return true;
            }

            @Override
            public boolean onSlideDown() {
                if (mCurrentMood > 0){
                    mCurrentMood--;
                    changeMood();
                }
                return true;
            }
        });

        // Remember the mood of the day
        String json = getPreferences(MODE_PRIVATE).getString("currentMood", null);
        if(json != null){
            Gson gson = new Gson();
            Mood mood = gson.fromJson(json, Mood.class);
            mCurrentMood = mood.getMood();
            if(mood.getComment() != null){
                mComment = mood.getComment();
            }
            changeMood();
        }
    }

    @Override
    public void onClick(View v) {
        int buttonClicked = (int) v.getTag();
        if (buttonClicked == 0) {
            final EditText editText = new EditText(this);
            editText.setHint("Votre commentaire");
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Commentaire");
            alert.setMessage(mComment);
            alert.setView(editText);
            alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(!editText.getText().toString().trim().equals(""))
                        mComment = editText.getText().toString();
                }
            });
            alert.setNegativeButton("Annuler", null);
            alert.show();
        } else if (buttonClicked == 1) {
            Intent historyActivity = new Intent(MainActivity.this, HistoryActivity.class);
            startActivity(historyActivity);
        }
    }

    private void changeMood() {
        switch (mCurrentMood) {
            case 0:
                mBackground.setBackgroundColor(getResources().getColor(R.color.faded_red));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_sad));
                break;
            case 1:
                mBackground.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_disappointed));
                break;
            case 2:
                mBackground.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_normal));
                break;
            case 3:
                mBackground.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_happy));
                break;
            case 4:
                mBackground.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_super_happy));
                break;
        }
    }

    @Override
    protected void onStop() {
        Mood mood;
        if(mComment.trim().equals("")){
            mood = new Mood(mCurrentMood);
        }else{
            mood = new Mood(mCurrentMood, mComment);
        }

        Gson gson = new Gson();
        String json = gson.toJson(mood);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.edit().putString("currentMood", json).apply();

        super.onStop();
    }
}
