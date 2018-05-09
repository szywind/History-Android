package com.application.cool.history.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.application.cool.history.R;
import com.application.cool.history.activities.community.PostDetailActivity;
import com.application.cool.history.activities.encyclopedia.EncyclopediaDetailActivity;
import com.application.cool.history.adapters.PostListAdapter;
import com.application.cool.history.adapters.RecordListAdapter;
import com.application.cool.history.constants.LCConstants;
import com.application.cool.history.managers.PostManager;
import com.application.cool.history.models.Post;
import com.application.cool.history.models.Record;
import com.avos.avoscloud.AVObject;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;


public class ForumSubFragment extends LazyFragment {
    private ProgressBar progressBar;
//    private TextView textView;

    private ListView listView;
    private int tabIndex;
    public static final String INTENT_INT_INDEX = "intent_int_index";

    private Record topic;
    private List<AVObject> posts;   // TODO List<Post> posts;
    private PostListAdapter adapter;

    public PostManager.PostResponse postResponse = new PostManager.PostResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            for (AVObject post : list) {
                posts.add(post);
            }
        }
    };


    @Override
    protected void onCreateViewLazy(final Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                progressBar.setVisibility(View.GONE);
//                textView.setVisibility(View.VISIBLE);
                refreshUI();
            }
        };

        setContentView(R.layout.fragment_forum_tab_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
//        textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
//        textView.setText("界面" + " " + tabIndex + " 加载完毕");

        listView = (ListView) findViewById(R.id.post_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), PostDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(PostDetailActivity.INTENT_RECORD, posts.get(position));

                intent.putExtras(bundle);
                //intent.putExtra("event", eventList.get(position));
                startActivity(intent);
            }
        });

        handler.sendEmptyMessageDelayed(1, 200);

    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeCallbacksAndMessages(null);
    }

    public void refreshUI(){

//        {"最新发布", "最多回复", "最多喜欢"};
        PostManager.getSharedInstance(getContext())
                .fetchPostFromLC(LCConstants.PostKey.subtopic, topic.getName(), postResponse);

        Log.i("people: ", Integer.toString(posts.size()));

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

        adapter = new PostListAdapter(getActivity(), posts);
        listView.setAdapter(adapter);
    }


    private Handler handler;
}