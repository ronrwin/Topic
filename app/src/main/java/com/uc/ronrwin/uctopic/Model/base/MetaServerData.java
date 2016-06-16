package com.uc.ronrwin.uctopic.model.base;

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
public class MetaServerData {
    public int code;
    public String message;
    public JSONObject metaJson;

    public MetaServerData(JSONObject json) {
        metaJson = json;
        code = json.optInt("code", 1);
        message = json.optString("message", "success");
    }

}
