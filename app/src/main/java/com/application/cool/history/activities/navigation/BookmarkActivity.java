package com.application.cool.history.activities.navigation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.application.cool.history.R;
import com.application.cool.history.fragment.BookmarkFragment;
import com.application.cool.history.managers.UserManager;

/**
 * Create by Zhenyuan Shen on 5/11/18.
 */

public class BookmarkActivity extends AppCompatActivity {

    private BookmarkFragment bookmarkFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        if (bookmarkFragment == null) {
            Bundle bundle = new Bundle();
            bookmarkFragment = new BookmarkFragment();
            bundle.putString(BookmarkFragment.INTENT_USER_INDEX, UserManager.getSharedInstance(this).getUserId());
            bookmarkFragment.setArguments(bundle);

            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();

            transaction.replace(R.id.fragment_container, bookmarkFragment);
            transaction.commit();
        }
    }
}