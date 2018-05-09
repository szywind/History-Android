package com.application.cool.history.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.models.Record;
import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/8/18.
 */

public class RecordListAdapter extends BaseAdapter {
    private Activity activity;

    private List<Record> list;


    public RecordListAdapter(Activity activity, List<Record> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.record_cell, null);
            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);

            holder.padding = (TextView) convertView.findViewById(R.id.padding);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Record record = list.get(position);

        if (record != null) {

            String boo = record.getName();
            holder.name.setText(record.getName());

            Ion.with(holder.avatar).fitXY().placeholder(R.drawable.placeholder).error(R.drawable.placeholder).load(record.getAvatarURL());
        }

        if (position == getCount()-1){
            ViewGroup.LayoutParams params = holder.padding.getLayoutParams();
            params.height = activity.getResources().getDimensionPixelSize(R.dimen.text_view_padding_height);
            holder.padding.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = holder.padding.getLayoutParams();
            params.height = activity.getResources().getDimensionPixelSize(R.dimen.zero_height);
            holder.padding.setLayoutParams(params);
        }
        return convertView;
    }
    class ViewHolder {
        ImageView avatar;
        TextView name;
        TextView padding;
    }

    public void updateListView(List<Record> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}