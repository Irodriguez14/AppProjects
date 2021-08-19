package com.ismael.musicclock;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

public class Audio {
    private Context context;
    private SoundPool soundPool;
    private int tickSound, dongSound;
    MediaPlayer mediaPlayer;

    public Audio(Context context) {
        this.context = context;
        // Create the SoundPool object for sound FX (depending on API version)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();
            soundPool = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        }
        else {
            soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        tickSound = soundPool.load(context, R.raw.clock, 0);
        dongSound = soundPool.load(context, R.raw.dong, 0);

        mediaPlayer = MediaPlayer.create(context, R.raw.background_music);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.setVolume(1.0f,1.0f);
        mediaPlayer.start();

    }

    public void playTickClock() {
        soundPool.play(tickSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    public void playDongClock() {
        soundPool.play(dongSound, 1.0f, 1.0f, 0, 0, 1.0f);
    }

    // TODO Garbage collector of SoundPool object!

    public void onDestroy() {
        // Free all sound-related resources
        soundPool.release();
        soundPool = null;
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    public void onResume(){
        mediaPlayer.start();
    }

    public void onPause(){
        if (mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.stop();
        }
    }
}

/*
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        int volume = prefs.getInt("volume", 100);
        sbVolume.setProgress(volume);
        int position = mediaPlayer.getCurrentPosition();
        mediaPlayer.setVolume(volume / 100.0f, volume / 100.0f);

        // mediaPlayer.start(); if you want the music playing as background
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putInt("volume", sbVolume.getProgress());
        ed.apply();

        // Free some resources from the current audio track
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
        mediaPlayer.stop();
    }
 */