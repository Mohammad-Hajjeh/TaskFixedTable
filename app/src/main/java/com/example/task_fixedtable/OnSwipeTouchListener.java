package com.example.task_fixedtable;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.view.GestureDetectorCompat;

public class OnSwipeTouchListener implements View.OnTouchListener {

    private GestureDetector gestureDetector;
    private GestureDetectorCompat detector;
    boolean shouldClick;
    private static final int MAX_CLICK_DURATION = 200;
    private long startClickTime;

    public OnSwipeTouchListener(Context c) {
        gestureDetector = new GestureDetector(c, new GestureListener());
        detector = new GestureDetectorCompat(c, new GestureListener());
    }

    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                shouldClick = true;
                break;
            case MotionEvent.ACTION_UP:
                if (shouldClick) {
                    view.performClick();
                    onClick();
                }
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                detector.onTouchEvent(motionEvent);
                gestureDetector.onTouchEvent(motionEvent);
                shouldClick = false;
                return true;
        }
        return false;
//        switch (motionEvent.getAction()) {
//            case MotionEvent.ACTION_DOWN: {
//                startClickTime = Calendar.getInstance().getTimeInMillis();
//                break;
//            }
//            case MotionEvent.ACTION_UP: {
//                long clickDuration = Calendar.getInstance().getTimeInMillis() - startClickTime;
//                if (clickDuration < MAX_CLICK_DURATION) {
//                    //click event has occurred
////                    onClick();
//                    view.performClick();
//                }
//            }
//        }
//        detector.onTouchEvent(motionEvent);
//        gestureDetector.onTouchEvent(motionEvent);
//        return false;
    }


    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            onClick();
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onDoubleClick();
            return super.onDoubleTap(e);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            onLongClick();
            super.onLongPress(e);
        }

        // Determines the fling velocity and then fires the appropriate swipe event accordingly
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                } else {
                    if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffY > 0) {
                            onSwipeDown();
                        } else {
                            onSwipeUp();
                        }
                    }
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeUp() {
    }

    public void onSwipeDown() {
    }

    public void onClick() {

    }

    public void onDoubleClick() {

    }

    public void onLongClick() {

    }
}
