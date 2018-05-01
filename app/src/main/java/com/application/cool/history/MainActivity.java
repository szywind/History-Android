package com.application.cool.history;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.application.cool.history.fragment.EncyclopediaFragment;
import com.application.cool.history.fragment.ForumFragment;
import com.application.cool.history.fragment.SearchFragment;
import com.application.cool.history.fragment.TimelineFragment;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondarySwitchDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class MainActivity extends AppCompatActivity {


    private BottomNavigationBar bottomNavigationBar;
    private Drawer drawerMenu;

    private EncyclopediaFragment encyclopediaFragment;
    private ForumFragment forumFragment;
    private SearchFragment searchFragment;
    private TimelineFragment timelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.background)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName("cool")
                                .withEmail("niu199212cool@163.com")
                                .withIcon(R.drawable.nav_con),
                        new ProfileSettingDrawerItem()
                                .withName(R.string.create_new_account)
                                .withIcon(R.drawable.ic_add_black_24dp),
                        new ProfileSettingDrawerItem()
                                .withName(R.string.add_exist_account)
                                .withIcon(R.drawable.ic_person_add_black_24dp)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();


        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.personal_information)
                .withIcon(R.drawable.ic_person_black_24dp);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.following)
                .withIcon(R.drawable.ic_content_copy_black_24dp);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.my_publish)
                .withIcon(R.drawable.ic_my_publish);
        SecondarySwitchDrawerItem item4 = new SecondarySwitchDrawerItem().withName(R.string.night_mode);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withName(R.string.qr_code);
        SecondaryDrawerItem item6 = new SecondaryDrawerItem().withName(R.string.setting);
        SecondaryDrawerItem item7 = new SecondaryDrawerItem().withName(R.string.help_center);

        drawerMenu = new DrawerBuilder().withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .addDrawerItems(item1, item2, item3,
                        new DividerDrawerItem(), item4, item5, item6, item7)
                .withSelectedItem(-1)
                .withDrawerWidthDp(300)
                .build();

        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .setMode(BottomNavigationBar.MODE_FIXED)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .setActiveColor("#FF107FFD")
                .setInActiveColor("#e9e6e6");

        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_time_axis, R.string.timeline))
                .addItem(new BottomNavigationItem(R.drawable.ic_forum, R.string.forum))
                .addItem(new BottomNavigationItem(R.drawable.ic_encyclopdeic, R.string.encyclopedia))
                .addItem(new BottomNavigationItem(R.drawable.ic_search, R.string.search))
                .initialise();

        setDefaultFragment();
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

    private void setDefaultFragment() {
        if (timelineFragment == null) {
            timelineFragment = new TimelineFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, timelineFragment);
            getSupportFragmentManager().beginTransaction().commit();
        }
    }
}
