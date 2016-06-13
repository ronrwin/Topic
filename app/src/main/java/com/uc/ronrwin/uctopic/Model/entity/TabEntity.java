package com.uc.ronrwin.uctopic.model.entity;

import com.uc.ronrwin.uctopic.model.base.IBaseEntityBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p/>
 * 标签类
 * Creation    : 2016/5/18
 * Author      : Ronrwin
 */
public class TabEntity extends Entity implements IBaseEntityBuilder<TabEntity> {
    public int id;
    public int index;
    public String name;

    private static TabEntity mBuilder;

    public TabEntity() {

    }

    public TabEntity(JSONObject json) {
        if (json != null) {
            id = json.optInt("id", 0);
            index = json.optInt("index", 0);
            name = json.optString("name", "");
        }
    }

    public static IBaseEntityBuilder<TabEntity> getBuilder() {
        if (mBuilder == null) {
            mBuilder = new TabEntity();
        }
        return mBuilder;
    }

    @Override
    public TabEntity create(JSONObject jsonObject) {
        return new TabEntity(jsonObject);
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();
        try {
            json.put("id", id);
            json.put("index", index);
            json.put("name", name);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }
}
