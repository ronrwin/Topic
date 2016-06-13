package com.uc.ronrwin.uctopic.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.view.ViewHelper;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.constant.BundleKeys;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.http.OkHttpUtils;
import com.uc.ronrwin.uctopic.model.TabModel;
import com.uc.ronrwin.uctopic.model.TransitionEvent;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.TabEntity;
import com.uc.ronrwin.uctopic.ui.fragment.InfoFragment;
import com.uc.ronrwin.uctopic.ui.fragment.LoginFragment;
import com.uc.ronrwin.uctopic.ui.fragment.VideoFragment;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;
import com.uc.ronrwin.uctopic.widget.CloseTemperatureLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

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

    public ViewGroup mMainLayout;
    private View mMainMask;
    public ViewGroup mTemperatureLayout;

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
    private View mTransitionView;
    private ViewGroup mTransitionLayout;
    private ImageView mTemperatureNum;

    private float mOriginTemperatureY;
    private float mOriginTemperatureDescY;
    private float mOriginLocationLayoutY;
    private float mOriginTemperatureExtraY;
    private float mOriginCloseTemperatureLayoutY;
    private float mOriginTemperatureAnimBackgroudY;
    private float mOriginTemperatureNumY;

    public boolean mLoginNormalShow = true;

    private int mChangeableHeight;

//    private TabModel mTabModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        EventBus.getDefault().register(this);

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//            getWindow().setStatusBarColor(mContext.getResources().getColor(R.color.black_alpha));
        }
        setContentView(R.layout.activity_main);
        mOriginTemperatureHeight = mContext.getResources().getDimensionPixelSize(R.dimen.temperature_height);
        mChangeableHeight = ScreenUtils.getStatusBarHeight(mContext) - mOriginTemperatureHeight;

        initView();
    }

    private void initView() {
        mFragmentManager = getSupportFragmentManager();
        mMainLayout = (ViewGroup) findViewById(R.id.main_layout);
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
        mTransitionView = findViewById(R.id.transition_view);
        mTransitionLayout = (ViewGroup) findViewById(R.id.transition_layout);
        mTemperatureNum = (ImageView) findViewById(R.id.temperature_num);

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
                mLocationLayout.setAlpha(mAnimStartAlpha);
                mMainMask.setAlpha(mAnimStartAlpha);
                mTemperatureExtra.setAlpha(mAnimStartAlpha);
                mTemperatureNum.setAlpha(mAnimStartAlpha);
                mWindLayout.setX(halfWidth / 2 - mWindLayout.getWidth() / 2);
                mWeahterLayout.setX(halfWidth - mWeahterLayout.getWidth() / 2);
                mAirLayout.setX(halfWidth + halfWidth / 2 - mAirLayout.getWidth() / 2);
                mCloseTemperatureLayout.setY(-mCloseTemperatureLayout.getHeight());
                mTemperatureAnimBackgroud.setY(-mTemperatureAnimBackgroud.getHeight() + mCloseTemperatureLayout.getY());
                mTemperatureNum.setY(mOriginTemperatureHeight);

                mOriginTemperatureDescY = mTemperatureDesc.getY();
                mOriginTemperatureY = mTemperature.getY();
                mOriginLocationLayoutY = mLocationLayout.getY();
                mOriginTemperatureExtraY = mTemperatureExtra.getY();
                mOriginCloseTemperatureLayoutY = mCloseTemperatureLayout.getY();
                mOriginTemperatureAnimBackgroudY = mTemperatureAnimBackgroud.getY();
                mOriginTemperatureNumY = mTemperatureNum.getY();

                mTemperatureNum.getLayoutParams().height = mTemperatureLayout.getHeight() - mTemperatureLayout.getHeight() / 4
                        - mCloseTemperatureLayout.getHeight()
                        - mTemperatureExtra.getHeight() -mLocationLayout.getHeight() - mTemperature.getHeight();
                mTemperatureNum.requestLayout();
            }
        });

        selectTab(0);
    }

    public float getTemperatureTopY() {
        return mTemperature.getY();
    }

    public void setTemperatureTopY(float distance) {
        mTemperatureIcon.setY(distance);
        mTemperature.setY(distance);

        float offset = 1 - distance / mChangeableHeight;
        mTemperature.setAlpha(offset);
        mTemperature.setScaleX(0.7f + 0.3f * offset);
        mTemperature.setScaleY(1.3f * (0.7f + 0.3f * offset));
    }

    public void startTransition(View origintView, final String url) {
        mTransitionLayout.setVisibility(View.VISIBLE);
        mTransitionLayout.setAlpha(1f);
        int height = origintView.getMeasuredHeight();
        int mainHeight = mMainLayout.getHeight();
        float scaleY = (float) mMainLayout.getHeight() / (float) height;
        int[] locations = new int[2];
        origintView.getLocationInWindow(locations);
        mTransitionView.setY(locations[1]);
        mTransitionView.getLayoutParams().height = height;
        mTransitionView.requestLayout();

        PropertyValuesHolder scale = PropertyValuesHolder.ofFloat("scaleY", 1f, scaleY);
        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", locations[1], mainHeight / 2 - height / 2);
        Animator a = ObjectAnimator.ofPropertyValuesHolder(mTransitionView, scale, holder2);
        a.setDuration(500);
        a.start();
        a.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent i = new Intent(mContext, WebActivity.class);
                i.putExtra("url", url);
                MainActivity.this.startActivityForResult(i, 1000);

//                mTransitionLayout.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            mTransitionLayout.setVisibility(View.GONE);
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    private Animator openTemperatureNum(boolean isOpen) {
        if (isOpen) {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", mAnimStartAlpha, 1f);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y",
                    mTemperatureLayout.getHeight() / 4 + mTemperature.getHeight() + mLocationLayout.getHeight() + mTemperatureExtra.getHeight());
            Animator a = ObjectAnimator.ofPropertyValuesHolder(mTemperatureNum, alpha, holder2);
            return a;
        } else {
            PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1f, mAnimStartAlpha);
            PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("Y", mOriginTemperatureNumY);
            Animator close = ObjectAnimator.ofPropertyValuesHolder(mTemperatureNum, alpha, holder2);
            return close;
        }
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
                .with(openTemperatureNum(false))
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
                        .with(openTemperatureNum(true))
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
//            fragmentTransaction.detach(currentFragment);
            fragmentTransaction.hide(currentFragment);
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
//            fragmentTransaction.attach(addFragment);
            fragmentTransaction.show(addFragment);
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
            transFragment(NormalContants.FragmentTag.LOGIN_TAG);
        }
    }

    @Override
    public void onBackPressed() {
        if (mOpenFlag) {
            mCloseTemperatureLayout.performClick();
            return;
        } else if (!mLoginNormalShow) {
            if (mCurrentTag.equals(NormalContants.FragmentTag.LOGIN_TAG)) {
                LoginFragment currentFragment = (LoginFragment) mFragmentManager.findFragmentByTag(mCurrentTag);
                currentFragment.changeLoginNormalAnim();
            }
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

    @Subscribe(threadMode = ThreadMode.MainThread)
    public void hideTransitionLayout(TransitionEvent event){
        mTransitionLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
