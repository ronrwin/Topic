package com.uc.ronrwin.uctopic.ui;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.TextView;

import com.uc.ronrwin.uctopic.R;

public class TemperatureActivity extends FragmentActivity implements View.OnClickListener {

    private Context mContext;

    private TextView mTemperature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(mContext.getResources().getColor(R.color.black_alpha));
        }
        setContentView(R.layout.activity_temperature);

        initView();
    }

    private void initView() {
        mTemperature = (TextView) findViewById(R.id.temperature);
        ViewCompat.setTransitionName(mTemperature, "temperature");
    }

    @Override
    public void onClick(View v) {

    }

}
