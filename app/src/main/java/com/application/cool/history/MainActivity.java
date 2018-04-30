package com.application.cool.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

        Drawer result = new DrawerBuilder().withActivity(this)
                .withAccountHeader(headerResult)
                .withActionBarDrawerToggle(true)
                .withTranslucentStatusBar(false)
                .withToolbar(toolbar)
                .addDrawerItems(item1, item2, item3,
                        new DividerDrawerItem(), item4, item5, item6, item7)
                .withSelectedItem(-1)
                .withDrawerWidthDp(300)
                .build();
    }
}
