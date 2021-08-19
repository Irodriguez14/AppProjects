package com.ismael.musicclock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.Calendar;

public class ClockView extends View {
    private Paint paintBackground;
    private Paint paintHour;
    private Paint paintMinute;
    private Paint paintSecond;
    private Paint paintTick;
    private Paint paintCover;

    Audio audio;
    private int lastMillisecond;

    public ClockView(Context context) {
        this(context, null, 0);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paintBackground = new Paint();
        paintBackground.setStyle(Paint.Style.FILL_AND_STROKE);
        paintBackground.setColor(Color.argb(48, 255, 255, 0));
        paintHour = new Paint(paintBackground);
        paintHour.setColor(Color.RED);
        paintHour.setStrokeWidth(15.0f);
        paintMinute = new Paint(paintBackground);
        paintMinute.setColor(Color.BLUE);
        paintMinute.setStrokeWidth(10.0f);
        paintSecond = new Paint(paintBackground);
        paintSecond.setColor(Color.BLACK);
        paintSecond.setStrokeWidth(5.0f);
        paintTick = new Paint(paintBackground);
        paintTick.setColor(Color.LTGRAY);
        paintTick.setStrokeWidth(15.0f);
        paintTick.setTextSize(64.0f);
        paintCover = new Paint(paintBackground);
        paintCover.setColor(Color.BLACK);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = Math.min(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        this.postInvalidateDelayed(50);

        int width = getWidth();
        int height = getHeight();
        int size = Math.min(width, height);
        int padLeft = (width - size) / 2;
        int padTop = (height - size) / 2;
        canvas.drawRect(padLeft, padTop, padLeft + size, padTop + size, paintBackground);
        // Move origin (0,0) to center of View
        int centerX = padLeft + size / 2;
        int centerY = padTop + size / 2;
        canvas.translate(centerX, centerY);
        int maxRadius = size / 2;

        // Paint ticks
        canvas.save();
        for(int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                canvas.drawLine(0, -maxRadius * 0.85f, 0, -maxRadius * 0.95f, paintTick);
                canvas.save();
                canvas.translate(0, -maxRadius * 0.7f);
                canvas.rotate(-i * 6.0f);
                String hourText = String.valueOf((i == 0) ? 12 : i / 5);
                float w = paintTick.measureText(hourText);
                canvas.drawText(hourText, -w/2, 32, paintTick);
                canvas.restore();
            }
            else {
                canvas.drawCircle(0, -maxRadius * 0.9f, 5.0f, paintTick);
            }
            canvas.rotate(6.0f);

        }
        canvas.restore();
        // Get current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int millisecond = calendar.get(Calendar.MILLISECOND);

        if(lastMillisecond > millisecond){
            audio.playTickClock();
        }
        if((lastMillisecond > millisecond) && (second == 0) && (minute == 0)){
            audio.playDongClock();
        }
        lastMillisecond = millisecond;

        canvas.save();
        float angleSecond = (second + millisecond / 1000.0f)  * 6.0f;
        canvas.rotate(angleSecond);
        canvas.drawLine(0,0, 0, -maxRadius * 0.80f, paintSecond);
        canvas.restore();

        canvas.save();
        float angleMinute = (minute + second / 60.0f) * 6.0f;
        canvas.rotate(angleMinute);
        canvas.drawLine(0,0, 0, -maxRadius * 0.65f, paintMinute);
        canvas.restore();

        canvas.save();
        float angleHour = (hour + minute / 60.0f + second / 3600.0f) * 30.0f;
        canvas.rotate(angleHour);
        canvas.drawLine(0,0, 0, -maxRadius * 0.5f, paintHour);
        canvas.restore();

        // Paint cover circle
        canvas.drawCircle(0, 0, maxRadius * 0.1f, paintCover);

    }

    public void setAudio(Audio audio) {
        this.audio = audio;
    }
}
