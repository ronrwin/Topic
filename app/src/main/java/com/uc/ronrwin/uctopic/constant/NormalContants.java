package com.uc.ronrwin.uctopic.constant;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/6/2
 * Author      : Ronrwin
 */

public class NormalContants {
    public static int REFRESG_EXTRA = 40;


    public interface FragmentTag {
        String TOPIC_TAG = "topic_tag";
        String VIDEO_TAG = "video_tag";
        String LOGIN_TAG = "login_tag";
    }

    public interface CardType {
        int TOPIC_CARD_1 = 1;
        int TOPIC_CARD_2 = 2;
        int TOPIC_CARD_3 = 3;
        String VIDEO_CARD = "viceo_card";
    }

    public interface RequestCode {
        int LOAD_TAB = 1001;
        int GET_TOPIC = 1002;
    }

    public interface RequestName {
        String GET_TAB = "getTabDatas";
        String GET_TOPIC = "getTopics";
        String GET_VIDEO = "getVideos";
    }

}
