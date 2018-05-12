package com.application.cool.history.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.application.cool.history.R;
import com.application.cool.history.activities.community.ForumActivity;
import com.application.cool.history.adapters.RecordListAdapter;
import com.application.cool.history.adapters.TopicGridAdapter;
import com.application.cool.history.managers.LocalDataManager;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.Record;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/8/18.
 */

public class CommunitySubFragment extends LazyFragment {
    private ProgressBar progressBar;

    private GridView gridView;
    private int tabIndex;
    public static final String INTENT_INT_INDEX = "intent_int_index";

    private List<Record> topics;

    private TopicGridAdapter adapter;

    @Override
    protected void onCreateViewLazy(final Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                progressBar.setVisibility(View.GONE);
                refreshUI();
            }
        };

        setContentView(R.layout.fragment_community_tab_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        progressBar = (ProgressBar) findViewById(R.id.community_progressBar);

        gridView = (GridView) findViewById(R.id.community_gridview);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), ForumActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(ForumActivity.INTENT_TOPIC, topics.get(position));

                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        handler.sendEmptyMessageDelayed(1, 200);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.history, R.color.black, R.color.avoscloud_blue);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshUI();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeCallbacksAndMessages(null);
    }

    public void refreshUI(){
        int skip = (UserManager.getSharedInstance(getContext()).isLogin()) ? 0 : 1;
//        {"关注", "人物", "事件", "地理", "艺术", "科技"};
        switch (tabIndex + skip) {
            case 0:
                topics = LocalDataManager.getSharedInstance(getContext()).getFollowingTopics();
                Log.i("following: ", Integer.toString(topics.size()));
                break;

            case 1:
                topics = LocalDataManager.getSharedInstance(getContext()).allPeople;
                Log.i("people: ", Integer.toString(topics.size()));
                break;

            case 2:
                topics = LocalDataManager.getSharedInstance(getContext()).events;
                Log.i("events: ", Integer.toString(topics.size()));
                break;

            case 3:
                topics = LocalDataManager.getSharedInstance(getContext()).geo;
                Log.i("geo: ", Integer.toString(topics.size()));

            case 4:
                topics = LocalDataManager.getSharedInstance(getContext()).art;
                Log.i("art: ", Integer.toString(topics.size()));
                break;

            case 5:
                topics = LocalDataManager.getSharedInstance(getContext()).tech;
                Log.i("tech: ", Integer.toString(topics.size()));
                break;

            default:
                break;
        }

        adapter = new TopicGridAdapter(getContext(), topics);
        gridView.setAdapter(adapter);
    }


    private Handler handler;
}
