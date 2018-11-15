package com.albanfontaine.moodtracker.controller;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Mood> mMoodList;
    private RelativeLayout mBackground;
    private ImageView mSmiley;
    private ImageView mAddNote;
    private ImageView mHistory;
    private int mCurrentMood;
    private String mComment;
    private int mSoundNote;
    private String mCurrentDate;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBackground = findViewById(R.id.main_activity_background);
        mSmiley = findViewById(R.id.main_activity_smiley);
        mAddNote = findViewById(R.id.main_activity_add_note);
        mHistory = findViewById(R.id.main_activity_history);
        gson = new Gson();

        // Initializing the default mood
        mCurrentMood = 3;
        mComment = "";
        mSoundNote = R.raw.note3;

        mAddNote.setOnClickListener(this);
        mHistory.setOnClickListener(this);
        mAddNote.setTag(0);
        mHistory.setTag(1);

        View screenView = findViewById(R.id.main_activity_background);

        // Changes the current mood on slide up/down
        screenView.setOnTouchListener(new OnSlidingTouchListener(this) {
            @Override
            public boolean onSlideUp() {
                if (mCurrentMood < 4){
                    mCurrentMood++;
                    changeMood();
                    playSound();
                }
                return true;
            }

            @Override
            public boolean onSlideDown() {
                if (mCurrentMood > 0){
                    mCurrentMood--;
                    changeMood();
                    playSound();
                }
                return true;
            }
        });

        // Remember the mood of the day from the SharedPreferences
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        mCurrentMood = prefs.getInt("currentMood", 3);
        mComment = prefs.getString("comment", "");
        changeMood();

        //Remember the date
        if(prefs.contains("date")){
            mCurrentDate = prefs.getString("date", "");
        } else {
            mCurrentDate = getTodaysDate();
        }

        // Remember the mood list
        if(prefs.contains("moodList")){
            String moodList = prefs.getString("moodList", null);
            Type arrayType = new TypeToken<ArrayList<Mood>>() {}.getType();
            mMoodList = gson.fromJson(moodList, arrayType);
        }else{
            mMoodList = new ArrayList<Mood>();
            mMoodList.add(new Mood(3, ""));
            mMoodList.add(new Mood(3, ""));
            mMoodList.add(new Mood(4, "Super journée"));
            mMoodList.add(new Mood(2, ""));
            mMoodList.add(new Mood(0, "La cata"));
            mMoodList.add(new Mood(3, ""));
            mMoodList.add(new Mood(1, "bof"));
        }

        // If it's a new day, update the history
        if(!mCurrentDate.equals(getTodaysDate()))
            updateHistory();
    }

    @Override
    public void onClick(View v) {
        int buttonClicked = (int) v.getTag();
        if (buttonClicked == 0) {
            final EditText editText = new EditText(this);
            editText.setHint("Votre commentaire");
            if(!mComment.trim().equals("")){
                editText.setText(mComment);
                editText.setSelectAllOnFocus(true);
            }
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Commentaire");
            alert.setMessage(mComment);
            alert.setView(editText);
            alert.setPositiveButton("Valider", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mComment = editText.getText().toString();
                }
            });
            alert.setNegativeButton("Annuler", null);
            alert.show();
        } else if (buttonClicked == 1) {
            Intent historyActivityIntent = new Intent(MainActivity.this, HistoryActivity.class);
            Type arrayType = new TypeToken<ArrayList<Mood>>() {}.getType();
            String moodList = gson.toJson(mMoodList, arrayType);
            historyActivityIntent.putExtra("moodList", moodList);
            startActivity(historyActivityIntent);
        }
    }

    private void changeMood() {
        switch (mCurrentMood) {
            case 0:
                mBackground.setBackgroundColor(getResources().getColor(R.color.faded_red));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_sad));
                mSoundNote = R.raw.note0;
                break;
            case 1:
                mBackground.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_disappointed));
                mSoundNote = R.raw.note1;
                break;
            case 2:
                mBackground.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_normal));
                mSoundNote = R.raw.note2;
                break;
            case 3:
                mBackground.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_happy));
                mSoundNote = R.raw.note3;
                break;
            case 4:
                mBackground.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_super_happy));
                mSoundNote = R.raw.note4;
                break;
        }
    }

    public void playSound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, mSoundNote);
        mediaPlayer.start();
    }

    // Returns today's date properly formatted as a String
    public String getTodaysDate(){
        return new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
    }

    @Override
    protected void onStop() {
        // Remembers the current mood
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.edit().putInt("currentMood", mCurrentMood).apply();
        prefs.edit().putString("comment", mComment).apply();
        prefs.edit().putString("date", mCurrentDate).apply();
        Type arrayType = new TypeToken<ArrayList<Mood>>() {}.getType();
        prefs.edit().putString("moodList", gson.toJson(mMoodList, arrayType)).apply();

        super.onStop();
    }

    // Removes the oldest entry and adds the current mood to the moods history list
     public void updateHistory(){
        mMoodList.remove(0);
        mMoodList.add(new Mood(mCurrentMood, mComment));
        mCurrentDate = getTodaysDate();
        mCurrentMood = 3;
        mComment = "";
        changeMood();
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.edit().remove("currentMood").apply();
        prefs.edit().remove("comment").apply();
        prefs.edit().remove("date").apply();

    }

}
