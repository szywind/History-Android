package com.application.cool.history.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.activities.account.WelcomeActivity;
import com.application.cool.history.constants.LCConstants;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.State;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Zhenyuan Shen on 5/10/18.
 */

public class UserListAdapter extends BaseAdapter {

    private Context context;
    private List<AVUser> list;
    private UserManager userManager;
    private AVUser user;
    private Boolean isFolloweePage = false;

    public UserListAdapter(Context context, List<AVUser> list) {
        this(context, list, false);
    }

    public UserListAdapter(Context context, List<AVUser> list, Boolean isFolloweePage) {
        this.context = context;
        this.list = list;
        isFolloweePage = isFolloweePage;
        this.userManager = UserManager.getSharedInstance(context);
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
        final ViewHolder holder;
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

        user = list.get(position);

        if (user != null) {

            holder.name.setText(userManager.getNickname(user));

            Ion.with(holder.avatar)
                    .placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                    .load(userManager.getAvatarURL(user));

            if (State.currentFollowees.contains(userManager.getNickname(user))) {
                holder.followBtn.setText("正在关注");
            } else {
                holder.followBtn.setText("+关注");
            }

            holder.followBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!userManager.isLogin()) {
                        new AlertDialog.Builder(context)
                                .setTitle("提醒")
                                .setMessage("请先登录账号")
                                .setPositiveButton("注册/登录", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        Intent intent = new Intent(context, WelcomeActivity.class);
                                        context.startActivity(intent);
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create().show();
                    } else {
                        if (holder.followBtn.getText().toString() == "+关注") {
                            follow();
                        } else {
                            new AlertDialog.Builder(context)
                                    .setTitle("提醒")
                                    .setMessage("是否取消关注")
                                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                            unfollow();
                                        }
                                    }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create().show();

                        }
                    }
                }
            });
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

    private void follow() {
        userManager.currentUser().followInBackground(userManager.getUserId(user), new FollowCallback() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                    Log.i("Following", "succeed in following");
                    userManager.updateCounter(LCConstants.UserKey.followees, 1);
                    State.currentFollowees.add(userManager.getUserId(user));
                    if (isFolloweePage) {
                        list.add(user);
                    }
                    refreshUI();
                }
            }

            @Override
            protected void internalDone0(Object o, AVException e) {

            }
        });
    }

    private void unfollow() {
        userManager.currentUser().unfollowInBackground(userManager.getUserId(user), new FollowCallback() {
            @Override
            public void done(AVObject avObject, AVException e) {
                if (e == null) {
                   Log.i("Unfollowing", "succeed in unfollowing");
                   userManager.updateCounter(LCConstants.UserKey.followees, -1);
                   State.currentFollowees.remove(userManager.getUserId(user));
                    if (isFolloweePage) {
                        list.remove(user);
                    }
                   refreshUI();
                }
            }

            @Override
            protected void internalDone0(Object o, AVException e) {

            }
        });
    }

    private void refreshUI() {
        updateListView(list);
    }
}