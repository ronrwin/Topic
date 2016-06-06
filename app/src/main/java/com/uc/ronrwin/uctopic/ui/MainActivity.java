package com.uc.ronrwin.uctopic.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.constant.BundleKeys;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.ui.fragment.InfoFragment;
import com.uc.ronrwin.uctopic.ui.fragment.LoginFragment;
import com.uc.ronrwin.uctopic.ui.fragment.VideoFragment;
import com.uc.ronrwin.uctopic.widget.CloseTemperatureLayout;
import com.uc.ronrwin.uctopic.widget.ColorPoint;

public class MainActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext;

    private final long DURATION = 500;
    private final long DIM_DURATION = 500;
    private String mCurrentTag = NormalContants.FragmentTag.TOPIC_TAG;
    FragmentManager mFragmentManager;

    private ViewGroup mTopicTab, mVideoTab, mLoginTab;
    private ImageView mTopicImage, mVideoImage, mLoginImage;
    private ViewGroup mNavigation;

    private float mAnimStartAlpha = -0.5f;

    private View mMainMask;
    private ViewGroup mTemperatureLayout;

    public TextView mTemperature;
    private int mOriginTemperatureHeight;
    private TextView mTemperatureDesc;
    private boolean mOpenFlag = false;
    private ImageView mTemperatureIcon;
    private ViewGroup mLocationLayout;
    private ViewGroup mTemperatureExtra;
    private ViewGroup mWindLayout;
    private ViewGroup mWeahterLayout;
    private ViewGroup mAirLayout;
    private CloseTemperatureLayout mCloseTemperatureLayout;
    private View mTemperatureAnimBackgroud;

    private float mOriginTemperatureY;
    private float mOriginTemperatureDescY;
    private float mOriginLocationLayoutY;
    private float mOriginTemperatureExtraY;
    private float mOriginCloseTemperatureLayoutY;
    private float mOriginTemperatureAnimBackgroudY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(mContext.getResources().getColor(R.color.black_alpha));
        }
        setContentView(R.layout.activity_main);
        mOriginTemperatureHeight = mContext.getResources().getDimensionPixelSize(R.dimen.temperature_height);

        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mNavigation = (ViewGroup) findViewById(R.id.navigation);
        mTopicTab = (ViewGroup) findViewById(R.id.topic_tab);
        mVideoTab = (ViewGroup) findViewById(R.id.video_tab);
        mLoginTab = (ViewGroup) findViewById(R.id.login_tab);
        mTopicImage = (ImageView) findViewById(R.id.topic_img);
        mVideoImage = (ImageView) findViewById(R.id.video_img);
        mLoginImage = (ImageView) findViewById(R.id.login_img);
        mTemperatureDesc = (TextView) findViewById(R.id.temperature_desc);
        mTemperatureIcon = (ImageView) findViewById(R.id.temperature_icon);
        mTemperatureLayout = (ViewGroup) findViewById(R.id.temperature_layout);
        mTemperature = (TextView) findViewById(R.id.temperature);
        mLocationLayout = (ViewGroup) findViewById(R.id.location_layout);
        mTemperatureExtra = (ViewGroup) findViewById(R.id.temperature_extra);
        mWindLayout = (ViewGroup) findViewById(R.id.wind_layout);
        mWeahterLayout = (ViewGroup) findViewById(R.id.weather_layout);
        mAirLayout = (ViewGroup) findViewById(R.id.air_layout);
        mCloseTemperatureLayout = (CloseTemperatureLayout) findViewById(R.id.close_button);
        mTemperatureAnimBackgroud = findViewById(R.id.backgroud);
        mMainMask = findViewById(R.id.main_mask);


        mTopicTab.setOnClickListener(this);
        mVideoTab.setOnClickListener(this);
        mLoginTab.setOnClickListener(this);
        mTemperature.setOnClickListener(this);
        mTemperatureAnimBackgroud.setOnClickListener(this);
        mCloseTemperatureLayout.setOnClickListener(this);

        mTemperatureDesc.post(new Runnable() {
            @Override
            public void run() {
                int halfWidth = mTemperatureLayout.getWidth() / 2;
                mOpenFlag = false;
                mTemperatureDesc.setY(-mTemperatureDesc.getHeight());
                ViewHelper.setScrollY(mTemperatureIcon, mTemperatureIcon.getHeight() / 2);
                mLocationLayout.setY(mOriginTemperatureHeight);
                mTemperatureExtra.setY(mOriginTemperatureHeight);
                mLocationLayout.setAlpha(0);
                mMainMask.setAlpha(0);
                mTemperatureExtra.setAlpha(0);
                mWindLayout.setX(halfWidth / 2 - mWindLayout.getWidth() / 2);
                mWeahterLayout.setX(halfWidth - mWeahterLayout.getWidth() / 2);
                mAirLayout.setX(halfWidth + halfWidth / 2 - mAirLayout.getWidth() / 2);
                mCloseTemperatureLayout.setY(-mCloseTemperatureLayout.getHeight());
                mTemperatureAnimBackgroud.setY(-mTemperatureAnimBackgroud.getHeight() + mCloseTemperatureLayout.getY());


                mOriginTemperatureDescY = mTemperatureDesc.getY();
                mOriginTemperatureY = mTemperature.getY();
                mOriginLocationLayoutY = mLocationLayout.getY();
                mOriginTemperatureExtraY = mTemperatureExtra.getY();
                mOriginCloseTemperatureLayoutY = mCloseTemperatureLayout.getY();
                mOriginTemperatureAnimBackgroudY = mTemperatureAnimBackgroud.getY();
            }
        });

        selectTab(0);
    }

    private Animator valueCloseLayout(final boolean isOpen) {
        ValueAnimator va = new ObjectAnimator();
        va.setObjectValues(new Integer(0));
        va.setInterpolator(new LinearInterpolator());
        va.setEvaluator(new IntEvaluator() {
            @Override
            public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
                if (isOpen) {
                    return (int) (mCloseTemperatureLayout.getHeight() * fraction);
                } else {
                    return (int) (mCloseTemperatureLayout.getHeight() * (1 - fraction));
                }
            }
        });
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer posy = (Integer) animation.getAnimatedValue();
                mCloseTemperatureLayout.setYValue(posy);
            }
        });
        return va;
    }

    private Animator openTemperatureExtra(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", mAnimStartAlpha, 1f);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y",
                    mTemperatureLayout.getHeight() / 4 + mTemperature.getHeight() + mLocationLayout.getHeight());
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mTemperatureExtra, alpha, holder2);
            return a;
        } else {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, mAnimStartAlpha);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginTemperatureExtraY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mTemperatureExtra, alpha, holder2);
            return close;
        }
    }

    private Animator openTemperatyreBackground(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", -mCloseTemperatureLayout.getHeight());
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mTemperatureAnimBackgroud, holder2);
            return a;
        } else {
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginTemperatureAnimBackgroudY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mTemperatureAnimBackgroud, holder2);
            return close;
        }
    }

    private Animator openCloseLayout(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mTemperatureLayout.getHeight() - mCloseTemperatureLayout.getHeight());
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mCloseTemperatureLayout, holder2);
            return a;
        } else {
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginCloseTemperatureLayoutY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mCloseTemperatureLayout, holder2);
            return close;
        }
    }

    private Animator openLocationLayout(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", mAnimStartAlpha, 1f);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mTemperatureLayout.getHeight() / 4 + mTemperature.getHeight());
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mLocationLayout, alpha, holder2);
            return a;
        } else {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, mAnimStartAlpha);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginLocationLayoutY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mLocationLayout, alpha, holder2);
            return close;
        }
    }

    private Animator openTemperature(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.5f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.3f, 1.95f);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mTemperatureLayout.getHeight() / 4);
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mTemperature, scaleX, scaleY, holder2);
            return a;
        } else {
            PropertyValuesHolder scaleX = PropertyValuesHolder.ofFloat("scaleX", 1.5f, 1f);
            PropertyValuesHolder scaleY = PropertyValuesHolder.ofFloat("scaleY", 1.95f, 1.3f);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginTemperatureY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mTemperature, scaleX, scaleY, holder2);
            return close;
        }
    }

    private Animator openTemperatureDesc(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", mAnimStartAlpha, 1f);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mTemperatureLayout.getHeight() / 4 - mTemperatureDesc.getHeight());
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mTemperatureDesc, alpha, holder2);
            return a;
        } else {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, mAnimStartAlpha);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginTemperatureDescY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mTemperatureDesc, alpha, holder2);
            return close;
        }
    }

    private void closeAnim() {
        // 关闭过程
        AnimatorSet set = new AnimatorSet();
        set.play(openTemperature(false))
                .with(openTemperatureDesc(false))
                .with(openLocationLayout(false))
                .with(openTemperatureExtra(false))
                .with(valueCloseLayout(false))
                .with(openCloseLayout(false))
                .with((openTemperatyreBackground(false)));
        set.setDuration(DURATION);
        set.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                ObjectAnimator.ofFloat(mMainMask, "alpha", 1f, 0).setDuration(DIM_DURATION).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        set.start();
    }

    private void openAnim() {
        // 打开过程
        Animator light = ObjectAnimator.ofFloat(mMainMask, "alpha", 0, 1f);
        light.setDuration(DIM_DURATION).start();
        light.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                AnimatorSet set = new AnimatorSet();
                set.setDuration(DURATION);
                set.play(openTemperature(true))
                        .with(openTemperatureDesc(true))
                        .with(openLocationLayout(true))
                        .with(openTemperatureExtra(true))
                        .with(valueCloseLayout(true))
                        .with(openCloseLayout(true))
                        .with((openTemperatyreBackground(true)));
                set.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void transFragment(String tag) {
        Fragment addFragment = mFragmentManager.findFragmentByTag(tag);
        Fragment currentFragment = mFragmentManager.findFragmentByTag(mCurrentTag);

        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();

        if (currentFragment != null && !mCurrentTag.equals(tag)) {
            fragmentTransaction.detach(currentFragment);
//            fragmentTransaction.hide(currentFragment);
        }

        if (addFragment == null) {
            if (tag.equals(NormalContants.FragmentTag.TOPIC_TAG)) {
                fragmentTransaction.add(R.id.main_fragment_stub, InfoFragment.newInstance(null), NormalContants.FragmentTag.TOPIC_TAG);
            } else if (tag.equals(NormalContants.FragmentTag.VIDEO_TAG)) {
                Bundle bundle = new Bundle();
                bundle.putString(BundleKeys.FRAGMENT_TYPE, NormalContants.CardType.VIDEO_CARD);
                fragmentTransaction.add(R.id.main_fragment_stub, VideoFragment.newInstance(bundle), NormalContants.FragmentTag.VIDEO_TAG);
            } else if (tag.equals(NormalContants.FragmentTag.LOGIN_TAG)) {
                fragmentTransaction.add(R.id.main_fragment_stub, LoginFragment.newInstance(null), NormalContants.FragmentTag.LOGIN_TAG);
            }
        } else {
            fragmentTransaction.attach(addFragment);
//            fragmentTransaction.show(addFragment);
        }

        mCurrentTag = tag;
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void selectTab(int index) {
        if (index == 0) {
            mNavigation.setBackgroundColor(Color.WHITE);
            mTopicImage.setImageResource(R.drawable.topic_selector);
            mVideoImage.setImageResource(R.drawable.video_selector);
            mLoginImage.setImageResource(R.drawable.login_selector);

            mTopicTab.setSelected(true);
            mVideoTab.setSelected(false);
            mLoginTab.setSelected(false);
            mTemperatureLayout.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            transFragment(NormalContants.FragmentTag.TOPIC_TAG);
        } else if (index == 1) {
            mNavigation.setBackgroundColor(Color.BLACK);
            mTopicImage.setImageResource(R.drawable.topic_selector_wt);
            mVideoImage.setImageResource(R.drawable.video_selector_wt);
            mLoginImage.setImageResource(R.drawable.login_selector_wt);
            mTopicTab.setSelected(false);
            mVideoTab.setSelected(true);
            mLoginTab.setSelected(false);
            mTemperatureLayout.setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }

            transFragment(NormalContants.FragmentTag.VIDEO_TAG);
        } else if (index == 2) {
            mNavigation.setBackgroundColor(Color.WHITE);
            mTopicImage.setImageResource(R.drawable.topic_selector);
            mVideoImage.setImageResource(R.drawable.video_selector);
            mLoginImage.setImageResource(R.drawable.login_selector);
            mTopicTab.setSelected(false);
            mVideoTab.setSelected(false);
            mLoginTab.setSelected(true);
            mTemperatureLayout.setVisibility(View.GONE);

            if (Build.VERSION.SDK_INT >= 21) {
                getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            }
            transFragment(NormalContants.FragmentTag.LOGIN_TAG);
        }
    }

    @Override
    public void onBackPressed() {
        if (mOpenFlag) {
            mCloseTemperatureLayout.performClick();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        if (v == mTopicTab) {
            selectTab(0);
        } else if (v == mVideoTab) {
            selectTab(1);
        } else if (v == mLoginTab) {
            selectTab(2);
        } else if (v == mTemperature) {
            if (!mOpenFlag) {
                // 先设置标志位，避免多次点击出现重复动画
                mOpenFlag = true;
                openAnim();
            }
        } else if (v == mCloseTemperatureLayout) {
            if (mOpenFlag) {
                // 先设置标志位，避免多次点击出现重复动画
                mOpenFlag = false;
                closeAnim();
            }
        }
    }
}
