package com.uc.ronrwin.uctopic.widget.smarttablayout;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Region;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.uc.ronrwin.uctopic.R;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/1
 * Author      : Ronrwin
 */

public class TitleIcon extends View {

    private Context mContext;
    private Paint mPaint;
    private Paint mFilterPaint;

    private Path mPath;
    private int mWidth;
    private int mHeight;

    Bitmap originBitmap;
    Bitmap bitmap;

    private int movingX;

    public TitleIcon(Context context) {
        this(context, null);
    }

    public TitleIcon(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPaint = new Paint();
        mFilterPaint = new Paint();
        mFilterPaint.setColorFilter(new PorterDuffColorFilter(Color.BLACK, PorterDuff.Mode.MULTIPLY));
        mPath = new Path();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 2;
        originBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.channel_scrolltab_logo, options);
    }

    public void setChangeAnim(float percent) {
        movingX = (int)(mWidth * percent * 1.7f);
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mWidth == 0) {
            mWidth = getMeasuredWidth();
            mHeight = getMeasuredHeight();
            bitmap = Bitmap.createScaledBitmap(originBitmap, mWidth, mWidth * (originBitmap.getHeight() / originBitmap.getWidth()), false);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        canvas.drawColor(Color.CYAN);
        canvas.drawBitmap(bitmap, 0, 0, mPaint);

        canvas.save();
        mPath.reset();
        canvas.clipPath(mPath); // makes the clip empty
        mPath.moveTo(0, 0);
        mPath.lineTo(0, movingX);
        mPath.lineTo(movingX, 0);
        mPath.lineTo(0, 0);
        canvas.clipPath(mPath, Region.Op.REPLACE);
        canvas.drawBitmap(bitmap, 0, 0, mFilterPaint);
        canvas.restore();

        super.onDraw(canvas);
    }
}
