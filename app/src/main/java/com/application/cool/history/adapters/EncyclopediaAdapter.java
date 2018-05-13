package com.application.cool.history.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SectionIndexer;

import com.application.cool.history.R;
import com.application.cool.history.models.Record;

import java.util.List;

/**
 * Create by Zhenyuan Shen on 5/13/18.
 */

public class EncyclopediaAdapter extends RecordListAdapter implements SectionIndexer {

    public EncyclopediaAdapter(Context context, List<Record> list) {
        super(context, list, R.layout.encyclopedia_cell);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        ViewHolder holder = (ViewHolder) view.getTag();

        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            holder.letter.setVisibility(View.VISIBLE);
            holder.letter.setText(list.get(position).getPinyin().toUpperCase().substring(0,1));
        } else {
            holder.letter.setVisibility(View.GONE);
        }

        return view;
    }

    @Override
    public Object[] getSections() {
        return new Object[0];
    }

    @Override
    public int getSectionForPosition(int position) {
        return list.get(position).getPinyin().toUpperCase().charAt(0);
    }

    @Override
    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getPinyin();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }
}
