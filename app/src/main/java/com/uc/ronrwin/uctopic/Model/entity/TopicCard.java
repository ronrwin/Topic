package com.uc.ronrwin.uctopic.Model.entity;

import com.uc.ronrwin.uctopic.Model.base.IBaseEntityBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p/>
 * 新闻类型卡片
 * Creation    : 2016/5/18
 * Author      : Ronrwin
 */
public class TopicCard extends BaseCard implements IBaseEntityBuilder<TopicCard> {
    public String url;
    public ArrayList<String> images;
    public String fromMedia;

    private static TopicCard mBuilder;

    public TopicCard() {

    }

    public TopicCard(JSONObject json) {
        super(json);
    }

    @Override
    public void parseExtra(JSONObject json) {
        if (json != null) {
            url = json.optString("url", "");
            fromMedia = json.optString("fromMedia", "");
            String imagesStr = json.optString("images", "");
            images = new ArrayList<>();
            String[] imgs = imagesStr.split("\\|\\|");
            for (String img : imgs) {
                images.add(img);
            }
        }
    }

    public static IBaseEntityBuilder<TopicCard> getBuilder() {
        if (mBuilder == null) {
            mBuilder = new TopicCard();
        }
        return mBuilder;
    }

    @Override
    public TopicCard create(JSONObject jsonObject) {
        return new TopicCard(jsonObject);
    }
}
