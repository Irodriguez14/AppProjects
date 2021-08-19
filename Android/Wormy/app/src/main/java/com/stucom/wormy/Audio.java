package com.stucom.wormy;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

import static android.content.Context.MODE_PRIVATE;

public class Audio {
    private final Context context;

    // SoundPool for Sound Effects
    private boolean playSoundEffects;
    private float soundEffectsVolume;
    private SoundPool soundPool;
    private int coinSound, dieSound;

    // MediaPlayer for Background Music
    private boolean playBackgroundMusic;
    private float backgroundMusicVolume;
    private MediaPlayer mediaPlayer;

    public Audio(Context context) {
        this.context = context;
    }

    public void onCreate() {
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
        coinSound = soundPool.load(context, R.raw.coin, 0);
        dieSound = soundPool.load(context, R.raw.die, 0);

        // MediaPlayer for background music
        mediaPlayer = MediaPlayer.create(context, R.raw.music);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build();
            mediaPlayer.setAudioAttributes(audioAttributes);
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
        mediaPlayer.pause();
    }

    public void playCoinSound() {
        if (!playSoundEffects) return;
        soundPool.play(coinSound, soundEffectsVolume, soundEffectsVolume, 0, 0, 1.0f);
    }
    public void playDieSound() {
        if (!playSoundEffects) return;
        soundPool.play(dieSound, soundEffectsVolume, soundEffectsVolume, 0, 0, 1.0f);
    }

    public boolean isPlaySoundEffects() {
        return playSoundEffects;
    }
    public void setPlaySoundEffects(boolean playSoundEffects) {
        this.playSoundEffects = playSoundEffects;
    }

    public float getSoundEffectsVolume() {
        return soundEffectsVolume;
    }
    public void setSoundEffectsVolume(float soundEffectsVolume) {
        this.soundEffectsVolume = soundEffectsVolume;
    }

    public boolean isPlayBackgroundMusic() {
        return playBackgroundMusic;
    }
    public void setPlayBackgroundMusic(boolean playBackgroundMusic) {
        this.playBackgroundMusic = playBackgroundMusic;
        if (playBackgroundMusic) {
            mediaPlayer.start();
        } else {
            mediaPlayer.pause();
        }
    }

    public float getBackgroundMusicVolume() {
        return backgroundMusicVolume;
    }
    public void setBackgroundMusicVolume(float backgroundMusicVolume) {
        this.backgroundMusicVolume = backgroundMusicVolume;
        mediaPlayer.setVolume(backgroundMusicVolume, backgroundMusicVolume);
    }

    public void onResume() {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        playSoundEffects = prefs.getBoolean("playSoundEffects", true);
        this.setPlaySoundEffects(playSoundEffects);
        soundEffectsVolume = prefs.getFloat("soundEffectsVolume", 1.0f);
        this.setSoundEffectsVolume(soundEffectsVolume);
        playBackgroundMusic = prefs.getBoolean("playBackgroundMusic", false);
        this.setPlayBackgroundMusic(playBackgroundMusic);
        backgroundMusicVolume = prefs.getFloat("backgroundMusicVolume", 0.25f);
        this.setBackgroundMusicVolume(backgroundMusicVolume);
    }

    public void onPause() {
        SharedPreferences prefs = context.getSharedPreferences(context.getPackageName(), MODE_PRIVATE);
        SharedPreferences.Editor ed = prefs.edit();
        ed.putBoolean("playSoundEffects", playSoundEffects);
        ed.putFloat("soundEffectsVolume", soundEffectsVolume);
        ed.putBoolean("playBackgroundMusic", playBackgroundMusic);
        ed.putFloat("backgroundMusicVolume", backgroundMusicVolume);
        ed.apply();
        if (playBackgroundMusic) {
            mediaPlayer.pause();
        }
    }

    public void onDestroy() {
        // Free all sound-related resources
        soundPool.release();
        soundPool = null;
        mediaPlayer.pause();
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }
}
