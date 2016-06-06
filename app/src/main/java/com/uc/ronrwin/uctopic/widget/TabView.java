package com.uc.ronrwin.uctopic.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uc.ronrwin.uctopic.R;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/2
 * Author      : Ronrwin
 */

public class TabView extends FrameLayout {
    private Context mContext;

    public TitleIcon titleIcon;
    public TextView title;
    public float changeableAlpha = 0.4f;

    public TabView(Context context) {
        this(context, null);
    }

    public TabView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public void setchangeAlpha(float alpha) {
        changeableAlpha = changeableAlpha * (1 - alpha);
        setAlpha(1 - changeableAlpha);
    }

    private void init() {
        View root = LayoutInflater.from(mContext).inflate(R.layout.tab_view, this);
        titleIcon = (TitleIcon) root.findViewById(R.id.tab_logo);
        title = (TextView) root.findViewById(R.id.tab_text);
//        setAlpha(1 - changeableAlpha);
    }
}
