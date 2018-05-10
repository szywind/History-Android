package com.application.cool.history.activities.navigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.application.cool.history.R;
import com.application.cool.history.fragment.SocialFragment;

public class SocialActivity extends AppCompatActivity {

    private SocialFragment socialFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social);

        if (socialFragment == null) {
            socialFragment = new SocialFragment();

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.fragment_container, socialFragment);
            transaction.commit();
        }
    }
}
