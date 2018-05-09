package com.application.cool.history.adapters;

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
 * Created by Zhenyuan Shen on 5/9/18.
 */

public class TopicGridAdapter extends BaseAdapter {

    private Context context;
    private List<Record> list;

    public TopicGridAdapter(Context context, List<Record> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.topic_cell, null);
            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Record topic = list.get(position);

        if (topic != null) {

            holder.name.setText(topic.getName());

            Ion.with(holder.avatar).fitXY().placeholder(R.drawable.placeholder).error(R.drawable.placeholder).load(topic.getAvatarURL());
        }

        return convertView;
    }

    class ViewHolder {
        ImageView avatar;
        TextView name;
    }
}
