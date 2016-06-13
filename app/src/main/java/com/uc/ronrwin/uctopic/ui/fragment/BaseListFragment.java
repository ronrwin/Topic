package com.uc.ronrwin.uctopic.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.constant.BundleKeys;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.ultra.PtrDefaultHandler;
import com.uc.ronrwin.uctopic.ultra.PtrFrameLayout;
import com.uc.ronrwin.uctopic.ultra.PtrHandler;
import com.uc.ronrwin.uctopic.ultra.PtrIndicator;
import com.uc.ronrwin.uctopic.ultra.PtrTensionIndicator;
import com.uc.ronrwin.uctopic.ultra.PtrUIHandler;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;
import com.uc.ronrwin.uctopic.widget.PercentCircle;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public abstract class BaseListFragment extends Fragment {

    protected View mRootView;
    protected Context mContext;

    protected ViewGroup mLayout;

    protected RecyclerView mRecyclerView;
    protected LinearLayoutManager mLinearLayoutManager;
    protected PtrFrameLayout mFrameLayout;

    protected PtrTensionIndicator mPtrTensionIndicator;

    protected View mHeader;
    protected PercentCircle mPercentCircle;
    protected TextView mRefreshText;

    protected SharedPreferences mRefreshTimePrefrenced;
    protected long mLastUpdate;
    protected final String LAST_UPDATE = "last_update_";

    abstract void refreshLoad();
    abstract void refreshPrepare();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mContext = getActivity();
            mRootView = inflater.inflate(R.layout.fragment_list, container, false);
            mFrameLayout = (PtrFrameLayout) mRootView.findViewById(R.id.material_style_ptr_frame);
            mRecyclerView = (RecyclerView) mRootView.findViewById(R.id.recyclerView);
            mLayout = (ViewGroup) mRootView.findViewById(R.id.list_layout);

            mLinearLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);

            mPtrTensionIndicator = new PtrTensionIndicator();
            mHeader = LayoutInflater.from(mContext).inflate(R.layout.refresh_layout, null);
            mPercentCircle = (PercentCircle) mHeader.findViewById(R.id.circle);
            mRefreshText = (TextView) mHeader.findViewById(R.id.refresh_text);
            mFrameLayout.setPtrIndicator(mPtrTensionIndicator);
            mFrameLayout.setResistance(1.1f);

            mFrameLayout.setLoadingMinTime(1000);
            mFrameLayout.setDurationToCloseHeader(1500);
            mFrameLayout.setHeaderView(mHeader);
            mFrameLayout.addPtrUIHandler(new PtrUIHandler() {
                @Override
                public void onUIReset(PtrFrameLayout frame) {
                }

                @Override
                public void onUIRefreshPrepare(PtrFrameLayout frame) {
                    refreshPrepare();
                }

                @Override
                public void onUIRefreshBegin(PtrFrameLayout frame) {
                    Animation a = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    a.setDuration(500);
                    a.setRepeatCount(Animation.INFINITE);
                    a.setRepeatMode(Animation.INFINITE);
                    mPercentCircle.startAnimation(a);
                }

                @Override
                public void onUIRefreshComplete(PtrFrameLayout frame) {
                    mPercentCircle.clearAnimation();
                }

                @Override
                public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
                    float percent = mPtrTensionIndicator.getOverDragPercent();
                    int currentY = mPtrTensionIndicator.getCurrentPosY();
                    int paddingTop = mHeader.getPaddingTop();
                    if (isUnderTouch) {
                        int smallOffset = (int) (paddingTop * percent);
                        float circlePercent = (float) currentY / (float) (mPercentCircle.getHeight() + smallOffset);
                        int headerNeedScrollY = -mHeader.getMeasuredHeight() + currentY + paddingTop - smallOffset;
                        ViewHelper.setScrollY(mHeader, headerNeedScrollY);
                        ViewHelper.setAlpha(mHeader, percent);
                        mPercentCircle.setPercent(circlePercent);

                        if (circlePercent >= 1f) {
                            int extraHeight = mHeader.getMeasuredHeight() - mRefreshText.getTop();
                            if (headerNeedScrollY < 0) {
                                float alpha = (float) headerNeedScrollY / (float) extraHeight;
                                mPercentCircle.setAlpha(alpha);
                            }
                        }
                    } else {
                        float circlePercent = (float) currentY / (float) mPercentCircle.getHeight();
                        mPercentCircle.setPercent(circlePercent);
                        int headerNeedScrollY = -mHeader.getMeasuredHeight() + currentY;
                        if (circlePercent >= 1f) {
                            int extraHeight = mHeader.getMeasuredHeight() - mRefreshText.getTop();
                            if (headerNeedScrollY < 0) {
                                float alpha = (float) headerNeedScrollY / (float) extraHeight;
                                mPercentCircle.setAlpha(alpha);
                            }
                        }
                    }
                }
            });

            mFrameLayout.setPtrHandler(new PtrHandler() {
                @Override
                public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                    boolean canScroll = PtrDefaultHandler.checkContentCanBePulledDown(frame, mRecyclerView, header);

                    return canScroll;
                }

                @Override
                public void onRefreshBegin(final PtrFrameLayout frame) {
                    refreshLoad();
                }
            });
        } else {
            ViewParent parent = mRootView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mRootView);
            }
        }
        return mRootView;
    }

}
