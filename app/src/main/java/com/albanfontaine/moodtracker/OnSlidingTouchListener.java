package com.albanfontaine.moodtracker;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class OnSlidingTouchListener implements View.OnTouchListener {
    private GestureDetector gestureDetector;

    protected OnSlidingTouchListener(Context context){
        gestureDetector = new GestureDetector(context, new GestureListener());
    }

    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {
        private final String TAG = GestureListener.class.getSimpleName();
        private static  final int SLIDE_THRESHOLD = 50;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return onClick();
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                float deltaY = e2.getY() - e1.getY();

                if (Math.abs(deltaY) > SLIDE_THRESHOLD) {
                    if (deltaY > 0) {
                        // the user made a sliding down gesture
                        return onSlideDown();
                    } else {
                        // the user made a sliding up gesture
                        return onSlideUp();
                    }
                }

            } catch (Exception exception) {
                Log.e(TAG, exception.getMessage());
            }

            return false;
        }
    }
    public boolean onClick() {
        return false;
    }

    public boolean onSlideUp() {
        return false;
    }

    public boolean onSlideDown() {
        return false;
    }
}
