package com.application.cool.history.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.activities.encyclopedia.EncyclopediaDetailActivity;
import com.application.cool.history.adapters.EncyclopediaAdapter;
import com.application.cool.history.adapters.RecordListAdapter;
import com.application.cool.history.managers.LocalDataManager;
import com.application.cool.history.models.Record;
import com.application.cool.history.view.SideIndexBar;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/8/18.
 */

public class EncyclopediaSubFragment extends LazyFragment implements SideIndexBar.OnTouchListener {
    private ProgressBar progressBar;

    private ListView listView;
    private int tabIndex;
    public static final String INTENT_INT_INDEX = "intent_int_index";

    private List<Record> records;

    private EncyclopediaAdapter adapter;
    private SideIndexBar sideIndexBar;
    private TextView dialog;

    @Override
    protected void onCreateViewLazy(final Bundle savedInstanceState) {
        super.onCreateViewLazy(savedInstanceState);
        handler = new Handler(Looper.getMainLooper()) {
            public void handleMessage(android.os.Message msg) {
                progressBar.setVisibility(View.GONE);
                refreshUI();
            }
        };

        setContentView(R.layout.fragment_encyclopedia_tab_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        progressBar = (ProgressBar) findViewById(R.id.encyclopedia_progressBar);
        sideIndexBar = (SideIndexBar) findViewById(R.id.sidebar);
        dialog = (TextView) findViewById(R.id.dialog);

        sideIndexBar.setTextView(dialog);
        sideIndexBar.setOnTouchListener(this);

        listView = (ListView) findViewById(R.id.encyclopedia_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), EncyclopediaDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EncyclopediaDetailActivity.INTENT_RECORD, records.get(position));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        handler.sendEmptyMessageDelayed(1, 200);

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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    swipeRefreshLayout.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPauseLazy() {
        super.onPauseLazy();
    }

    public void refreshUI(){

//        {"人物", "全部", "事件", "地理", "艺术", "科技"};
        switch (tabIndex) {
            case 0:
                records = LocalDataManager.getSharedInstance(getContext()).allPeople;
                Log.i("people: ", Integer.toString(records.size()));
                break;

            case 1:
                records = LocalDataManager.getSharedInstance(getContext()).allEvents;
                Log.i("all: ", Integer.toString(records.size()));
                break;

            case 2:
                records = LocalDataManager.getSharedInstance(getContext()).events;
                Log.i("events: ", Integer.toString(records.size()));
                break;

            case 3:
                records = LocalDataManager.getSharedInstance(getContext()).geo;
                Log.i("geo: ", Integer.toString(records.size()));
                break;

            case 4:
                records = LocalDataManager.getSharedInstance(getContext()).art;
                Log.i("art: ", Integer.toString(records.size()));
                break;

            case 5:
                records = LocalDataManager.getSharedInstance(getContext()).tech;
                Log.i("tech: ", Integer.toString(records.size()));
                break;

            default:
                break;
        }

        adapter = new EncyclopediaAdapter(getContext(),records);
        listView.setAdapter(adapter);
    }


    private Handler handler;


    @Override
    public void onTouchChanged(String s) {
        int position = 0;
        // 该字母首次出现的位置
        if (adapter != null) {
            position = adapter.getPositionForSection(s.charAt(0));
        }
        if (position != -1) {
            listView.setSelection(position);
        }
    }
}
