package com.application.cool.history.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.State;
import com.avos.avoscloud.AVUser;
import com.koushikdutta.ion.Ion;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Zhenyuan Shen on 5/10/18.
 */

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<AVUser> list;


    public UserListAdapter(Context context, List<AVUser> list) {
        this.context = context;
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
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.user_cell, null);
            holder = new ViewHolder();
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.followBtn = (Button) convertView.findViewById(R.id.follow_btn);
            holder.padding = (TextView) convertView.findViewById(R.id.padding);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final AVUser user = list.get(position);

        UserManager userManager = UserManager.getSharedInstance(context);

        if (user != null) {

            holder.name.setText(userManager.getNickname(user));

            Ion.with(holder.avatar)
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                    .load(userManager.getAvatarURL(user));

            if (State.currentFollowees.contains(UserManager.getSharedInstance(context).getNickname(user))) {
                holder.followBtn.setText("正在关注");
            } else {
                holder.followBtn.setText("+关注");
            }
        }

        if (position == getCount()-1){
            ViewGroup.LayoutParams params = holder.padding.getLayoutParams();
            params.height = context.getResources().getDimensionPixelSize(R.dimen.text_view_padding_height);
            holder.padding.setLayoutParams(params);
        } else {
            ViewGroup.LayoutParams params = holder.padding.getLayoutParams();
            params.height = context.getResources().getDimensionPixelSize(R.dimen.zero_height);
            holder.padding.setLayoutParams(params);
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView avatar;
        TextView name;
        Button followBtn;
        TextView padding;
    }

    public void updateListView(List<AVUser> list) {
        this.list = list;
        notifyDataSetChanged();
    }


}