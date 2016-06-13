package com.uc.ronrwin.uctopic.manager;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.http.LoadServerDataListener;
import com.uc.ronrwin.uctopic.http.OkHttpUtils;
import com.uc.ronrwin.uctopic.http.ParseUtils;
import com.uc.ronrwin.uctopic.model.TabModel;
import com.uc.ronrwin.uctopic.model.CardModel;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.TabEntity;
import com.uc.ronrwin.uctopic.model.entity.TopicCard;
import com.uc.ronrwin.uctopic.model.entity.VideoCard;
import com.uc.ronrwin.uctopic.ui.fragment.ListFragment;
import com.uc.ronrwin.uctopic.ui.fragment.VideoFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Copyright (C) 2004 - 2015 UCWeb Inc. All Rights Reserved.
 * Description : This is my code. Do not even change a single
 * line, otherwise ... ^_*
 * <p>
 * Creation    : 2016/5/30
 * Author      : Ronrwin
 */
public class DataManager {

    public TabModel tabmodel;
    public CardModel cardModel;

    public DataManager() {
        tabmodel = new TabModel();
        cardModel = new CardModel();
    }

    public void loadTabData(final LoadServerDataListener loadServerDataListener) {
        UCTopicApplication.requestManager.loadTabList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadServerDataListener.onFailure(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                try {
                    MetaServerData mMetaMeta = new MetaServerData(new JSONObject(r));
                    ArrayList<TabEntity> result = ParseUtils.parseEntityList(mMetaMeta.metaJson, TabEntity.getBuilder());
                    tabmodel.tabListMetaData = mMetaMeta;
                    tabmodel.mTabList.clear();
                    tabmodel.mTabList.addAll(result);
                    loadServerDataListener.onSuccess(tabmodel.mTabList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void loadListData(final String key, final LoadServerDataListener loadServerDataListener, final boolean isReload) {
        UCTopicApplication.requestManager.loadTopicList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadServerDataListener.onFailure(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                try {
                    MetaServerData mMetaMeta = new MetaServerData(new JSONObject(r));
                    ArrayList<TopicCard> result = ParseUtils.parseEntityList(mMetaMeta.metaJson, TopicCard.getBuilder());
                    ArrayList<TopicCard> data = cardModel.getTopicList(key);
                    if (isReload) {
                        data.clear();
                    }
                    data.addAll(result);

                    cardModel.mTopicMeta.put(key, mMetaMeta);
                    cardModel.mTopicCardListMap.put(key, data);
                    loadServerDataListener.onSuccess(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public void loadVideoData(final LoadServerDataListener loadServerDataListener, final boolean isReload) {
        UCTopicApplication.requestManager.loadVideoList(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadServerDataListener.onFailure(call.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String r = response.body().string();
                try {
                    MetaServerData mMetaMeta = new MetaServerData(new JSONObject(r));
                    ArrayList<VideoCard> videos = ParseUtils.parseEntityList(mMetaMeta.metaJson, VideoCard.getBuilder());
                    if (isReload) {
                        cardModel.mVideoList.clear();
                    }
                    cardModel.mVideoList.addAll(videos);
                    loadServerDataListener.onSuccess(cardModel.mVideoList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
