package com.uc.ronrwin.uctopic.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;
import com.uc.ronrwin.uctopic.widget.ColorPoint;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class VideoFragment extends BaseListFragment {

    private VideoRecyclerAdapter mMyRecyclerAdapter;

    public VideoFragment() {
    }

    public static VideoFragment newInstance(Bundle bundle) {
        VideoFragment fragment = new VideoFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        mLayout.setPadding(0, ScreenUtils.getStatusBarHeight(mContext), 0, 0);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mMyRecyclerAdapter = new VideoRecyclerAdapter();
        mRecyclerView.setAdapter(mMyRecyclerAdapter);


        return mRootView;
    }

    class VH extends RecyclerView.ViewHolder {

        public ColorPoint mAnimPoint;
        public ImageView mResume;
        public ColorPoint mVideoBgAnim;

        public VH(View itemView) {
            super(itemView);
            mVideoBgAnim = (ColorPoint) itemView.findViewById(R.id.video_anim_bg);
            mResume = (ImageView) itemView.findViewById(R.id.video_resume);
            mAnimPoint = (ColorPoint) itemView.findViewById(R.id.play_anim);
        }
    }

    class VideoRecyclerAdapter extends RecyclerView.Adapter<VH> {
        private Animator loading;
        private Animator.AnimatorListener mAnimatorListener;

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.video_item_layout, null);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            holder.mAnimPoint.setVisibility(View.GONE);
            holder.mResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.mAnimPoint.setVisibility(View.VISIBLE);
                    holder.mResume.setVisibility(View.GONE);
                    animBg(holder);
                    Animation a = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                            Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    a.setDuration(200);
                    a.setRepeatCount(Animation.INFINITE);
                    a.setRepeatMode(Animation.REVERSE);
                    holder.mAnimPoint.startAnimation(a);

                    new Handler(new Handler.Callback() {
                        @Override
                        public boolean handleMessage(Message msg) {
                            holder.mVideoBgAnim.setScaleX(0f);
                            holder.mVideoBgAnim.setScaleY(0f);
                            holder.mAnimPoint.setVisibility(View.GONE);
                            holder.mAnimPoint.clearAnimation();
                            holder.mResume.setVisibility(View.VISIBLE);
                            return false;
                        }
                    }).sendEmptyMessageDelayed(0, 2000);
                }
            });

        }

        private void animBg(VH holder) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 20f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 20f);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(holder.mVideoBgAnim, scaleX, scaleY);
            close.setDuration(500);
            close.start();
        }

        @Override
        public int getItemCount() {
            return 50;
        }
    }

}
