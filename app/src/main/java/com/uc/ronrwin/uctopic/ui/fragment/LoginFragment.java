package com.uc.ronrwin.uctopic.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.ui.MainActivity;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class LoginFragment extends Fragment implements View.OnClickListener{

    protected View mRootView;
    protected Context mContext;

    private ViewGroup mBackgroud;
    private ViewGroup mCircleLayout;
    private TextView mLoginTip;

    public LoginFragment() {
    }


    public static LoginFragment newInstance(Bundle bundle) {
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mContext = getActivity();
            mRootView = inflater.inflate(R.layout.fragment_login, container, false);
            mBackgroud = (ViewGroup) mRootView.findViewById(R.id.logo_backgroud);
            mCircleLayout = (ViewGroup) mRootView.findViewById(R.id.circle_layout);
            mLoginTip = (TextView) mRootView.findViewById(R.id.login_tip);
            DisplayMetrics displayMetrics = ScreenUtils.screenDisplayMetrics(mContext);
            if (displayMetrics.heightPixels > displayMetrics.widthPixels) {
                mBackgroud.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, displayMetrics.widthPixels));
            }

            mCircleLayout.setOnClickListener(this);

            mBackgroud.post(new Runnable() {
                @Override
                public void run() {
                    mCircleLayout.setY(mBackgroud.getHeight() - mCircleLayout.getHeight() / 2);
                    mLoginTip.setY(mBackgroud.getHeight());
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

    public void changeLoginNormalAnim() {
        if (((MainActivity)getActivity()).mLoginNormalShow) {
            ((MainActivity)getActivity()).mLoginNormalShow = false;
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.1f, 0f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.1f, 0f);
            Animator small = ObjectAnimator.ofPropertyValuesHolder(mCircleLayout, scaleX, scaleY);
            small.setDuration(500);
            small.start();
        } else {
            ((MainActivity)getActivity()).mLoginNormalShow = true;
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 1f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 1f);
            Animator bigger = ObjectAnimator.ofPropertyValuesHolder(mCircleLayout, scaleX, scaleY);
            bigger.setDuration(500);
            bigger.start();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == mCircleLayout) {
            changeLoginNormalAnim();
        }
    }


}
