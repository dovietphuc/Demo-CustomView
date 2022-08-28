package com.example.democustomview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class CircleView extends View {

    private float mRadius;
    private int mColor;
    private float mProgress;
    private Paint mPaint;
    private ValueAnimator mValueAnimator;
    private OnProgressUpdateListener mUpdateListener;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        if(attrs != null){
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleViewAttrs);
            mRadius = a.getDimensionPixelSize(R.styleable.CircleViewAttrs_CircleView_radius, -1);
            mColor = a.getColor(R.styleable.CircleViewAttrs_CircleView_color, -1);
            mProgress = a.getFloat(R.styleable.CircleViewAttrs_CircleView_progress, 0);
            a.recycle();
        } else {
            mRadius = -1;
            mColor = -1;
            mProgress = 0;
        }

        mPaint = new Paint();
        mPaint.setColor(mColor);
        mValueAnimator = new ValueAnimator();
        mValueAnimator.addUpdateListener(animation -> {
            setProgress((Float) animation.getAnimatedValue());
            if(mUpdateListener != null){
                mUpdateListener.onUpdate(mProgress);
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(mRadius == -1){
            mRadius = Math.min(getWidth() / 2f, getHeight() / 2f);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float left = getWidth() / 2f - mRadius;
        float top = getHeight() / 2f - mRadius;
        float right = left + mRadius * 2;
        float bottom = top + mRadius * 2;
        float endAngle = (360 / 100f) * mProgress;
        canvas.drawArc(left, top, right, bottom, -90, endAngle, true, mPaint);
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        if(radius < 0){
            throw new RuntimeException("Radius must be grater than 0");
        }
        this.mRadius = radius;
        invalidate();
    }

    public int getColor() {
        return mColor;
    }

    public void setColor(int color) {
        this.mColor = color;
        invalidate();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float progress) {
        if(progress < 0 || progress > 100){
            throw new RuntimeException("Progress must be in range 0 to 100");
        }
        this.mProgress = progress;
        invalidate();
    }

    public float getOnProgressUpdateListener() {
        return mProgress;
    }

    public void setOnProgressUpdateListener(OnProgressUpdateListener listener) {
        this.mUpdateListener = listener;
    }

    public void startAnimationProgress(long duration){
        if(mValueAnimator.isRunning()){
            throw new RuntimeException("Animation is running");
        }
        mValueAnimator.setFloatValues(mProgress, 100);
        long realDuration = (long) ((duration / 100f) * (100f - mProgress));
        mValueAnimator.setDuration(realDuration);
        mValueAnimator.start();
    }

    public interface OnProgressUpdateListener {
        public void onUpdate(float progress);
    }
}
