package com.stucom.wormy;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class VolumeControl extends View {
    private static final int PADDING = 5;
    private final Paint paint = new Paint();
    private final Path bgTriangle = new Path();
    private final Path volumeTriangle = new Path();

    private float volume;

    public VolumeControl(Context context) {  this(context, null, 0); }
    public VolumeControl(Context context, @Nullable AttributeSet attrs) { this(context, attrs, 0); }
    public VolumeControl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        volume = 0.5f;
    }

    public float getVolume() { return volume; }

    public void setVolume(float volume) {
        if (volume < 0.0f) volume = 0.0f;
        if (volume > 1.0f) volume = 1.0f;
        this.volume = volume;
        this.invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        // canvas.drawColor(Color.YELLOW);
        float w = getWidth();
        float h = getHeight();

        // Eval all needed positions
        float x0 = PADDING;
        float x1 = (w - 2 * PADDING) * volume + PADDING;
        float x2 = w - PADDING;
        float y0 = PADDING;
        float y1 = (h - 2 * PADDING) * (1 - volume) + PADDING;
        float y2 = h - PADDING;

        // Paint bg triangle
        paint.setColor(Color.LTGRAY);
        bgTriangle.reset();
        bgTriangle.moveTo(x0, y2);
        bgTriangle.lineTo(x2, y0);
        bgTriangle.lineTo(x2, y2);
        bgTriangle.close();
        canvas.drawPath(bgTriangle, paint);

        // Paint volume triangle
        paint.setColor(Color.BLUE);
        volumeTriangle.reset();
        volumeTriangle.moveTo(x0, y2);
        volumeTriangle.lineTo(x1, y1);
        volumeTriangle.lineTo(x1, y2);
        volumeTriangle.close();
        canvas.drawPath(volumeTriangle, paint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
            float x = event.getX();
            int w = getWidth();
            if (x < PADDING) x = PADDING;
            if (x > w - PADDING) x = w - PADDING;
            volume = (x - PADDING) / (w - 2 * PADDING);
            this.invalidate();

            // Notify the listener
            if (listener != null) {
                listener.onVolumeChanged(this, volume);
            }
        }
        return true;
    }

    // Listener saved to be alerted in value change
    private VolumeControlListener listener;
    public void setVolumeControlListener(VolumeControlListener listener) {
        this.listener = listener;
    }

    // Interface declaration for volume change events
    public interface VolumeControlListener {
        void onVolumeChanged(VolumeControl view, float volume);
    }
}
