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
import com.application.cool.history.activities.encyclopedia.EncyclopediaDetailActivity;
import com.application.cool.history.activities.navigation.UserProfileDetailActivity;
import com.application.cool.history.adapters.PostListAdapter;
import com.application.cool.history.adapters.RecordListAdapter;
import com.application.cool.history.adapters.UserListAdapter;
import com.application.cool.history.constants.LCConstants;
import com.application.cool.history.managers.LocalDataManager;
import com.application.cool.history.managers.PostManager;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.Record;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.shizhefei.fragment.LazyFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/10/18.
 */

public class SearchSubFragment extends LazyFragment {
    private ProgressBar progressBar;
//    private TextView textView;

    private ListView listView;
    private int tabIndex;
    private String searchWord;

    public static final String INTENT_INT_INDEX = "intent_int_index";
    public static final String INTENT_SEARCH_WORD = "intent_search_word";

    private List<Record> records;
    private List<AVUser> users;
    private List<AVObject> posts;

    private RecordListAdapter recordListAdapter;
    private PostListAdapter postListAdapter;
    private UserListAdapter userListAdapter;

    PostManager.PostResponse postResponse = new PostManager.PostResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            posts = list;
            Log.i("search post: ", Integer.toString(posts.size()));
            postListAdapter = new PostListAdapter(getContext(), posts);
            listView.setAdapter(postListAdapter);
        }
    };

    UserManager.UserResponse userResponse = new UserManager.UserResponse() {
        @Override
        public void processFinish(String output) {

        }

        @Override
        public void processFinish(List<AVUser> list) {
            users = list;
            Log.i("search user: ", Integer.toString(users.size()));
            userListAdapter = new UserListAdapter(getContext(), users);
            listView.setAdapter(userListAdapter);
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

        setContentView(R.layout.fragment_search_tab_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        searchWord = getArguments().getString(INTENT_SEARCH_WORD);

        progressBar = (ProgressBar) findViewById(R.id.search_progressBar);

        listView = (ListView) findViewById(R.id.search_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent;
                Bundle bundle;

                switch (tabIndex) {
                    case 0: // 百科-人物
                    case 1: // 百科-事件
                        intent = new Intent(getContext(), EncyclopediaDetailActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelable(EncyclopediaDetailActivity.INTENT_RECORD, records.get(position));

                        intent.putExtras(bundle);
                        startActivity(intent);

                        break;

                    case 2: // 社区
                        intent = new Intent(getContext(), PostDetailActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelable(PostDetailActivity.INTENT_POST, posts.get(position));

                        intent.putExtras(bundle);
                        startActivity(intent);

                        break;

                    case 3: // 用户
                        intent = new Intent(getContext(), UserProfileDetailActivity.class);
                        bundle = new Bundle();
                        bundle.putParcelable(UserProfileDetailActivity.INTENT_USER, users.get(position));

                        intent.putExtras(bundle);
                        startActivity(intent);

                        break;

                    default:
                        break;
                }

            }
        });

        handler.sendEmptyMessageDelayed(1, 200);

        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setDistanceToTriggerSync(10);
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

//        {"人物", "事件", "社区", "用户"};
        switch (tabIndex) {
            case 0:
                records = new ArrayList<>();
                for ( Record person: LocalDataManager.getSharedInstance(getContext()).allPeople) {
                    if (person.getName().contains(searchWord)) {
                        records.add(person);
                    }
                }
                Log.i("search record: ", Integer.toString(records.size()));
                recordListAdapter = new RecordListAdapter(getContext(),records);
                listView.setAdapter(recordListAdapter);
                return;

            case 1:
                records = new ArrayList<>();

                for ( Record event: LocalDataManager.getSharedInstance(getContext()).allEvents) {
                    if (event.getName().contains(searchWord)) {
                        records.add(event);
                    }
                }

                Log.i("search record: ", Integer.toString(records.size()));
                recordListAdapter = new RecordListAdapter(getContext(),records);
                listView.setAdapter(recordListAdapter);
                return;

            case 2:
                PostManager.getSharedInstance(getContext()).searchPostFromLC(LCConstants.PostKey.title, searchWord, postResponse);
                return;

            case 3:
                UserManager.getSharedInstance(getContext()).searchUserFromLC(LCConstants.UserKey.nickname, searchWord, userResponse);
                return;

            default:
                return;
        }
    }


    private Handler handler;
}
