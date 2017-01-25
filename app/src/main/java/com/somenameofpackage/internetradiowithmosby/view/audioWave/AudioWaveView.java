package com.somenameofpackage.internetradiowithmosby.view.audioWave;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.somenameofpackage.internetradiowithmosby.R;

import java.util.Random;

public class AudioWaveView extends View {
    private byte[] mBytes;
    private float[] mPoints;
    private Rect mRect;
    private Paint mForePaint;

    public AudioWaveView(Context context) {
        super(context);
        init(new Random().nextInt());
    }

    public AudioWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorWave);
        int color = attributes.getColor(R.styleable.ColorWave_wave_color, Color.RED);

        attributes.recycle();
        init(color);
    }

    public AudioWaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorWave);
        int color = attributes.getColor(R.styleable.ColorWave_wave_color, Color.RED);
        init(color);
    }

    private void init(int color) {
        mRect = new Rect();
        mForePaint = new Paint();

        mBytes = null;
        mForePaint.setStrokeWidth(3f);
        mForePaint.setAntiAlias(true);
        mForePaint.setColor(color);
    }

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) return;
        if (mPoints == null || mPoints.length < mBytes.length * 4)
            mPoints = new float[mBytes.length * 4];

        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * 4] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * 4 + 1] = mRect.height() / 2 + ((byte) (mBytes[i] + 128)) * (mRect.height() / 2) / 128;
            mPoints[i * 4 + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * 4 + 3] = mRect.height() / 2 + ((byte) (mBytes[i + 1] + 128)) * (mRect.height() / 2) / 128;
        }
        canvas.drawLines(mPoints, mForePaint);
    }
}
