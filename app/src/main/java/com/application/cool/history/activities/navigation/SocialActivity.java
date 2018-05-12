package com.application.cool.history.activities.navigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.application.cool.history.R;
import com.application.cool.history.adapters.BaseFragmentPagerAdapter;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.fragment.SocialFragment;
import com.application.cool.history.managers.SocialManager;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.models.State;
import com.avos.avoscloud.AVUser;

import java.util.List;

public class SocialActivity extends AppCompatActivity {

    private SocialFragment socialFragment;

    private SocialManager.SocialResponse socialResponse = new SocialManager.SocialResponse() {
        @Override
        public void processFinish(List<AVUser> list) {
            Log.i("search social circle: ", Integer.toString(list.size()));

            // update current followee list
            State.currentFollowees.clear();
            for (AVUser user: list) {
                State.currentFollowees.add(UserManager.getSharedInstance(getBaseContext()).getUserId(user));
            }

            refreshUI();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        refreshUI();
        SocialManager.getSharedInstance(this)
                .fetchAllFollowees(UserManager.getSharedInstance(this).currentUser(), socialResponse);
    }


    private void refreshUI() {
        if (socialFragment == null) {
            socialFragment = new SocialFragment();

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.fragment_container, socialFragment);
            transaction.commit();
        }
    }
}
