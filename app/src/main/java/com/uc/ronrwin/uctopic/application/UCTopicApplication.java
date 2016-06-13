package com.uc.ronrwin.uctopic.application;

import android.app.Application;
import android.content.Context;

import com.uc.ronrwin.uctopic.manager.DataManager;
import com.uc.ronrwin.uctopic.manager.RequestManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p/>
 * Creation    : 2016/5/16
 * Author      : Ronrwin
 */
public class UCTopicApplication extends Application{

    public static Context mContext;
    public static OkHttpClient okHttpClient;
    public static DataManager dataManager;
    public static RequestManager requestManager;

    public UCTopicApplication() {
    }

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        dataManager = new DataManager();
        requestManager = new RequestManager();

        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        super.onCreate();
    }


}
