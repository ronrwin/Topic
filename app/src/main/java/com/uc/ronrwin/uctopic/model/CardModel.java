package com.uc.ronrwin.uctopic.model;

import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.BaseCard;
import com.uc.ronrwin.uctopic.model.entity.TopicCard;
import com.uc.ronrwin.uctopic.model.entity.VideoCard;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public class CardModel {
    public HashMap<String, MetaServerData> mTopicMeta = new HashMap<>();
    public HashMap<String, ArrayList<TopicCard>> mTopicCardListMap = new HashMap<>();
    public ArrayList<VideoCard> mVideoList = new ArrayList<>();
    public MetaServerData mVideoMeta;

    public CardModel() {

    }

    public ArrayList<TopicCard> getTopicList(String key) {
        if (mTopicCardListMap.containsKey(key)) {
            return mTopicCardListMap.get(key);
        }
        return new ArrayList<>();
    }

    public MetaServerData getTopicMeta(String key) {
        if (mTopicMeta.containsKey(key)) {
            return mTopicMeta.get(key);
        }
        return null;
    }

}
