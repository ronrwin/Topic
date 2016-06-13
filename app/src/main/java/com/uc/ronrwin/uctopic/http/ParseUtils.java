package com.uc.ronrwin.uctopic.http;

import com.uc.ronrwin.uctopic.model.base.IBaseEntityBuilder;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.Entity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * 实体解析管理器
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public class ParseUtils {
    @SuppressWarnings("unchecked")
    public static <T extends Object> T parseBaseType(JSONObject json) {

        return (T) json.optJSONObject("data").toString();
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> T parseEntity(JSONObject json, IBaseEntityBuilder builder) {
        return (T) builder.create(json.optJSONObject("data"));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Entity> ArrayList<T> parseEntityList(JSONObject json, IBaseEntityBuilder builder) {
        ArrayList<T> list = new ArrayList<>();
        JSONArray array = json.optJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            list.add((T) builder.create(array.optJSONObject(i)));
        }

        return list;
    }
}
