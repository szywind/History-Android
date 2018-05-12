package com.application.cool.history.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.application.cool.history.R;
import com.application.cool.history.activities.navigation.UserProfileDetailActivity;
import com.application.cool.history.adapters.UserListAdapter;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.managers.SocialManager;
import com.application.cool.history.managers.UserManager;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/8/18.
 */

public class SocialSubFragment extends LazyFragment {

    private ProgressBar progressBar;

    private ListView listView;
    private int tabIndex;
    public static final String INTENT_INT_INDEX = "intent_int_index";

    private List<AVUser> users;

    private UserListAdapter adapter;


    private SocialManager.SocialResponse socialResponse = new SocialManager.SocialResponse() {
        @Override
        public void processFinish(List<AVUser> list) {
            users = list;
            Log.i("search social circle: ", Integer.toString(list.size()));

            adapter = new UserListAdapter(getContext(), users);
            listView.setAdapter(adapter);
        }
    };

    private SocialManager socialManager = SocialManager.getSharedInstance(getContext());
    private UserManager userManager = UserManager.getSharedInstance(getContext());

    @Override
    protected void onResumeLazy() {
        super.onResumeLazy();
    }

    @Override
    protected void onCreateViewLazy(final Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                progressBar.setVisibility(View.GONE);
                refreshUI();
            }
        };

        setContentView(R.layout.fragment_social_tab_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        progressBar = (ProgressBar) findViewById(R.id.social_progressBar);

        listView = (ListView) findViewById(R.id.social_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), UserProfileDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(UserProfileDetailActivity.INTENT_USER, users.get(position));

                intent.putExtras(bundle);
                startActivity(intent);
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

        LocalBroadcastManager.getInstance(getContext())
                .registerReceiver(new MyBroadcastReceiver(), new IntentFilter(Constants.Broadcast.REFRESH_FOLLOWING_USER));

    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeCallbacksAndMessages(null);

        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(new MyBroadcastReceiver());

    }

    final class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && Constants.Broadcast.REFRESH_FOLLOWING_USER.equals(intent.getAction())) {
                refreshUI();
            }
        }
    }

    public void refreshUI(){

//        {"关注者", "正在关注", "最热用户", "可能喜欢"};

        switch (tabIndex) {
            case 0:
                socialManager.fetchAllFollowers(userManager.currentUser(), socialResponse);
                return;

            case 1:
                socialManager.fetchAllFollowees(userManager.currentUser(), socialResponse);
                return;

            case 2:
                userManager.findHotUsers(0, 10, new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        if (e == null) {
                            users = list;
                            Log.i("search hot users: ", Integer.toString(list.size()));
                            adapter = new UserListAdapter(getContext(), users);
                            listView.setAdapter(adapter);
                        }
                    }
                });
                return;


            case 3:
                // TODO
                userManager.findHotUsers(0, 10, new FindCallback<AVUser>() {
                    @Override
                    public void done(List<AVUser> list, AVException e) {
                        if (e == null) {
                            users = list;
                            Log.i("recommend users: ", Integer.toString(list.size()));
                            adapter = new UserListAdapter(getContext(), users);
                            listView.setAdapter(adapter);
                        }
                    }
                });
                return;


            default:
                break;
        }
    }


    private Handler handler;
}
