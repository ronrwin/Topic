package com.uc.ronrwin.uctopic.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.http.OkHttpUtils;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.TabEntity;
import com.uc.ronrwin.uctopic.utils.PreferencesHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;

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
public class TabModel {
    public MetaServerData tabListMetaData;

    public ArrayList<TabEntity> mTabList = new ArrayList<>();

    public TabModel() {
        mTabList = loadLoacalTabDatas();
    }

    /**
     * 加载本地的标签数据
     * @return
     */
    public ArrayList<TabEntity> loadLoacalTabDatas() {
        mTabList.clear();
        String data = PreferencesHelper.getTabData();
        if (data.equals("")) {
            String[] tabs = UCTopicApplication.mContext.getResources().getStringArray(R.array.default_tabs);
            for (int i = 0; i < tabs.length; i++) {
                TabEntity tabEntity = new TabEntity();
                tabEntity.id = i;
                tabEntity.name = tabs[i];
                tabEntity.index = i;
                mTabList.add(tabEntity);
            }
            PreferencesHelper.saveTabData(mTabList.toString());
        } else {
            JSONArray array = null;
            try {
                array = new JSONArray(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    TabEntity tabEntity = new TabEntity(array.optJSONObject(i));
                    mTabList.add(tabEntity);
                }
            }
        }

        return mTabList;
    }
}
