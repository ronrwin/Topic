package com.uc.ronrwin.uctopic.http;

import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.utils.ThreadUtils;

import java.util.List;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/25
 * Author      : Ronrwin
 */
public class OkHttpUtils {
    public static void asynGet(final String url, final Callback callback) {
        ThreadUtils.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                UCTopicApplication.okHttpClient.newCall(request).enqueue(callback);
            }
        });
    }

    public static RequestBody formBody(List<BasicNameValuePair> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (BasicNameValuePair pair : params) {
            builder.add(pair.name, pair.value);
        }
        return builder.build();
    }

    public static void asynPost(final String url, final List<BasicNameValuePair> params, final Callback callback) {
        ThreadUtils.getExecutorService().execute(new Runnable() {
            @Override
            public void run() {
                RequestBody formBody = formBody(params);
                Request request = new Request.Builder()
                        .url(url)
                        .post(formBody)
                        .build();
                UCTopicApplication.okHttpClient.newCall(request).enqueue(callback);
            }
        });
    }

}
