package com.uc.ronrwin.uctopic.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.uc.ronrwin.uctopic.R;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/2
 * Author      : Ronrwin
 */

public class PercentCircle extends View {

    private Paint mPaint;
    private Paint mBitmapPaint;
    private int mWidth;
    private int mHeight;
    private RectF mRectF;
    private float mPercent;
    private int mAlpha;
    private int transX;
    private int transY;

    private Bitmap bitmap;
    private Matrix mMatrix;


    public PercentCircle(Context context) {
        this(context, null);
    }

    public PercentCircle(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PercentCircle(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mMatrix = new Matrix();
        mPaint.setColor(context.getResources().getColor(R.color.theme));
        mBitmapPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            mRectF = new RectF(0, 0, mWidth, mHeight);
            bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.last_read_refresh_white), mWidth * 2 / 3, mHeight * 2 / 3, false);
            transX = mWidth / 2 - bitmap.getWidth() / 2;
            transY = mHeight / 2 - bitmap.getHeight() / 2;
        }
    }

    public void setPercent(float percent) {
        this.mPercent = percent;
//        mAlpha = (int) ((mPercent - 1) * 255);
//        if (mAlpha > 255) {
//            mAlpha = 255;
//        }
        invalidate();
    }

    public void setAlpha(float alpha) {
        mAlpha = (int) (alpha * 255);
        if (mAlpha > 255) {
            mAlpha = 255;
        }
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawArc(mRectF, -90, 360 * mPercent, true, mPaint);
        if (mPercent > 1f) {
            canvas.save();
            mMatrix.reset();
            mBitmapPaint.setAlpha(mAlpha);
            canvas.rotate(90f * ((float) mAlpha / 255f), mWidth / 2, mHeight / 2);
            canvas.translate(transX, transY);
            canvas.drawBitmap(bitmap, 0, 0, mBitmapPaint);
            canvas.restore();
        }
        super.onDraw(canvas);

    }
}
