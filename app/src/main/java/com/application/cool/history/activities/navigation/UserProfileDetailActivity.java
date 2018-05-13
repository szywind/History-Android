package com.application.cool.history.activities.navigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.fragment.BookmarkFragment;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.util.GlideApp;
import com.avos.avoscloud.AVUser;


/**
 * Created by Zhenyuan Shen on 5/10/18.
 */

public class UserProfileDetailActivity extends AppCompatActivity {

    public static final String INTENT_USER = "intent_user";

    private ImageView avatar;
    private TextView name;

    private AVUser user;

    private BookmarkFragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_detail);

        name = (TextView) findViewById(R.id.nickname);
        avatar = (ImageView) findViewById(R.id.avatar);

        //Extract the data
        user = getIntent().getExtras().getParcelable(INTENT_USER);

        UserManager userManager = UserManager.getSharedInstance(this);
        name.setText(userManager.getNickname(user));

        GlideApp.with(this)
                .load(userManager.getAvatarURL(user))
                .circleCrop()
                .placeholder(R.drawable.avatar)
                .error(R.drawable.avatar)
                .into(avatar);

        if (bookmarkFragment == null) {
            Bundle bundle = new Bundle();
            bookmarkFragment = new BookmarkFragment();
            bundle.putString(BookmarkFragment.INTENT_USER_INDEX, UserManager.getSharedInstance(this).getUserId(user));
            bookmarkFragment.setArguments(bundle);

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.fragment_container, bookmarkFragment);
            transaction.commit();
        }
    }
}

