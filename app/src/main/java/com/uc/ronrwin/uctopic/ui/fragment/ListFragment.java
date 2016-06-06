package com.uc.ronrwin.uctopic.ui.fragment;

import android.content.Context;
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
import com.uc.ronrwin.uctopic.constant.BundleKeys;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;
import com.uc.ronrwin.uctopic.widget.PercentCircle;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.ultra.PtrDefaultHandler;
import com.uc.ronrwin.uctopic.ultra.PtrFrameLayout;
import com.uc.ronrwin.uctopic.ultra.PtrHandler;
import com.uc.ronrwin.uctopic.ultra.PtrIndicator;
import com.uc.ronrwin.uctopic.ultra.PtrTensionIndicator;
import com.uc.ronrwin.uctopic.ultra.PtrUIHandler;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class ListFragment extends BaseListFragment {

    private MyRecyclerAdapter mMyRecyclerAdapter;
//
    public ListFragment() {
    }

    public static ListFragment newInstance(Bundle bundle) {
        ListFragment fragment = new ListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMyRecyclerAdapter = new MyRecyclerAdapter();
        mRecyclerView.setAdapter(mMyRecyclerAdapter);

        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
    }

    class VH extends RecyclerView.ViewHolder {
        public VH(View itemView) {
            super(itemView);
        }
    }

    class MyRecyclerAdapter extends RecyclerView.Adapter<VH> {
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.default_item_layout, null);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

}
