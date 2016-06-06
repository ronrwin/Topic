package com.uc.ronrwin.uctopic.Model.base;

import org.json.JSONObject;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p/>
 * 网络请求元数据处理
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public class MetaServerData<T> {
    public int code;
    public String message;
    public JSONObject metaJson;

    public T data;

    public static final int OK = 1;
    public static final int FAIL = -1;

    public MetaServerData(JSONObject json) {
        metaJson = json;
        code = json.optInt("code", 1);
        message = json.optString("message", "success");
    }

}
