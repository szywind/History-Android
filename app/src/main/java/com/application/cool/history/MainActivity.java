package com.application.cool.history;

import android.content.DialogInterface;
import android.content.Intent;

import com.application.cool.history.activities.navigation.ProfileDetailActivity;
import com.application.cool.history.activities.navigation.BookmarkActivity;
import com.application.cool.history.activities.navigation.SocialActivity;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.fragment.CommunityFragment;
import com.application.cool.history.managers.UserManager;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.application.cool.history.activities.account.WelcomeActivity;
import com.application.cool.history.db.EventEntity;
import com.application.cool.history.db.PersonEntity;
import com.application.cool.history.fragment.EncyclopediaFragment;
import com.application.cool.history.fragment.SearchFragment;
import com.application.cool.history.fragment.TimelineFragment;
import com.application.cool.history.util.MessageEvent;
import com.application.cool.history.managers.DBManagers.EventStore;
import com.application.cool.history.managers.DBManagers.PersonStore;
import com.application.cool.history.managers.EventManager;
import com.application.cool.history.managers.LocalDataManager;
import com.application.cool.history.managers.PersonManager;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.List;


public class MainActivity extends AppCompatActivity {



    private BottomNavigationBar bottomNavigationBar;
    private DrawerLayout drawerLayoutMenu;
    private NavigationView navView;
    private EncyclopediaFragment encyclopediaFragment;
    private CommunityFragment communityFragment;
    private SearchFragment searchFragment;
    private TimelineFragment timelineFragment;

    private CircleImageView imageView;
    private TextView userName;
    private TextView userEmail;
    private CircleImageView userAvatar;
    private UserManager userManager;


    private EventManager.EventResponse eventResponse = new EventManager.EventResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            for (AVObject event : list) {
                EventEntity eventEntity = new EventEntity(event);
                EventStore.getSharedInstance(getBaseContext()).saveEvent(eventEntity);
                LocalDataManager.getSharedInstance(getBaseContext()).addRecord(eventEntity);
            }
        }
    };

    private PersonManager.PersonResponse personResponse = new PersonManager.PersonResponse() {
        @Override
        public void processFinish(List<AVObject> list) {
            for (AVObject person : list) {
                PersonEntity personEntity = new PersonEntity(person);
                PersonStore.getSharedInstance(getBaseContext()).savePerson(personEntity);
                LocalDataManager.getSharedInstance(getBaseContext()).addRecord(personEntity);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        drawerLayoutMenu = findViewById(R.id.drawer_layout);

        navView = (NavigationView) findViewById(R.id.nav_view);
        userName = navView.getHeaderView(0).findViewById(R.id.username);
        userEmail = navView.getHeaderView(0).findViewById(R.id.user_email);
        userAvatar = navView.getHeaderView(0).findViewById(R.id.avatar_img);
        userManager = UserManager.getSharedInstance(this);

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
                Intent intent;
                if (!userManager.isLogin()) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("提醒")
                            .setMessage("请先登录账号")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                                    startActivity(intent);
                                }
                            })
                            .create().show();
                    return false;
                } else {
                    switch (item.getItemId()) {
                        case R.id.nav_logout:
                            userManager.logout();
                            break;

                        case R.id.nav_following:
                            intent = new Intent(getBaseContext(), SocialActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.nav_bookmark:
                            intent = new Intent(getBaseContext(), BookmarkActivity.class);
                            startActivity(intent);
                            break;
                        case R.id.nav_personal_information:
                            intent = intent = new Intent(MainActivity.this, ProfileDetailActivity.class);
                            startActivity(intent);
                    }
                }
                navView.setCheckedItem(-1);
                return true;
            }
        });

        if (!UserManager.getSharedInstance(MainActivity.this).isLogin()) {
            navView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            onLogin();
        }


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
                        if (communityFragment == null) {
                            communityFragment = new CommunityFragment();
                        }
                        transaction.replace(R.id.fragment_container, communityFragment);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void LoginOrRegisterEvent(MessageEvent messageEvent) {

        if (messageEvent.messgae == Constants.EventType.EVENT_LOGIN
                || messageEvent.messgae == Constants.EventType.EVENT_SIGN_UP) {
            onLogin();
        } else if (messageEvent.messgae == Constants.EventType.EVENT_UPDATE_USER) {
            String url = userManager.getAvatarURL(userManager.currentUser());
            if (url != null) {
                Glide.with(this).load(url).into(userAvatar);
                userName.setText(userManager.getNickname(userManager.currentUser()));
            }
        }
    }

    private void onLogin() {
        View view= navView.getHeaderView(0);
        view.findViewById(R.id.sign_up_in).setVisibility(View.GONE);

        AVUser user = userManager.currentUser();
        String url = userManager.getAvatarURL(user);
        if (url != null) {
            Glide.with(this).load(url).into(userAvatar);
        }

        userName.setText(userManager.getNickname());

        String email = userManager.getEmail(userManager.currentUser());
        if (null != email) {
            userEmail.setText(email);
        }
        navView.getHeaderView(0).setClickable(false);
    }

    private void setupData() {
        LocalDataManager.getSharedInstance(this).clearAll();
        EventManager.getSharedInstance(this).fetchAllEventsFromLC(eventResponse);
        PersonManager.getSharedInstance(this).fetchAllPeopleFromLC(personResponse);

    }
}
