package com.uc.ronrwin.uctopic.ui.fragment;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.view.menu.ShowableListMenu;
import android.support.v7.widget.ForwardingListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.http.LoadServerDataListener;
import com.uc.ronrwin.uctopic.http.OkHttpUtils;
import com.uc.ronrwin.uctopic.http.ParseUtils;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.TopicCard;
import com.uc.ronrwin.uctopic.model.entity.VideoCard;
import com.uc.ronrwin.uctopic.utils.PreferencesHelper;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;
import com.uc.ronrwin.uctopic.utils.TimeUtils;
import com.uc.ronrwin.uctopic.widget.ColorPoint;
import com.uc.ronrwin.uctopic.widget.MyLinearLayoutManager;
import com.uc.ronrwin.uctopic.widget.MyVideo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class VideoFragment extends BaseListFragment {

    private Context mContext;
    private VideoRecyclerAdapter mMyRecyclerAdapter;

    private final String SHARE_TAG = "video";

    private ArrayList<VideoCard> mVideos = new ArrayList<>();

    private VH mPlayingVH;
    private int VIDEO_COUNT = 50;


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
        mContext = getActivity();
        mRefreshTimePrefrenced = PreferencesHelper.getSharedPreferences("video_update");

        mLayout.setPadding(0, ScreenUtils.getStatusBarHeight(mContext), 0, 0);
        mRefreshText.setTextColor(mContext.getResources().getColor(R.color.topic_time));
        mMyRecyclerAdapter = new VideoRecyclerAdapter();
//        mRecyclerView.setAdapter(mMyRecyclerAdapter);

        mLayout.setBackgroundColor(Color.BLACK);
        mRecyclerView.setBackgroundResource(R.drawable.infoflow_unlogin_background);
        mFrameLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mFrameLayout.autoRefresh();
            }
        }, 100);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                int firstVisible = mLinearLayoutManager.findFirstVisibleItemPosition();
                int lastVisible = mLinearLayoutManager.findLastVisibleItemPosition();
                int firstCompletelyVisibleItemPosition = mLinearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int lastCompletelyVisibleItemPosition = mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
                if (mPlayingPosition >= 0) {
                    if (mPlayingPosition < firstCompletelyVisibleItemPosition || mPlayingPosition > lastCompletelyVisibleItemPosition) {
                        if (mPlayingVH != null && mPlayingVH.mMyVideo.mPlayer.isPlaying()) {
                            mPlayingVH.mMyVideo.mPlayer.pause();
                            mPlayingVH.mMyVideo.show();
//                            mPlayingVH.mMask.setVisibility(View.GONE);
                            changeAround(mPlayingPosition);
                        }
                    }
                    if (mPlayingPosition < firstVisible || mPlayingPosition > lastVisible) {
                        if (mPlayingVH != null) {
                            mPlayingVH.mMyVideo.reset();
                            mPlayingVH = null;
                            mPlayingPosition = -1;
                            changeAround(mPlayingPosition);
                        }
                    }
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return mRootView;
    }

    @Override
    void refreshPrepare() {
        mLastUpdate = mRefreshTimePrefrenced.getLong(LAST_UPDATE + SHARE_TAG, System.currentTimeMillis());
        mRefreshText.setText(TimeUtils.simplyTime(mLastUpdate));
    }

    @Override
    protected void refreshLoad() {
        UCTopicApplication.dataManager.loadVideoData(new LoadServerDataListener<ArrayList<VideoCard>>() {
            @Override
            public void onFailure(String message) {

            }

            @Override
            public void onSuccess(final ArrayList<VideoCard> data) {
                mVideos = data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mFrameLayout.refreshComplete();
                        mRecyclerView.setAdapter(mMyRecyclerAdapter);
                        mMyRecyclerAdapter.notifyDataSetChanged();

                        mLastUpdate = System.currentTimeMillis();
                        mRefreshTimePrefrenced.edit().putLong(LAST_UPDATE + SHARE_TAG, mLastUpdate).apply();
                        mRefreshText.setText(TimeUtils.simplyTime(mLastUpdate));
                    }
                });
            }
        }, true);
    }

    private int mPlayingPosition = -1;

    class VH extends RecyclerView.ViewHolder {
        MyVideo mMyVideo;
        View mMask;

        VH(View itemView) {
            super(itemView);
            mMyVideo = (MyVideo) itemView.findViewById(R.id.myvideo);
            mMask = itemView.findViewById(R.id.mask);
            mMask.setVisibility(View.GONE);
        }

    }


    private class VideoRecyclerAdapter extends RecyclerView.Adapter<VH> {
        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.video_item_layout, null);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(final VH holder, final int position) {
            final VideoCard video = mVideos.get(position % mVideos.size());
            if (!TextUtils.isEmpty(video.thumbnailUrl)) {
                Picasso.with(mContext).load(video.thumbnailUrl).into(holder.mMyVideo.mThumb);
            }
            holder.mMyVideo.mTitle.setText(video.title);

            holder.mMyVideo.mThumbLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPlayingVH != null) {
                        mPlayingVH.mMyVideo.reset();
                    }

                    mPlayingPosition = position;
                    mPlayingVH = holder;
                    holder.mMyVideo.startLoading();
                    holder.mMyVideo.mPlayer.setVideoURI(Uri.parse(video.videoUrl));
//                    holder.mMask.setVisibility(View.GONE);
                    changeAround(position);
                }
            });

            holder.mMyVideo.mResume.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPlayingPosition = position;
                    holder.mMyVideo.doPauseResume();
                }
            });

//            if (mPlayingPosition >= 0) {
//                if (mPlayingPosition == position) {
//                    holder.mMask.setVisibility(View.GONE);
//                } else {
//                    holder.mMask.setVisibility(View.VISIBLE);
//                }
//            } else {
//                holder.mMask.setVisibility(View.GONE);
//            }
        }

        @Override
        public int getItemCount() {
            return VIDEO_COUNT;
        }
    }

    private void changeAround(int pos) {
//        if (pos - 1 >= 0) {
//            mMyRecyclerAdapter.notifyItemChanged(pos - 1);
//        }
//        if (pos - 2 >= 0) {
//            mMyRecyclerAdapter.notifyItemChanged(pos - 2);
//        }
//        if (pos + 1 < VIDEO_COUNT) {
//            mMyRecyclerAdapter.notifyItemChanged(pos + 1);
//        }
//        if (pos + 2 < VIDEO_COUNT) {
//            mMyRecyclerAdapter.notifyItemChanged(pos + 2);
//        }
    }
}
