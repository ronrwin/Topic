package com.uc.ronrwin.uctopic.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.widget.JazzyViewPager;
import com.uc.ronrwin.uctopic.smarttablayout.SmartTabLayout;
import com.uc.ronrwin.uctopic.widget.TabView;

import java.util.ArrayList;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class InfoFragment extends Fragment {

    protected View mRootView;
    protected Context mContext;

    private JazzyViewPager mViewPager;
    private SmartTabLayout mSmartTabLayout;

    private FragmentManager mFragmentManager;

    // todo: 为了使子控件能够调用，直接采用public，以后有时间再改为接口处理
    public FrameLayout mScrollView;

    public float currentY;

    private ArrayList<ListFragment> mFragments = new ArrayList<>();
    private int[] colors;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(Bundle bundle) {
        InfoFragment fragment = new InfoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mContext = getActivity();

            mFragmentManager = getFragmentManager();
            mRootView = inflater.inflate(R.layout.fragment_info, container, false);
            colors = mContext.getResources().getIntArray(R.array.custom_tab_colors);

            for (int i = 0; i < 14; i++) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "" + i);
                bundle.putInt("color", colors[i % colors.length]);
                bundle.putInt("bgcolor", colors[(i + 2) % colors.length]);
                ListFragment listFragment = ListFragment.newInstance(bundle);
                mFragments.add(listFragment);
            }

            mScrollView = (FrameLayout) mRootView.findViewById(R.id.scrollView);
            mViewPager = (JazzyViewPager) mRootView.findViewById(R.id.viewpager);
            mViewPager.setmFragments(mFragments);
            mSmartTabLayout = (SmartTabLayout) mRootView.findViewById(R.id.viewpagertab);
            mViewPager.setAdapter(new MyPagerAdapter());
            mViewPager.setPageMargin(30);
            mSmartTabLayout.setCustomTabView(new SmartTabLayout.TabProvider() {
                @Override
                public View createTabView(ViewGroup container, int position, PagerAdapter adapter) {
                    TabView tabView = new TabView(mContext);
                    StringBuilder sb = new StringBuilder("");
                    int len = position % 4 + 1;
                    for (int i = 0; i < len; i++) {
                        sb.append("" + i);
                    }
                    tabView.title.setText(sb.toString());
                    if (position == 0) {
                        tabView.title.setText("头条");
                        tabView.titleIcon.setVisibility(View.VISIBLE);
                    } else {
                        tabView.titleIcon.setVisibility(View.GONE);
                    }
                    return tabView;
                }
            });
            mSmartTabLayout.setViewPager(mViewPager);
            mSmartTabLayout.setOnTabClickListener(new SmartTabLayout.OnTabClickListener() {
                @Override
                public void onTabClicked(int position) {

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
