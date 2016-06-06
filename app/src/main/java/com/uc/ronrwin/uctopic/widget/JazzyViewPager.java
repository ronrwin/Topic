package com.uc.ronrwin.uctopic.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Camera;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.ui.fragment.InfoFragment;
import com.uc.ronrwin.uctopic.ui.fragment.ListFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class JazzyViewPager extends ViewPager {

    private Context mContext;

    public ArrayList<ListFragment> mFragments = new ArrayList<>();


    public JazzyViewPager(Context context) {
        this(context, null);
    }

    public JazzyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public void setmFragments(ArrayList<ListFragment> fragments) {
        mFragments = fragments;
    }

    private State mState;
    private int oldPage;

    private View mLeft;
    private View mRight;

    private enum State {
        IDLE,
        GOING_LEFT,
        GOING_RIGHT
    }

    private void animateAccordion(View left, View right, float positionOffset) {
        if (mState != State.IDLE) {
            if (left != null) {
                ViewHelper.setPivotX(left, left.getMeasuredWidth());
                ViewHelper.setPivotY(left, 0);
                ViewHelper.setScrollX(left, -(int) (left.getMeasuredWidth() * positionOffset / 5));
            }
            if (right != null) {
                ViewHelper.setPivotX(right, 0);
                ViewHelper.setPivotY(right, 0);
                ViewHelper.setScrollX(right, (int) (right.getMeasuredWidth() * (1 - positionOffset) / 5));
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mState == State.IDLE && positionOffset > 0) {
            oldPage = getCurrentItem();
            mState = position == oldPage ? State.GOING_RIGHT : State.GOING_LEFT;
        }
        boolean goingRight = position == oldPage;
        if (mState == State.GOING_RIGHT && !goingRight)
            mState = State.GOING_LEFT;
        else if (mState == State.GOING_LEFT && goingRight)
            mState = State.GOING_RIGHT;


        float effectOffset = isSmall(positionOffset) ? 0 : positionOffset;

        if (mFragments.size() > 0 && position < mFragments.size() - 1) {
            mLeft = mFragments.get(position).getView();
            mRight = mFragments.get(position + 1).getView();
            animateAccordion(mLeft, mRight, effectOffset);
        }

        super.onPageScrolled(position, positionOffset, positionOffsetPixels);

        if (effectOffset == 0) {
            mState = State.IDLE;
        }

    }

    private boolean isSmall(float positionOffset) {
        return Math.abs(positionOffset) < 0.0001;
    }

}