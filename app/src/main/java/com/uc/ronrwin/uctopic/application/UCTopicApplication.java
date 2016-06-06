package com.uc.ronrwin.uctopic.application;

import android.app.Application;
import android.content.Context;

import com.uc.ronrwin.uctopic.manager.ThreadManager;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

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
    public static ThreadManager threadManager;

    public UCTopicApplication() {
    }

    @Override
    public void onCreate() {
        mContext = getApplicationContext();
        okHttpClient = new OkHttpClient();
        threadManager = new ThreadManager();
        super.onCreate();
    }


}
