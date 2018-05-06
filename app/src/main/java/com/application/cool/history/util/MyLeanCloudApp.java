package com.application.cool.history.util;

import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class MyLeanCloudApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        AVOSCloud.initialize(this,"FCHudlonDJ4mzppWt6xSuRH7-gzGzoHsz",
                "Wf1tUQlil0vn7FPeleN1KCyi");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
