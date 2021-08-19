package com.stucom.wormy;

import android.graphics.Canvas;
import android.os.Handler;

public abstract class Game {

    protected GameView gameView;
    protected boolean playing, paused;
    protected float viewWidth, viewHeight;
    protected float accelerometerX, accelerometerY;

    protected int updatePhysicsMilliseconds  = 50; // 20 physic-updates/second
    protected int updateViewEachPhysicsCount = 1;  // 1 draw each 2 physics = 10 fps
    protected Handler handler;
    private int physicsCounter = 0;

    public Game(GameView gameView) {
        this.gameView = gameView;
        gameView.setGame(this);
        handler = new Handler();
        handler.postDelayed(clock, 0);
    }

    Runnable clock = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(this, updatePhysicsMilliseconds);
            if (playing && !paused) {
                Game.this.physics();
            }
            physicsCounter = (physicsCounter + 1) % updateViewEachPhysicsCount;
            if ((physicsCounter == 0) && (gameView != null)) {
                gameView.invalidate();
            }
        }
    };

    public void release() {
        if (gameView == null) return;
        gameView.release();
        this.gameView = null;
    }

    public void pause() { paused = true; }
    public void resume() { paused = false; }
    public boolean isPaused() { return this.paused; }

    public void setPlaying(boolean playing) { this.playing = playing; }
    public boolean isPlaying() { return playing; }

    public void setViewSize(float viewWidth, float viewHeight) {
        this.viewWidth = viewWidth;
        this.viewHeight = viewHeight;
    }

    public void setAccelerometer(float accelerometerX, float accelerometerY) {
        this.accelerometerX = accelerometerX;
        this.accelerometerY = accelerometerY;
    }

    public abstract void newGame();
    public abstract void physics();
    public abstract void onDraw(Canvas canvas);
}
