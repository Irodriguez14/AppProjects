package com.stucom.wormy;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements WormyGame.WormyListener, SensorEventListener {
    private WormyGame game;
    private TextView tvScore;

    private SensorManager sensorManager;
    private Audio audio;
    private Switch swMusic, swSoundEffects;
    private VolumeControl sbMusic, sbSoundEffects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swMusic = findViewById(R.id.swMusic);
        swSoundEffects = findViewById(R.id.swSoundEffects);
        sbMusic = findViewById(R.id.sbMusic);
        sbSoundEffects = findViewById(R.id.sbSoundEffects);

        GameView gameView = findViewById(R.id.gameView);
        game = new WormyGame(this, gameView);

        Button btnNewGame = findViewById(R.id.btnNewGame);
        tvScore = findViewById(R.id.tvScore);
        btnNewGame.setOnClickListener(v -> {
            tvScore.setText("0");
            game.newGame();
        });

        game.setWormyListener(this);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        audio = new Audio(this);
        audio.onCreate();

        swMusic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audio.setPlayBackgroundMusic(isChecked);
            }
        });
        swSoundEffects.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                audio.setPlaySoundEffects(isChecked);
            }
        });
        sbMusic.setVolumeControlListener(new VolumeControl.VolumeControlListener() {
            @Override
            public void onVolumeChanged(VolumeControl view, float volume) {
                audio.setBackgroundMusicVolume(volume);
            }
        });
        sbSoundEffects.setVolumeControlListener(new VolumeControl.VolumeControlListener() {
            @Override
            public void onVolumeChanged(VolumeControl view, float volume) {
                audio.setSoundEffectsVolume(volume);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_GAME);
        audio.onResume();
        swMusic.setChecked(audio.isPlayBackgroundMusic());
        swSoundEffects.setChecked(audio.isPlaySoundEffects());
        sbMusic.setVolume(audio.getBackgroundMusicVolume());
        sbSoundEffects.setVolume(audio.getSoundEffectsVolume());
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
        audio.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        game.release();
        audio.onDestroy();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // KEYBOARD INPUT FOR EMULATOR (simulates the accelerometer direction) "S" = 0,0 (flat)
        int[] keys = new int[] {
                KeyEvent.KEYCODE_Q, KeyEvent.KEYCODE_W, KeyEvent.KEYCODE_E,
                KeyEvent.KEYCODE_A, KeyEvent.KEYCODE_S, KeyEvent.KEYCODE_D,
                KeyEvent.KEYCODE_Z, KeyEvent.KEYCODE_X, KeyEvent.KEYCODE_C,
        };
        for (int i = 0; i < keys.length; i++) {
            if (event.getKeyCode() != keys[i]) continue;
            int aY = (i / 3) * 10 - 10;
            int aX = (i % 3) * 10 - 10;
            game.setAccelerometer(aX, aY);
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void scoreUpdated(int score) {
        tvScore.setText(String.valueOf(score));
        audio.playCoinSound();
    }

    @Override
    public void gameLost() {
        game.pause();
        audio.playDieSound();
        Toast.makeText(this, getString(R.string.you_lost), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float aX = event.values[0];
        float aY = event.values[1];
        game.setAccelerometer(-aX, aY);
    }

    @Override public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
