package com.uc.ronrwin.uctopic.manager;

import com.uc.ronrwin.uctopic.Model.base.IBaseEntityBuilder;
import com.uc.ronrwin.uctopic.Model.base.MetaServerData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Entity;

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
public class ParseManager {
    @SuppressWarnings("unchecked")
    public <T extends Object> MetaServerData<T> parseBaseType(JSONObject json) {
        MetaServerData<T> metaServerData = new MetaServerData<>(json);

        metaServerData.data = (T) json.optJSONObject("data").toString();

        return metaServerData;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> MetaServerData<T> parseEntity(JSONObject json, IBaseEntityBuilder builder) {
        MetaServerData<T> metaServerData = new MetaServerData<>(json);

        metaServerData.data = (T) builder.create(json.optJSONObject("data"));

        return metaServerData;
    }

    @SuppressWarnings("unchecked")
    public <T extends Entity> MetaServerData<ArrayList<T>> parseEntityList(JSONObject json, IBaseEntityBuilder builder) {
        MetaServerData<ArrayList<T>> metaServerData = new MetaServerData<>(json);
        ArrayList<T> list = new ArrayList<>();
        JSONArray array = json.optJSONArray("data");
        for (int i = 0; i < array.length(); i++) {
            list.add((T) builder.create(array.optJSONObject(i)));
        }

        metaServerData.data = list;

        return metaServerData;
    }
}
