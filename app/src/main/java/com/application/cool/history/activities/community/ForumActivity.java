package com.application.cool.history.activities.community;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.application.cool.history.MainActivity;
import com.application.cool.history.R;
import com.application.cool.history.activities.account.WelcomeActivity;
import com.application.cool.history.activities.encyclopedia.EncyclopediaDetailActivity;
import com.application.cool.history.fragment.ForumFragment;
import com.application.cool.history.managers.PostManager;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.Record;
import com.application.cool.history.models.State;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/9/18.
 */
public class ForumActivity extends AppCompatActivity {

    public static final String INTENT_TOPIC = "intent_topic";

    private ImageView banner;
    private Button encyclopediaBtn;
    private Button followBtn;
    private ForumFragment forumFragment;

    private Record topic;
    private UserManager userManager;
    private Bundle bundle;

//    public PostManager.PostResponse postResponse = new PostManager.PostResponse() {
//        @Override
//        public void processFinish(List<AVObject> list) {
//            for (AVObject post : list) {
//                posts.add(post);
//            }
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        banner = (ImageView) findViewById(R.id.banner);
        encyclopediaBtn = (Button) findViewById(R.id.encyclopedia_btn);
        followBtn = (Button) findViewById(R.id.follow_btn);

        bundle = getIntent().getExtras();

        //Extract the data
        topic = bundle.getParcelable(INTENT_TOPIC);
        userManager = UserManager.getSharedInstance(getBaseContext());

        if (forumFragment == null) {
            refreshFragment();
        }

        encyclopediaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), EncyclopediaDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(EncyclopediaDetailActivity.INTENT_RECORD, topic);

                intent.putExtras(bundle);
                //intent.putExtra("event", eventList.get(position));
                startActivity(intent);
            }
        });

        followBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!userManager.isLogin()) {
                    new AlertDialog.Builder(getBaseContext())
                            .setTitle("提醒")
                            .setMessage("请先登录账号")
                            .setPositiveButton("注册/登录", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                    Intent intent = new Intent(getBaseContext(), WelcomeActivity.class);
                                    startActivity(intent);
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
                    if (followBtn.getText().toString() == "+关注") {
                        State.currentSubscribeTopics.add(topic.getName());
                        userManager.setSubscribeTopics(new SaveCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    followBtn.setText("正在关注");
                                    refreshFragment();
                                } else {
                                    State.currentSubscribeTopics.remove(topic.getName());
                                }
                            }
                        });
                    } else {
                        new AlertDialog.Builder(getBaseContext())
                                .setTitle("提醒")
                                .setMessage("是否取消关注")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                        State.currentSubscribeTopics.remove(topic.getName());
                                        userManager.setSubscribeTopics(new SaveCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {
                                                    followBtn.setText("+关注");
                                                    refreshFragment();
                                                } else {
                                                    State.currentSubscribeTopics.add(topic.getName());
                                                }
                                            }
                                        });
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                })
                                .create().show();

                    }
                }
            }
        });

    }

    private void refreshFragment() {

        forumFragment = new ForumFragment();

        bundle.putString(ForumFragment.INTENT_TOPIC_NAME, topic.getName());
        // set Fragmentclass Arguments
        forumFragment.setArguments(bundle);

        android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

        transaction.replace(R.id.fragment_container, forumFragment);
        transaction.commit();
    }

}
