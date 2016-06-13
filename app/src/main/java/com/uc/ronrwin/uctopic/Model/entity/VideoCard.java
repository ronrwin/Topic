package com.uc.ronrwin.uctopic.model.entity;

import com.uc.ronrwin.uctopic.model.base.IBaseEntityBuilder;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p/>
 * 视频类型卡片
 * Creation    : 2016/5/18
 * Author      : Ronrwin
 */
public class VideoCard extends BaseCard implements IBaseEntityBuilder<VideoCard> {
    public int commentNum;
    public long duration;
    public int followNum;
    public String thumbnailUrl;
    public String videoUrl;

    private static VideoCard mBuilder;
    public boolean isPlaying = false;

    public VideoCard() {
    }

    public VideoCard(JSONObject json) {
        super(json);
    }

    @Override
    public void parseExtra(JSONObject json) {
        if (json != null) {
            commentNum = json.optInt("commentNum", 0);
            duration = json.optLong("duration", 0L);
            followNum = json.optInt("followNum", 0);
            thumbnailUrl = json.optString("thumbnailUrl", "");
            videoUrl = json.optString("videoUrl", "");
        }
    }

    @Override
    public VideoCard create(JSONObject json) {
        return new VideoCard(json);
    }

    public static IBaseEntityBuilder<VideoCard> getBuilder() {
        if (mBuilder == null) {
            mBuilder = new VideoCard();
        }
        return mBuilder;
    }

    @Override
    public String toString() {
        JSONObject json = new JSONObject();

        try {
            json.put("typeId", typeId);
            json.put("title", title);
            json.put("createtime", createtime);
            json.put("duration", duration);
            json.put("followNum", followNum);
            json.put("thumbnailUrl", thumbnailUrl);
            json.put("videoUrl", videoUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json.toString();
    }

}
