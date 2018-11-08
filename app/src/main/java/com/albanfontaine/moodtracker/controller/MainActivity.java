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

import com.albanfontaine.moodtracker.HistoryUpdateBroadcastReceiver;
import com.albanfontaine.moodtracker.OnSlidingTouchListener;
import com.albanfontaine.moodtracker.R;
import com.albanfontaine.moodtracker.model.Mood;
import com.google.gson.Gson;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout mBackground;
    private ImageView mSmiley;
    private ImageView mAddNote;
    private ImageView mHistory;
    private int mCurrentMood;
    private String mComment;
    private int mNote;
    private PendingIntent mPendingIntent;
    private AlarmManager mAlarmManager;

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
        mNote = R.raw.note3;

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

        // Remember the mood of the day
        String json = getPreferences(MODE_PRIVATE).getString("currentMood", null);
        if(json != null){
            Gson gson = new Gson();
            Mood mood = gson.fromJson(json, Mood.class);
            mCurrentMood = mood.getMood();
            mComment = mood.getComment();
            changeMood();
        }
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
                    //if(!editText.getText().toString().trim().equals(""))
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
                mNote = R.raw.note0;
                break;
            case 1:
                mBackground.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_disappointed));
                mNote = R.raw.note1;
                break;
            case 2:
                mBackground.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_normal));
                mNote = R.raw.note2;
                break;
            case 3:
                mBackground.setBackgroundColor(getResources().getColor(R.color.light_sage));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_happy));
                mNote = R.raw.note3;
                break;
            case 4:
                mBackground.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                mSmiley.setImageDrawable(getResources().getDrawable(R.drawable.smiley_super_happy));
                mNote = R.raw.note4;
                break;
        }
    }

    public void playSound(){
        MediaPlayer mediaPlayer = MediaPlayer.create(this, mNote);
        mediaPlayer.start();
    }

    public void updateHistory(){
        Intent updateIntent = new Intent(this, HistoryUpdateBroadcastReceiver.class);
        mPendingIntent = PendingIntent.getBroadcast(this, 0, updateIntent, 0);
        mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar midnightCalendar = Calendar.getInstance();
        midnightCalendar.set(Calendar.HOUR_OF_DAY, 0);
        midnightCalendar.set(Calendar.MINUTE, 0);
        midnightCalendar.set(Calendar.SECOND,0);
        mAlarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                midnightCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, mPendingIntent);
    }

    @Override
    protected void onStop() {
        Mood mood = new Mood(mCurrentMood, mComment);
        Gson gson = new Gson();
        String json = gson.toJson(mood);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.edit().putString("currentMood", json).apply();

        super.onStop();
    }
}
