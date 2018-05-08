package com.application.cool.history.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.adapters.RecordListAdapter;
import com.application.cool.history.managers.LocalDataManager;
import com.application.cool.history.models.Record;
import com.shizhefei.fragment.LazyFragment;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/8/18.
 */

public class MoreFragment extends LazyFragment {
    private ProgressBar progressBar;
//    private TextView textView;

    private ListView listView;
    private int tabIndex;
    public static final String INTENT_INT_INDEX = "intent_int_index";

    int typeIndex = 0;

    private List<Record> records;

    private RecordListAdapter adapter;

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

        setContentView(R.layout.fragment_tabmain_item);
        tabIndex = getArguments().getInt(INTENT_INT_INDEX);
        progressBar = (ProgressBar) findViewById(R.id.fragment_mainTab_item_progressBar);
//        textView = (TextView) findViewById(R.id.fragment_mainTab_item_textView);
//        textView.setText("界面" + " " + tabIndex + " 加载完毕");

        listView = (ListView) findViewById(R.id.record_listview);

        handler.sendEmptyMessageDelayed(1, 200);

    }

    @Override
    public void onDestroyViewLazy() {
        super.onDestroyViewLazy();
        handler.removeCallbacksAndMessages(null);
    }

    public void refreshUI(){

//        {"人物", "全部", "事件", "地理", "艺术", "科技"};
        switch (tabIndex) {
            case 0:
                records = LocalDataManager.getSharedInstance(getContext()).allPeople;
                Log.i("all people: ", Integer.toString(records.size()));
                break;
            case 1:
                records = LocalDataManager.getSharedInstance(getContext()).allRecords;
                Log.i("all records: ", Integer.toString(records.size()));
                break;
            case 2:
                records = LocalDataManager.getSharedInstance(getContext()).allEvents;
                Log.i("events: ", Integer.toString(records.size()));
                break;
            case 3:
                records = LocalDataManager.getSharedInstance(getContext()).geo;
                Log.i("geo: ", Integer.toString(records.size()));
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
        adapter = new RecordListAdapter(getActivity(), records);
        listView.setAdapter(adapter);
    }


    private Handler handler;
}
