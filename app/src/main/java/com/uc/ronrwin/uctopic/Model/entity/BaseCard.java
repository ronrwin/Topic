package com.uc.ronrwin.uctopic.model.entity;

import org.json.JSONObject;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p/>
 * Creation    : 2016/5/18
 * Author      : Ronrwin
 */
public abstract class BaseCard extends Entity{
    public int typeId;
    public String title;
    public long createtime;

    public BaseCard() {}

    public BaseCard(JSONObject json) {
        typeId = json.optInt("typeId", 0);
        title = json.optString("title", "");
        createtime = json.optLong("createtime", 0L);
        parseExtra(json);
    }

    public abstract void parseExtra(JSONObject json);

}
