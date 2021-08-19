package com.stucom.wormy;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

public class GameView extends View {

    private Game game;

    public GameView(Context context) { this(context, null, 0); }
    public GameView(Context context, AttributeSet attrs) { this(context, attrs, 0); }
    public GameView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final ViewTreeObserver vto = this.getViewTreeObserver();
        vto.addOnGlobalLayoutListener (new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                GameView.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int width  = GameView.this.getWidth();
                int height = GameView.this.getHeight();
                game.setViewSize(width, height);
            }
        });
    }

    public void setGame(Game game) { this.game = game; }

    @Override public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        game.onDraw(canvas);
    }

    public void release() {
        this.game = null;
    }

}
