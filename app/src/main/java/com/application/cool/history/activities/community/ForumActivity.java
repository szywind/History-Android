package com.application.cool.history.activities.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.application.cool.history.R;
import com.application.cool.history.activities.encyclopedia.EncyclopediaDetailActivity;
import com.application.cool.history.fragment.ForumFragment;
import com.application.cool.history.managers.PostManager;
import com.application.cool.history.models.Record;
import com.avos.avoscloud.AVObject;

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

        Bundle bundle = getIntent().getExtras();

        //Extract the data
        topic = bundle.getParcelable(INTENT_TOPIC);

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

        if (forumFragment == null) {
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

}
