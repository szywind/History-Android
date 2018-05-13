package com.application.cool.history.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.application.cool.history.R;
import com.application.cool.history.activities.community.PostDetailActivity;
import com.application.cool.history.adapters.PostListAdapter;
import com.application.cool.history.constants.LCConstants;
import com.application.cool.history.managers.PostManager;
import com.avos.avoscloud.AVObject;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.List;


public class ForumSubFragment extends LazyFragment {

    public static final String INTENT_INT_INDEX = "intent_int_index";
    public static final String INTENT_TOPIC_NAME = "intent_topic_name";

    private ProgressBar progressBar;

    private ListView listView;
    private int tabIndex;

    private String topicName;
    private List<AVObject> posts = new ArrayList<>();   // TODO List<Post> posts;
    private PostListAdapter adapter;

    public PostManager.PostResponse postResponse = new PostManager.PostResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            for (AVObject post : list) {
                posts.add(post);
            }
            refreshUI();
        }
    };


    @Override
    protected void onCreateViewLazy(final Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                progressBar.setVisibility(View.GONE);
                refreshUI();
            }
        };

        setContentView(R.layout.fragment_forum_tab_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        topicName = getArguments().getString(INTENT_TOPIC_NAME);

        progressBar = (ProgressBar) findViewById(R.id.forum_progressBar);

        listView = (ListView) findViewById(R.id.forum_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PostDetailActivity.INTENT_POST, posts.get(position));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        handler.sendEmptyMessageDelayed(1, 200);

        PostManager.getSharedInstance(getContext())
                .fetchPostFromLC(LCConstants.PostKey.subtopic, topicName, postResponse);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setDistanceToTriggerSync(10);
        swipeRefreshLayout.setColorSchemeResources(R.color.historyDarkBrown, R.color.black, R.color.avoscloud_blue);
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

//        {"最新发布", "最多回复", "最多喜欢"};

        Log.i("posts: ", Integer.toString(posts.size()));

        switch (tabIndex) {
            case 0:
                break;

            case 1:
//                posts = posts.sortByRepies();
                break;

            case 2:
//                posts = posts.sortByLikes();
                break;

            default:
                break;
        }

        adapter = new PostListAdapter(getContext(), posts);
        listView.setAdapter(adapter);
    }


    private Handler handler;
}