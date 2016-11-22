package com.hrsst.housekeeper.common.widget;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.TextView;

import com.hrsst.housekeeper.R;


/**
 * Created by dxs on 2015/12/30.
 */
public class SwitchView extends TextView {
    public final static int State_on=1;
    public final static int State_off=2;
    public final static int State_progress=0;
    private Paint mPaint;
    private Context mContext;
    private Bitmap modeBitmap,progressBitmap;
    private ArcAnimal animal;
    private float degree=0;
    private int State=0;

    public SwitchView(Context context) {
        super(context);
        mContext = context;
    }

    public SwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    private void init() {
        animal=new ArcAnimal();
        animal.setRepeatCount(-1);
        animal.setRepeatMode(Animation.RESTART);
        animal.setInterpolator(new LinearInterpolator());
        animal.setDuration(800);
        progressBitmap=BitmapFactory.decodeResource(getResources(), R.mipmap.progress_white_small);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startLoading();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint = getPaint();
        if(State!=State_progress){
            DrawModeImage(canvas);
        }else{
            canvas.save();
            DraProgress(canvas);
            canvas.restore();
        }
        super.onDraw(canvas);
    }
    private void DraProgress(Canvas canvas) {
        if (progressBitmap != null&&State==0) {
            float left = (getWidth() - progressBitmap.getWidth()) / 2;
            float top=(getHeight()-progressBitmap.getHeight())/2;
            canvas.rotate(degree*360,getWidth()/2,getHeight()/2);
            canvas.drawBitmap(progressBitmap, left, top, mPaint);
        }
    }


    private void DrawModeImage(Canvas canvas) {
        if (modeBitmap != null) {
            float left = (getWidth() - modeBitmap.getWidth()) / 2;
            float top = (getHeight() - modeBitmap.getHeight()) / 2;
            canvas.drawBitmap(modeBitmap, left, top, mPaint);
        }
    }

    public int getModeStatde() {
        return State;
    }

    public void setModeStatde(int modeStatde) {
        State = modeStatde;
        startLoading();
    }

    private void startLoading(){
            switch (State){
                case State_progress:
                    if(animal==null){
                        init();
                    }
                    this.startAnimation(animal);
                    break;
                case 3:
                case State_on:
                    modeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.on);
                    invalidate();
                    break;
                case 4:
                case State_off:
                    modeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.off);
                    invalidate();
                    break;
            }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public class ArcAnimal extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            super.applyTransformation(interpolatedTime, t);
            degree=interpolatedTime;
            postInvalidate();
        }

    }
}
