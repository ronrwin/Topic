package com.uc.ronrwin.uctopic.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;

import com.nineoldandroids.view.ViewHelper;
import com.uc.ronrwin.uctopic.ui.fragment.ListFragment;

import java.util.ArrayList;

public class UCViewPager extends ViewPager {

    private Context mContext;

    public ArrayList<ListFragment> mFragments = new ArrayList<>();


    public UCViewPager(Context context) {
        this(context, null);
    }

    public UCViewPager(Context context, AttributeSet attrs) {
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
        float remain = 0.9f;
        if (mState != State.IDLE) {
            if (left != null) {
                ViewHelper.setPivotX(left, left.getMeasuredWidth());
                ViewHelper.setPivotY(left, left.getMeasuredHeight() / 2);
                ViewHelper.setScaleX(left, remain + (1 - remain) * (1 - positionOffset));
                ViewHelper.setScaleY(left, remain + (1 - remain) * (1 - positionOffset));
                ViewHelper.setScrollX(left, -(int) (left.getMeasuredWidth() * positionOffset / 5));
            }
            if (right != null) {
                ViewHelper.setPivotX(right, 0);
                ViewHelper.setPivotY(right, right.getMeasuredHeight() / 2);
                ViewHelper.setScaleX(right, remain + (1 - remain) * positionOffset);
                ViewHelper.setScaleY(right, remain + (1 - remain) * positionOffset);
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