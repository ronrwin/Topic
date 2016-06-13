package com.uc.ronrwin.uctopic.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.uc.ronrwin.uctopic.R;

import java.util.Formatter;
import java.util.Locale;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/13
 * Author      : Ronrwin
 */

public class MyVideo extends FrameLayout {
    View mRoot;

    ColorPoint mAnimPoint;
    public ImageView mResume;
    ColorPoint mVideoBgAnim;
    public ImageView mThumb;
    public TextView mTitle;
    ViewGroup mVideoLayout;
    public VideoView mPlayer;
    SeekBar mProgress;
    TextView mEndTime, mCurrentTime;
    ViewGroup mControllerLayout;
    ViewGroup mVideoContainer;
    public ViewGroup mThumbLayout;

    boolean mShowing;
    boolean canAnim = true;

    public MyVideo(Context context) {
        this(context, null);
    }

    public MyVideo(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyVideo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mRoot = LayoutInflater.from(context).inflate(R.layout.video_layout, this);
        mVideoContainer = (ViewGroup) mRoot.findViewById(R.id.video_contaner);
        mPlayer = (VideoView) mRoot.findViewById(R.id.video_view);
        mVideoBgAnim = (ColorPoint) mRoot.findViewById(R.id.video_anim_bg);
        mResume = (ImageView) mRoot.findViewById(R.id.video_resume);
        mAnimPoint = (ColorPoint) mRoot.findViewById(R.id.play_anim);
        mThumb = (ImageView) mRoot.findViewById(R.id.thumb);
        mTitle = (TextView) mRoot.findViewById(R.id.title);
        mVideoLayout = (ViewGroup) mRoot.findViewById(R.id.video_layout);
        mProgress = (SeekBar) mRoot.findViewById(R.id.mediacontroller_progress);
        mCurrentTime = (TextView) mRoot.findViewById(R.id.time_current);
        mEndTime = (TextView) mRoot.findViewById(R.id.time);
        mControllerLayout = (ViewGroup) mRoot.findViewById(R.id.controller_layout);
        mThumbLayout = (ViewGroup) mRoot.findViewById(R.id.thumb_layout);

        stopLoadingPoint();
        mProgress.setOnSeekBarChangeListener(mSeekListener);
        mControllerLayout.setVisibility(View.GONE);
        mProgress.setMax(1000);

        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());

        mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                reset();
            }
        });

        mPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                reset();
                return false;
            }
        });

        mPlayer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                if (action == MotionEvent.ACTION_DOWN) {
                    if (!mShowing) {
                        setProgress();
                        updatePausePlay();
                        show(sDefaultTimeout);
                    } else {
                        hide();
                    }
                }
                return false;
            }
        });

        mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mPlayer.setVisibility(View.VISIBLE);
                mThumbLayout.setVisibility(View.GONE);
                mPlayer.requestFocus();
                mp.start();
                stopLoadingPoint();
            }
        });
    }


    public void reset() {
        mTitle.setVisibility(View.VISIBLE);
        mPlayer.suspend();
        mPlayer.setVideoURI(null);
        mThumbLayout.setVisibility(View.VISIBLE);
        stopLoadingPoint();
        hide();
        canAnim = true;
    }

    StringBuilder mFormatBuilder;
    Formatter mFormatter;

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void animLoadingPoint() {
        if (canAnim) {
            mAnimPoint.setVisibility(View.VISIBLE);
            Animation a = new ScaleAnimation(0.5f, 1f, 0.5f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            a.setDuration(200);
            a.setRepeatCount(Animation.INFINITE);
            a.setRepeatMode(Animation.REVERSE);
            mAnimPoint.startAnimation(a);
        }
    }

    private void stopLoadingPoint() {
        mAnimPoint.setVisibility(View.GONE);
        mVideoBgAnim.setScaleX(0f);
        mVideoBgAnim.setScaleY(0f);
        mAnimPoint.setVisibility(View.GONE);
        mAnimPoint.clearAnimation();

    }

    private void animBg() {
        if (canAnim) {
            canAnim = false;
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 0f, 20f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 0f, 20f);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mVideoBgAnim, scaleX, scaleY);
            close.setDuration(500);
            close.start();
        }
    }

    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            int pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    mControllerLayout.setVisibility(View.VISIBLE);
                    pos = setProgress();
                    if (!mDragging && mShowing && mPlayer.isPlaying()) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                    }
                    break;
            }
        }
    };

    public void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private void updatePausePlay() {
        mResume.setVisibility(View.VISIBLE);
        if (mPlayer.isPlaying()) {
            mResume.setImageResource(R.drawable.icon_video_pause);
        } else {
            mResume.setImageResource(R.drawable.icon_video_resume);
        }
    }

    private int setProgress() {
        if (mPlayer == null || mDragging) {
            return 0;
        }
        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                // use long to avoid overflow
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        if (mEndTime != null)
            mEndTime.setText(stringForTime(duration));
        if (mCurrentTime != null)
            mCurrentTime.setText(stringForTime(position));

        return position;
    }

    private static final int sDefaultTimeout = 3000;
    boolean mDragging;
    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;
            mHandler.removeMessages(SHOW_PROGRESS);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = mPlayer.getDuration();
            long newposition = (duration * progress) / 1000L;
            mPlayer.seekTo((int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime((int) newposition));
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);
        }
    };

    public void show() {
        show(sDefaultTimeout);
    }

    public void show(int timeout) {
        if (!mShowing) {
            setProgress();
            mShowing = true;
        }
        updatePausePlay();

        // cause the progress bar to be updated even if mShowing
        // was already true.  This happens, for example, if we're
        // paused with the progress bar showing the user hits play.
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            Message msg = mHandler.obtainMessage(FADE_OUT);
            mHandler.sendMessageDelayed(msg, timeout);
        }
    }

    public void hide() {
        if (mShowing) {
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                mControllerLayout.setVisibility(View.GONE);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    public void startLoading() {
        animLoadingPoint();
        animBg();
        mTitle.setVisibility(View.GONE);
    }
}
