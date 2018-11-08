package com.albanfontaine.moodtracker.controller;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.albanfontaine.moodtracker.R;
import com.albanfontaine.moodtracker.model.Mood;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Mood> mMoodList = new ArrayList<Mood>();
    private RelativeLayout mMood0;
    private RelativeLayout mMood1;
    private RelativeLayout mMood2;
    private RelativeLayout mMood3;
    private RelativeLayout mMood4;
    private RelativeLayout mMood5;
    private RelativeLayout mMood6;
    private ImageView mCom0;
    private ImageView mCom1;
    private ImageView mCom2;
    private ImageView mCom3;
    private ImageView mCom4;
    private ImageView mCom5;
    private ImageView mCom6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        mMoodList.add(new Mood(4, ""));
        mMoodList.add(new Mood(3, ""));
        mMoodList.add(new Mood(4, "Super journée"));
        mMoodList.add(new Mood(2, ""));
        mMoodList.add(new Mood(0, "La cata"));
        mMoodList.add(new Mood(3, ""));
        mMoodList.add(new Mood(1, ""));

        mMood0 = findViewById(R.id.activity_history_layout_O);
        mMood1 = findViewById(R.id.activity_history_layout_1);
        mMood2 = findViewById(R.id.activity_history_layout_2);
        mMood3 = findViewById(R.id.activity_history_layout_3);
        mMood4 = findViewById(R.id.activity_history_layout_4);
        mMood5 = findViewById(R.id.activity_history_layout_5);
        mMood6 = findViewById(R.id.activity_history_layout_6);
        mCom0 = findViewById(R.id.activity_history_com_0);
        mCom1 = findViewById(R.id.activity_history_com_1);
        mCom2 = findViewById(R.id.activity_history_com_2);
        mCom3 = findViewById(R.id.activity_history_com_3);
        mCom4 = findViewById(R.id.activity_history_com_4);
        mCom5 = findViewById(R.id.activity_history_com_5);
        mCom6 = findViewById(R.id.activity_history_com_6);

        // Setting up the listeners, tags, visibility of comment icons and layouts color
        RelativeLayout[] layoutList = {mMood0, mMood1, mMood2, mMood3, mMood4, mMood5, mMood6};
        ImageView[] iconList = {mCom0, mCom1, mCom2, mCom3, mCom4, mCom5, mCom6};
        for(int i = 0; i < 7; i++){
            iconList[i].setOnClickListener(this);
            iconList[i].setTag(i);
            updateMoodLayout(mMoodList.get(i).getMood(), layoutList[i]);
            if(mMoodList.get(i).getComment().trim().equals("")){
                iconList[i].setVisibility(View.GONE);
            }
        }

        // Remember the mood list
        String json = getPreferences(MODE_PRIVATE).getString("moodList", null);
        if(json != null){
            Gson gson = new Gson();
            mMoodList = gson.fromJson(json, ArrayList.class);
        }
    }

    public void updateMoodLayout(int mood, RelativeLayout layout){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();
        float d = getResources().getDisplayMetrics().density;
        switch (mood) {
            case 0:
                layout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                params.setMarginEnd((int)(290*d));
                break;
            case 1:
                layout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                params.setMarginEnd((int)(225*d));
                break;
            case 2:
                layout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                params.setMarginEnd((int)(150*d));
                break;
            case 3:
                layout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                params.setMarginEnd((int)(75*d));
                break;
            case 4:
                layout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                params.setMarginEnd(0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int buttonClicked = (int) v.getTag();
        Toast.makeText(this, mMoodList.get(buttonClicked).getComment(), Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        Gson gson = new Gson();
        String json = gson.toJson(mMoodList);
        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        prefs.edit().putString("moodList", json).apply();

        super.onStop();
    }
}
