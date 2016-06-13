package com.uc.ronrwin.uctopic.ui;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewGroupCompat;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.utils.ScreenUtils;

public class WebActivity extends SwipeBackActivity implements View.OnClickListener {

    private Context mContext;

    private ViewGroup mWebLayout;
    private WebView mWebView;

    private boolean isClickBack;

    private ViewGroup mBack;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        overridePendingTransition(0, 0);
//        enterAnimation();
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getWindow().getDecorView().setBackgroundColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
//            getWindow().setStatusBarColor(mContext.getResources().getColor(R.color.black_alpha));
        }
        setContentView(R.layout.activity_web);

        initView();

        mWebLayout.setPadding(0, ScreenUtils.getStatusBarHeight(mContext), 0, 0);

        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            url = bundle.getString("url");
            mWebView.loadUrl(url);
        }
        setResult(Activity.RESULT_OK);
    }

    private void initView() {
        mWebLayout = (ViewGroup) findViewById(R.id.web_layout);
        mWebView = (WebView) findViewById(R.id.webview);
        mBack = (ViewGroup) findViewById(R.id.back);

        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == mBack) {
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        isClickBack = true;
        super.onBackPressed();
    }

    @Override
    public void finish() {
        super.finish();
        if (!isClickBack) {
            overridePendingTransition(0, 0);
        }
    }
}
