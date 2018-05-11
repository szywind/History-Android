package com.application.cool.history.activities.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.util.MessageEvent;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDetailActivity extends AppCompatActivity {

    @BindView(R.id.cancel)
    TextView cancel;
    @BindView(R.id.edit_profile)
    Button editProfile;
    @BindView(R.id.nickname)
    TextView nickname;
    @BindView(R.id.email)
    TextView email;
    @BindView(R.id.join_time)
    TextView joinTime;
    @BindView(R.id.location)
    TextView location;
    @BindView((R.id.avatar_img))
    CircleImageView avatarImage;
    UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        userManager = UserManager.getSharedInstance(this);

        nickname.setText(userManager.getNickname());
        email.setText(userManager.getEmail(userManager.currentUser()));
        String avatarUrl = userManager.getAvatarURL(userManager.currentUser());
        if (avatarUrl != null) {
            Glide.with(this).load(avatarUrl).into(avatarImage);
        }


    }

    @OnClick({R.id.cancel, R.id.edit_profile})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                finish();
                break;
            case R.id.edit_profile:
                Intent intent = new Intent(this, ProfileEditActivity.class);
                startActivity(intent);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void EditProfileEvent(MessageEvent messageEvent) {

        if (messageEvent.messgae == Constants.EventType.EVENT_UPDATE_USER) {
            AVUser user = userManager.currentUser();
            String url = userManager.getAvatarURL(user);
            if (url != null) {
                Glide.with(this).load(url).into(avatarImage);
            }
            nickname.setText(userManager.getNickname(userManager.currentUser()));
        }
    }
}
