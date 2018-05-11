package com.application.cool.history.activities.navigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.managers.UserManager;
import com.avos.avoscloud.AVUser;
import com.koushikdutta.ion.Ion;

/**
 * Created by Zhenyuan Shen on 5/10/18.
 */

public class UserProfileDetailActivity extends AppCompatActivity {

    public static final String INTENT_USER = "intent_user";

    private ImageView avatar;
    private TextView name;

    private AVUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);

        name = (TextView) findViewById(R.id.name);
        avatar = (ImageView) findViewById(R.id.avatar);

        Bundle bundle = getIntent().getExtras();

        //Extract the data
        user = bundle.getParcelable(INTENT_USER);

        UserManager userManager = UserManager.getSharedInstance(this);
        name.setText(userManager.getNickname(user));

        Ion.with(avatar).fitXY().placeholder(R.drawable.placeholder).error(R.drawable.placeholder)
                .load(userManager.getAvatarURL(user));
    }
}

