package com.uc.ronrwin.uctopic.ui.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.constant.BundleKeys;
import com.uc.ronrwin.uctopic.http.LoadServerDataListener;
import com.uc.ronrwin.uctopic.model.entity.TabEntity;
import com.uc.ronrwin.uctopic.utils.PreferencesHelper;
import com.uc.ronrwin.uctopic.widget.UCViewPager;
import com.uc.ronrwin.uctopic.widget.smarttablayout.SmartTabLayout;
import com.uc.ronrwin.uctopic.widget.smarttablayout.TabView;

import java.util.ArrayList;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class InfoFragment extends Fragment implements View.OnClickListener {

    protected View mRootView;
    protected Context mContext;

    private UCViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;

    private FragmentManager mFragmentManager;

    public ViewGroup mContentLayout;
    private ImageView mChannelSetting;
    private ArrayList<TabEntity> mTabs = new ArrayList<>();

    private ArrayList<ListFragment> mFragments = new ArrayList<>();
    private int mOriginPadding;


    public InfoFragment() {
    }

    public static InfoFragment newInstance(Bundle bundle) {
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private SmartTabLayout.TabProvider mTabProvider = new SmartTabLayout.TabProvider() {
        @Override
        public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
            TabView tabView = new TabView(mContext);
            tabView.title.setText(mTabs.get(position).name);
            if (position == 0) {
//                tabView.title.setText("头条");
                tabView.titleIcon.setVisibility(View.VISIBLE);
                tabView.title.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            } else {
                tabView.setAlpha(0.5f);
                tabView.titleIcon.setVisibility(View.GONE);
            }
            return tabView;
        }
    };


    // TODO：暂时只使用本地的
    private void loadTab() {
        UCTopicApplication.dataManager.loadTabData(new LoadServerDataListener<ArrayList<TabEntity>>() {
            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onSuccess(ArrayList<TabEntity> data) {
                mTabs = data;
                setTabAndFragmentData();
            }
        });

    }

    private void setTabAndFragmentData() {
        mFragments.clear();
        for (int i = 0; i < mTabs.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString(BundleKeys.TITLE, mTabs.get(i).name);
            ListFragment listFragment = ListFragment.newInstance(bundle);
            mFragments.add(listFragment);
        }

        mViewPager.setPageMargin(30);
        mViewPager.setmFragments(mFragments);
        mViewPager.setAdapter(new MyPagerAdapter());
        mSmartTabLayout.setCustomTabView(mTabProvider);
        mSmartTabLayout.setViewPager(mViewPager);
        PreferencesHelper.saveTabData(mTabs.toString());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mContext = getActivity();

            mFragmentManager = getFragmentManager();
            mRootView = inflater.inflate(R.layout.fragment_info, container, false);

            mContentLayout = (ViewGroup) mRootView.findViewById(R.id.info_layout);
            mViewPager = (UCViewPager) mRootView.findViewById(R.id.viewpager);
            mChannelSetting = (ImageView) mRootView.findViewById(R.id.channel_setting);
            mSmartTabLayout = (SmartTabLayout) mRootView.findViewById(R.id.viewpagertab);

            mTabs = UCTopicApplication.dataManager.tabmodel.mTabList;
            setTabAndFragmentData();

            mOriginPadding = mContext.getResources().getDimensionPixelSize(R.dimen.temperature_height);
            setInfoY(0);

            loadTab();
            mChannelSetting.setOnClickListener(this);
        } else {
            ViewParent parent = mRootView.getParent();
            if (parent != null) {
                ((ViewGroup) parent).removeView(mRootView);
            }
        }
        return mRootView;
    }


    public void offsetTopAndBottomY(int distance) {
        mContentLayout.offsetTopAndBottom(distance);
    }

    public float getInfoY() {
        return mContentLayout.getY();
    }

    public void setInfoY(float distance) {
//        ViewHelper.setScrollY(mScrollView, distance);
        mContentLayout.setY(mOriginPadding + distance);
        mViewPager.setPadding(0, 0, 0, mOriginPadding + (int) distance);
    }

    @Override
    public void onClick(View v) {
        if (v == mChannelSetting) {

        }
    }

    public class MyPagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "" + position;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = mFragments.get(position);
            if (!fragment.isAdded()) { // 如果fragment还没有added
                FragmentTransaction ft = mFragmentManager.beginTransaction();
                ft.add(fragment, fragment.getClass().getSimpleName());
                ft.commit();
                /**
                 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
                 * 会在进程的主线程中，用异步的方式来执行。
                 * 如果想要立即执行这个等待中的操作，就要调用这个方法（只能在主线程中调用）。
                 * 要注意的是，所有的回调和相关的行为都会在这个调用中被执行完成，因此要仔细确认这个方法的调用位置。
                 */
                mFragmentManager.executePendingTransactions();
            }

            if (fragment.getView().getParent() == null) {
                container.addView(fragment.getView()); // 为viewpager增加布局
            }

            return fragment.getView();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mFragments.get(position).getView());
        }
    }


}
