package com.ismael.musicclock;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    Audio audio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audio = new Audio(this);

        ClockView clockView = findViewById(R.id.clockView);
        clockView.setAudio(audio);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audio.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        audio.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        audio.onResume();
    }
}