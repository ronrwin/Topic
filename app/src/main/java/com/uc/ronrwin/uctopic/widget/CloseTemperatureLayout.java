package com.uc.ronrwin.uctopic.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.util.AttributeSet;
import android.view.View;

import com.uc.ronrwin.uctopic.R;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/1
 * Author      : Ronrwin
 */
public class CloseTemperatureLayout extends View {

    private Context mContext;
    private Paint mPaint;

    private Path mPath;
    private int mWidth;
    private int mHeight;

    Bitmap originBitmap;

    private int movingY = 0;
    private int mBitmapX;
    private int mBitmapY;

    public CloseTemperatureLayout(Context context) {
        this(context, null);
    }

    public CloseTemperatureLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CloseTemperatureLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        mPath = new Path();
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inSampleSize = 2;
        originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.close_button);
    }

    public void setYValue(int y) {
        movingY = y;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
//            bitmap = Bitmap.createScaledBitmap(originBitmap, mWidth, mWidth * (originBitmap.getHeight() / originBitmap.getWidth()), false);
            mBitmapX = mWidth / 2 - originBitmap.getWidth() / 2;
            mBitmapY = mHeight / 2 - originBitmap.getHeight() / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawBitmap(bitmap, 0, 0, mPaint);
//        canvas.drawColor(Color.CYAN);

        canvas.save();
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        mPath.moveTo(0, 0);
        mPath.lineTo(mWidth, 0);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, movingY);
        mPath.lineTo(0, 0);
        canvas.clipPath(mPath, Region.Op.REPLACE);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(originBitmap, mBitmapX, mBitmapY, mPaint);
        canvas.restore();

        super.onDraw(canvas);
    }
}
