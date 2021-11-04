package com.example.task_fixedtable;


import androidx.annotation.NonNull;import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;



import java.lang.ref.SoftReference;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FixedHeaderTableLayout fixedHeaderTableLayout;
    private ProgressBar pgsBar;

    FixedHeaderSubTableLayout mainTable;
    FixedHeaderSubTableLayout columnHeaderTable;
    FixedHeaderSubTableLayout rowHeaderTable;
    FixedHeaderSubTableLayout cornerTable;
    private boolean shouldClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fixedHeaderTableLayout = findViewById(R.id.FixedHeaderTableLayout);
        pgsBar = findViewById(R.id.pBar);

        // Really this should be done in the background as generating such a big layout takes time
        GenerateTables generateTables = new GenerateTables(this);
        generateTables.execute();



    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // Redraw screen calculating the new boundaries without new pan or scale
            fixedHeaderTableLayout.calculatePanScale(0, 0, 1f);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // Redraw screen calculating the new boundaries without new pan or scale
            fixedHeaderTableLayout.calculatePanScale(0, 0, 1f);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class GenerateTables extends AsyncTask<Void, Integer, Void> {

        private final SoftReference<MainActivity> activityReference;
        private final Context mContext;

        // only retain a soft reference to the activity
        GenerateTables(MainActivity context) {
            mContext = context;
            activityReference = new SoftReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            activity.pgsBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {
            createTable(mContext);
            return null;
        }


        @Override
        protected void onPostExecute(Void param) {

            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            // Setup FixHeader Table
            fixedHeaderTableLayout.addViews(mainTable, columnHeaderTable, rowHeaderTable, cornerTable);
            fixedHeaderTableLayout.setTag("fixedHeaderTableLayout");
            mainTable.setTag("mainTable");
            columnHeaderTable.setTag("columnHeaderTable");
            activity.pgsBar.setVisibility(View.GONE);
        }

    }


    private static final String ALLOWED_CHARACTERS = "qwertyuiopasdfghjklzxcvbnm";

    private static String getRandomString(int maxLength) {
        final Random random = new Random();
        final int sizeOfRandomString = random.nextInt(maxLength);
        final StringBuilder sb = new StringBuilder(sizeOfRandomString);
        for (int i = 0; i < sizeOfRandomString; ++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createTable(Context mContext) {
        // Create our 4 Sub Tables
        mainTable = new FixedHeaderSubTableLayout(mContext);
        // 25 x 5 in size
        float textSize = 20.0f;
        for (int i = 1; i <= 25; i++) {
            FixedHeaderTableRow tableRowData = new FixedHeaderTableRow(mContext);
            // Add some data
            for (int j = 1; j <= 25; j++) {

                // Add a Textview
                TextView textView = new TextView(mContext);
                textView.setGravity(Gravity.CENTER);
                textView.setText(String.format(Locale.ROOT, "C%d:R%d:", j, i));
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_launcher_foreground, 0, 0);
                textView.setBackgroundResource(R.drawable.list_border);
                textView.setPadding(20, 20, 20, 20);
                textView.setTextSize(textSize * 1.5f);
                textView.setTag(textView.getText().toString());
                textView.setTextColor(getResources().getColor(R.color.colorText));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {

                            @Override
                            public void onClick() {
                                super.onClick();
                                // your on click here
                                Toast.makeText(MainActivity.this, "click", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onDoubleClick() {
                                super.onDoubleClick();
                                // your on onDoubleClick here
                                Toast.makeText(MainActivity.this, "onDoubleClick", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onLongClick() {
                                super.onLongClick();
                                // your on onLongClick here
                            }

                            @Override
                            public void onSwipeUp() {
                                super.onSwipeUp();
                                // your swipe up here
                            }

                            @Override
                            public void onSwipeDown() {
                                super.onSwipeDown();
                                // your swipe down here.
                            }

                            @Override
                            public void onSwipeLeft() {
                                super.onSwipeLeft();
                                // your swipe left here.
                            }

                            @Override
                            public void onSwipeRight() {
                                super.onSwipeRight();
                                // your swipe right here.
                            }
                        });
                    }
                });
//                textView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Toast.makeText(MainActivity.this, "efeeg", Toast.LENGTH_SHORT).show();
//                    }
//                });

                tableRowData.setTag(" tableRowData=  "+i);
                tableRowData.addView(textView);
            }
            mainTable.addView(tableRowData);

        }

        columnHeaderTable = new FixedHeaderSubTableLayout(mContext);
        // 2 x 5 in size
        for (int i = 1; i <= 1; i++) {
            FixedHeaderTableRow tableRowData = new FixedHeaderTableRow(mContext);
            // Add some data
            for (int j = 1; j <= 20; j++) {
                // Add a Textview
                TextView textView = new TextView(mContext);
                textView.setGravity(Gravity.CENTER);
                textView.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_launcher_background, 0, 0);
                textView.setText(String.format(Locale.ROOT, "P%d", j));
                textView.setBackgroundResource(R.drawable.list_border);
                textView.setPadding(5, 5, 5, 5);
                textView.setTextSize(textSize);
                textView.setTag(textView.getText().toString());
                textView.setTextColor(getResources().getColor(R.color.colorText));
                tableRowData.addView(textView);
            }
            columnHeaderTable.addView(tableRowData);
        }
        columnHeaderTable.setBackgroundResource(R.drawable.bottom_border);

        rowHeaderTable = new FixedHeaderSubTableLayout(mContext);
        // 25 x 1 in size
        for (int i = 1; i <= 25; i++) {
            FixedHeaderTableRow tableRowData = new FixedHeaderTableRow(mContext);
            // Add some data
            for (int j = 1; j <= 1; j++) {
                // Add a Textview
                int time;
                if (i == 1)
                    time = 13 - i;
                else if (i > 13)
                    time = i - 13;
                else
                    time = i - 1;
                TextView textView = new TextView(mContext);
                textView.setGravity(Gravity.CENTER);
                if (i < 13)
                    textView.setText(String.format(Locale.ROOT, "%d:00 AM", time));
                else
                    textView.setText(String.format(Locale.ROOT, "%d:00 PM", time));
                textView.setBackgroundResource(R.drawable.list_border);
                textView.setPadding(10, 10, 10, 10);
                textView.setTextSize(textSize);
                textView.setTextColor(getResources().getColor(R.color.colorText));
                textView.setTag(textView.getText().toString());

                tableRowData.addView(textView);
            }
            rowHeaderTable.addView(tableRowData);
        }
        rowHeaderTable.setBackgroundResource(R.drawable.right_border);

        cornerTable = new FixedHeaderSubTableLayout(mContext);
        // 2 x 1 in size
        for (int i = 1; i <= 1; i++) {
            FixedHeaderTableRow tableRowData = new FixedHeaderTableRow(mContext);
            // Add some data
            for (int j = 1; j <= 1; j++) {
                ImageView imagebyCode = new ImageView(this);
                imagebyCode.setImageResource(R.drawable.ic_launcher_foreground);
                // Add a Textview
                TextView textView = new TextView(mContext);
                textView.setGravity(Gravity.CENTER);
                textView.setText(String.format(Locale.ROOT, ""));
                textView.setBackgroundResource(R.drawable.list_border);
                textView.setPadding(5, 5, 5, 5);
                textView.setTextSize(textSize * 1.5f);
                textView.setTextColor(getResources().getColor(R.color.colorText));
                textView.setTag(textView.getText().toString());

                tableRowData.addView(imagebyCode);
            }
            cornerTable.addView(tableRowData);
        }
        cornerTable.setBackgroundResource(R.drawable.corner_border);

        fixedHeaderTableLayout.setMinScale(0.1f);
    }

}