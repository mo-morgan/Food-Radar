package com.example.foodradar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class LoadingActivity extends AppCompatActivity {
    // for debugging purposes
    private static final String TAG = "My_message";

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Loading Activity!");
        // Show loading screen
        setContentView(R.layout.fragment_loading);


        Thread welcomeThread = new Thread(){
            public void run() {
                // Sleep for a second
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    // Go to main activity
                    Intent mainActivity = new Intent(LoadingActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                }
            }
        };
        welcomeThread.start();

    }

}
