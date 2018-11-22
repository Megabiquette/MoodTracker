package com.albanfontaine.moodtracker.controller;

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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private ArrayList<Mood> mMoodList;
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

        // Gets the list from MainActivity
        String moodList = (String) getIntent().getExtras().get("moodList");
        Gson gson = new Gson();
        Type arrayType = new TypeToken<ArrayList<Mood>>() {
        }.getType();
        mMoodList = gson.fromJson(moodList, arrayType);

        // Setting up the 7 days past
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
        for (int i = 0; i < 7; i++) {
            iconList[i].setOnClickListener(this);
            iconList[i].setTag(i);
            updateMoodLayout(mMoodList.get(i).getMood(), layoutList[i]);
            if (mMoodList.get(i).getComment().trim().equals("")) {
                iconList[i].setVisibility(View.GONE);
            }
        }
    }

    // Sets the layout according to the mood
    public void updateMoodLayout(int mood, RelativeLayout layout) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) layout.getLayoutParams();

        // Used to convert pixels to dp
        float d = getResources().getDisplayMetrics().density;
        switch (mood) {
            case 0:
                layout.setBackgroundColor(getResources().getColor(R.color.faded_red));
                params.setMarginEnd((int) (290 * d));
                break;
            case 1:
                layout.setBackgroundColor(getResources().getColor(R.color.warm_grey));
                params.setMarginEnd((int) (225 * d));
                break;
            case 2:
                layout.setBackgroundColor(getResources().getColor(R.color.cornflower_blue_65));
                params.setMarginEnd((int) (150 * d));
                break;
            case 3:
                layout.setBackgroundColor(getResources().getColor(R.color.light_sage));
                params.setMarginEnd((int) (75 * d));
                break;
            case 4:
                layout.setBackgroundColor(getResources().getColor(R.color.banana_yellow));
                params.setMarginEnd(0);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        // Shows the comment of the mood according to the tag
        int buttonClicked = (int) v.getTag();
        Toast.makeText(this, mMoodList.get(buttonClicked).getComment(), Toast.LENGTH_LONG).show();
    }

}
