package com.uc.ronrwin.uctopic.manager;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.http.OkHttpUtils;
import com.uc.ronrwin.uctopic.http.ParseUtils;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.TopicCard;
import com.uc.ronrwin.uctopic.ui.fragment.ListFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * 定义各个接口的请求
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public class RequestManager {
//    private HashMap<Integer, Boolean> taskMap = new HashMap<>();
    private static String HOST;

    public RequestManager() {
        HOST = UCTopicApplication.mContext.getString(R.string.url_web_host);
    }

    public void loadTabList(Callback callback) {
        OkHttpUtils.asynGet(HOST + NormalContants.RequestName.GET_TAB, callback);
    }

    public void loadTopicList(Callback callback) {
        OkHttpUtils.asynGet(HOST + NormalContants.RequestName.GET_TOPIC, callback);
    }

    public void loadVideoList(Callback callback) {
        OkHttpUtils.asynGet(HOST + NormalContants.RequestName.GET_VIDEO, callback);
    }
}
