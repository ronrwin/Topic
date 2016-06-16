package com.uc.ronrwin.uctopic.ui.fragment;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.uc.ronrwin.uctopic.R;
import com.uc.ronrwin.uctopic.application.UCTopicApplication;
import com.uc.ronrwin.uctopic.constant.BundleKeys;
import com.uc.ronrwin.uctopic.constant.NormalContants;
import com.uc.ronrwin.uctopic.http.LoadServerDataListener;
import com.uc.ronrwin.uctopic.http.OkHttpUtils;
import com.uc.ronrwin.uctopic.http.ParseUtils;
import com.uc.ronrwin.uctopic.model.base.MetaServerData;
import com.uc.ronrwin.uctopic.model.entity.TopicCard;
import com.uc.ronrwin.uctopic.ui.MainActivity;
import com.uc.ronrwin.uctopic.utils.PreferencesHelper;
import com.uc.ronrwin.uctopic.utils.TimeUtils;

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
 * Creation    : 2016/5/24
 * Author      : Ronrwin
 */
public class ListFragment extends BaseListFragment {

    private DefaultRecyclerAdapter mDefaultRecyclerAdapter;
    ArrayList<TopicCard> topics = new ArrayList<>();
    private TopicAdapter mTopicAdapter;

    private String mTitle = "";
    private int mIndex;
    private boolean hasInnitLoaded = false;

    public ListFragment() {
    }

    public static ListFragment newInstance(Bundle bundle) {
        ListFragment fragment = new ListFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        if (getArguments() != null) {
            Bundle bundle = getArguments();
            if (bundle.containsKey(BundleKeys.TITLE)) {
                mTitle = bundle.getString(BundleKeys.TITLE);
                mIndex = bundle.getInt(BundleKeys.INDEX);
            }
        }
        mRefreshTimePrefrenced = PreferencesHelper.getSharedPreferences("tabs");
        mLastUpdate = mRefreshTimePrefrenced.getLong(LAST_UPDATE + mTitle, System.currentTimeMillis());
        mRefreshText.setText(TimeUtils.simplyTime(mLastUpdate));
        mHeader.setBackgroundColor(Color.WHITE);

        mDefaultRecyclerAdapter = new DefaultRecyclerAdapter();
        mTopicAdapter = new TopicAdapter();
        mRecyclerView.setAdapter(mDefaultRecyclerAdapter);

        mFrameLayout.setMainActivity(getActivity());
        InfoFragment fragment = (InfoFragment) getFragmentManager().findFragmentByTag(NormalContants.FragmentTag.TOPIC_TAG);
        if (fragment != null) {
            mFrameLayout.setInfoFragment(fragment);
        }

        if (mIndex == 0 && !hasInnitLoaded) {
            mFrameLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    laodData();
                }
            }, 500);
        }

        return mRootView;
    }

    public void laodData() {
        if (mFrameLayout != null && !hasInnitLoaded) {
//            mFrameLayout.autoRefresh();
            refreshLoad();
        }
    }

    public void finishLoad() {
        if (mFrameLayout != null) {
            mFrameLayout.reset();
            mFrameLayout.refreshComplete();
        }
    }

    @Override
    protected void refreshPrepare() {
        mLastUpdate = mRefreshTimePrefrenced.getLong(LAST_UPDATE + mTitle, System.currentTimeMillis());
        mRefreshText.setText(TimeUtils.simplyTime(mLastUpdate));
    }

    @Override
    protected void refreshLoad() {
        UCTopicApplication.dataManager.loadListData(mTitle, new LoadServerDataListener<ArrayList<TopicCard>>() {
            @Override
            public void onFailure(String message) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishLoad();
                        Toast.makeText(mContext, "Loading Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onSuccess(final ArrayList<TopicCard> data) {
                topics = data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hasInnitLoaded = true;
                        mFrameLayout.refreshComplete();
                        mRecyclerView.setAdapter(mTopicAdapter);
                        mTopicAdapter.notifyDataSetChanged();

                        mLastUpdate = System.currentTimeMillis();
                        mRefreshTimePrefrenced.edit().putLong(LAST_UPDATE + mTitle, mLastUpdate).apply();
                        mRefreshText.setText(TimeUtils.simplyTime(mLastUpdate));
                    }
                });
            }
        }, true);

    }

    class TopicVH extends RecyclerView.ViewHolder {
        ImageView thumb;
        TextView title;
        TextView time;
        ViewGroup root;
        ImageView thumb1, thumb2, thumb3;

        TopicVH(View itemView) {
            super(itemView);
        }

        TopicVH(View itemView, int viewType) {
            super(itemView);
            if (viewType == NormalContants.CardType.TOPIC_CARD_1) {
                title = (TextView) itemView.findViewById(R.id.topic_title);
                time = (TextView) itemView.findViewById(R.id.topic_time);
                root = (ViewGroup) itemView.findViewById(R.id.item_layout);
            } else if (viewType == NormalContants.CardType.TOPIC_CARD_2) {
                thumb = (ImageView) itemView.findViewById(R.id.topic_thumb);
                title = (TextView) itemView.findViewById(R.id.topic_title);
                time = (TextView) itemView.findViewById(R.id.topic_time);
                root = (ViewGroup) itemView.findViewById(R.id.item_layout);
            } else if (viewType == NormalContants.CardType.TOPIC_CARD_3) {
                title = (TextView) itemView.findViewById(R.id.topic_title);
                time = (TextView) itemView.findViewById(R.id.topic_time);
                root = (ViewGroup) itemView.findViewById(R.id.item_layout);
                thumb1 = (ImageView) itemView.findViewById(R.id.topic_thumb_1);
                thumb2 = (ImageView) itemView.findViewById(R.id.topic_thumb_2);
                thumb3 = (ImageView) itemView.findViewById(R.id.topic_thumb_3);
            } else {
                root = (ViewGroup) itemView.findViewById(R.id.item_layout);
            }
        }

    }

    private class TopicAdapter extends RecyclerView.Adapter<TopicVH> {
        @Override
        public TopicVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.default_item_layout, null);
            if (viewType == NormalContants.CardType.TOPIC_CARD_1) {
                v = LayoutInflater.from(mContext).inflate(R.layout.topic_item_layout_1, null);
                return new TopicVH(v, viewType);
            } else if (viewType == NormalContants.CardType.TOPIC_CARD_2) {
                v = LayoutInflater.from(mContext).inflate(R.layout.topic_item_layout_2, null);
                return new TopicVH(v, viewType);
            } else if (viewType == NormalContants.CardType.TOPIC_CARD_3) {
                v = LayoutInflater.from(mContext).inflate(R.layout.topic_item_layout_3, null);
                return new TopicVH(v, viewType);
            }
            return new TopicVH(v, viewType);
        }

        @Override
        public void onBindViewHolder(TopicVH holder, int position) {
            int index = position % topics.size();
            final TopicCard card = topics.get(index);
            if (card.typeId == NormalContants.CardType.TOPIC_CARD_1) {
                if (card.images.get(0) != "") {
                    Picasso.with(mContext).load(card.images.get(0)).into(holder.thumb);
                }
                holder.title.setText(card.title);
                holder.time.setText(card.fromMedia);
            } else if (card.typeId == NormalContants.CardType.TOPIC_CARD_2) {
                if (card.images.get(0) != "") {
                    Picasso.with(mContext).load(card.images.get(0)).into(holder.thumb);
                }
                holder.title.setText(card.title);
                holder.time.setText(card.fromMedia);
            } else if (card.typeId == NormalContants.CardType.TOPIC_CARD_3) {
                if (card.images.size() > 2) {
                    Picasso.with(mContext).load(card.images.get(0)).into(holder.thumb1);
                    Picasso.with(mContext).load(card.images.get(1)).into(holder.thumb2);
                    Picasso.with(mContext).load(card.images.get(2)).into(holder.thumb3);
                }
                holder.title.setText(card.title);
                holder.time.setText(card.fromMedia);
            }
            holder.root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainActivity ac = (MainActivity) getActivity();
                    ac.startTransition(v, card.url);
                }
            });
        }

        @Override
        public int getItemViewType(int position) {
            int index = position % topics.size();
            return topics.get(index).typeId;
        }

        @Override
        public int getItemCount() {
            return 40;
        }
    }

    class DefaultVH extends RecyclerView.ViewHolder {
        ViewGroup defaultLayout;
        TextView text;

        DefaultVH(View itemView) {
            super(itemView);
            defaultLayout = (ViewGroup) itemView.findViewById(R.id.item_layout);
            text = (TextView) itemView.findViewById(R.id.default_text);
        }
    }

    private class DefaultRecyclerAdapter extends RecyclerView.Adapter<DefaultVH> {
        @Override
        public DefaultVH onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.default_item_layout, null);
            return new DefaultVH(v);
        }

        @Override
        public void onBindViewHolder(DefaultVH holder, int position) {
            holder.text.setText(position + "");
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }

        @Override
        public int getItemCount() {
            return 20;
        }
    }

}
