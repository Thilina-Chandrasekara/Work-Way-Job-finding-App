package com.example.workway;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 2000; // 2 seconds
    private static final String PREFS_NAME = "WeatherPrefs";
    private static final String KEY_LOGGED_IN = "logged_in";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            boolean isLoggedIn = prefs.getBoolean(KEY_LOGGED_IN, false);
            Log.d("SplashActivity", "Logged in: " + isLoggedIn);

            Intent nextActivityIntent;
            if (isLoggedIn) {
                nextActivityIntent = new Intent(SplashActivity.this, HomePage.class);
            } else {
                nextActivityIntent = new Intent(SplashActivity.this, MainActivity.class);
            }

            startActivity(nextActivityIntent);
            finish(); // Close the splash activity
        }, SPLASH_DISPLAY_LENGTH);
    }
}
