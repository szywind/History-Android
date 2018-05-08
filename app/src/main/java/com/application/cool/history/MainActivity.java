package com.application.cool.history;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.application.cool.history.activities.account.LoginActivity;
import com.application.cool.history.activities.account.RegisterNameActivity;
import com.application.cool.history.activities.account.WelcomeActivity;
import com.application.cool.history.db.EventEntity;
import com.application.cool.history.db.PersonEntity;
import com.application.cool.history.fragment.EncyclopediaFragment;
import com.application.cool.history.fragment.ForumFragment;
import com.application.cool.history.fragment.SearchFragment;
import com.application.cool.history.fragment.TimelineFragment;
import com.application.cool.history.managers.DBManagers.EventStore;
import com.application.cool.history.managers.DBManagers.PersonStore;
import com.application.cool.history.managers.EventManager;
import com.application.cool.history.managers.PersonManager;
import com.application.cool.history.util.CommonData;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.FindCallback;

import java.util.List;


public class MainActivity extends AppCompatActivity {



    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout drawerLayoutMenu;

    private EncyclopediaFragment encyclopediaFragment;
    private ForumFragment forumFragment;
    private SearchFragment searchFragment;
    private TimelineFragment timelineFragment;


    private EventManager.EventResponse eventResponse = new EventManager.EventResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            for (AVObject event : list) {
                EventStore.getSharedInstance(getBaseContext()).saveEvent(new EventEntity(event));
            }
        }
    };

    private PersonManager.PersonResponse personResponse = new PersonManager.PersonResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            for (AVObject person : list) {
                PersonStore.getSharedInstance(getBaseContext()).savePerson(new PersonEntity(person));
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayoutMenu = findViewById(R.id.drawer_layout);

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        navView.setCheckedItem(-1);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                drawerLayoutMenu.closeDrawers();
                return true;
            }
        });

        navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                startActivity(intent);
            }
        });

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#FF107FFD")
                .setInActiveColor("#e9e6e6");

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_timeline, R.string.timeline))
                .addItem(new BottomNavigationItem(R.drawable.ic_forum, R.string.forum))
                .addItem(new BottomNavigationItem(R.drawable.ic_encyclopdeic, R.string.encyclopedia))
                .addItem(new BottomNavigationItem(R.drawable.ic_search, R.string.search))
                .initialise();

        setDefaultFragment();
        setupData();
        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction transaction = fm.beginTransaction();
                switch (position) {
                    case 0:
                        if (timelineFragment == null) {
                            timelineFragment = new TimelineFragment();
                        }
                        transaction.replace(R.id.fragment_container, timelineFragment);
                        break;
                    case 1:
                        if (forumFragment == null) {
                            forumFragment = new ForumFragment();
                        }
                        transaction.replace(R.id.fragment_container, forumFragment);
                        break;
                    case 2:
                        if (encyclopediaFragment == null) {
                            encyclopediaFragment = new EncyclopediaFragment();
                        }
                        transaction.replace(R.id.fragment_container, encyclopediaFragment);
                        break;
                    case 3:
                        if (searchFragment == null) {
                            searchFragment = new SearchFragment();
                        }
                        transaction.replace(R.id.fragment_container, searchFragment);
                        break;
                    default:
                        break;
                }
                transaction.commit();

            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayoutMenu.openDrawer(GravityCompat.START);
                break;
            default:

        }
        return true;
    }

    private void setDefaultFragment() {
        if (timelineFragment == null) {
            timelineFragment = new TimelineFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timelineFragment);
            getSupportFragmentManager().beginTransaction().commit();
        }
    }

    private void setupData() {
        EventManager.getSharedInstance(this).fetchAllEventsFromLC(eventResponse);
        PersonManager.getSharedInstance(this).fetchAllPeopleFromLC(personResponse);
    }
}
