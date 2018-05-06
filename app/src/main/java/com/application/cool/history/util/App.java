package com.application.cool.history.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.application.cool.history.constants.Constants;
import com.application.cool.history.constants.ImportantSettings;
import com.avos.avoscloud.AVOSCloud;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by Zhenyuan Shen on 05/06/18.
 *
 * This App extends Application class.
 * Use this class to get general information such as UserID.
 *
 */

public class App extends Application implements Constants {

    private static App app;

    private static SharedPreferences pref;

    @Override
    public void onCreate() {

        super.onCreate();

        app = this;

        pref = this.getSharedPreferences(SharedPref.PREF_NAME, Context.MODE_PRIVATE);

        AVOSCloud.initialize(this, ImportantSettings.leanCloudId(), ImportantSettings.leanCloudKey());

        // china/us
        if (ImportantSettings.USE_US_CLUSTER) {
            AVOSCloud.useAVCloudUS();
        } else {
            AVOSCloud.useAVCloudCN();
        }
        initImageLoader(app);

        // open leancloud log
//        AVOSCloud.setDebugLogEnabled(true);


    }

    public static synchronized App getInstance() {
        return app;
    }

    public boolean isConnected() {

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

//    public static List<String> getBlockList() {
//        List<String> list;
//        Set<String> set = pref.getStringSet(PREF_BLOCK_LIST, null);
//
//        if(set == null) list=new ArrayList<>();
//        else list = new ArrayList<>(set);
//
//        while (list.contains("")) list.remove("");
//
//        return list;
//    }
//
//    public static void setBlockList(List<String > list) {
//        SharedPreferences.Editor editor = pref.edit();
//
//        while (list.contains("")) list.remove("");
//
//        Set<String> set = new HashSet<>(list);
//        editor.putStringSet(PREF_BLOCK_LIST, set);
//        editor.apply();
//    }

    public static String  getSnsToken() {
        return pref.getString(SharedPref.PREF_SNS_TOKEN, null);
    }

    public static void setSnsToken(String s) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SharedPref.PREF_SNS_TOKEN, s);
        editor.apply();
    }

    public static boolean getAppVisible() {
        return pref.getBoolean(SharedPref.PREF_APP_IS_VISIBLE, true);
    }

    public static void setAppVisible(boolean b) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(SharedPref.PREF_APP_IS_VISIBLE, b);
        editor.apply();
    }

    public static String getImageName() {
        return pref.getString(SharedPref.PREF_IMG_NAME, null);
    }

    public static void setImageName(String name) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(SharedPref.PREF_IMG_NAME, name);
        editor.apply();
    }

    public static boolean getNotification() {
        return pref.getBoolean(SharedPref.PREF_NOTIFICATION, true);
    }

    public static void setNotification(boolean b) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(SharedPref.PREF_NOTIFICATION, b);
        editor.apply();
    }

//    public static UserInfo getUser() {
//        String s = pref.getString(SharedPref.PREF_User, null);
//        if (s == null) {
//            return null;
//        } else {
//            return new Gson().fromJson(s, UserInfo.class);
//        }
//    }
//
//    public static void setUser(UserInfo UserInfo) {
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString(SharedPref.PREF_User, new Gson().toJson(UserInfo));
//        editor.apply();
//    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPoolSize(3).threadPriority(Thread.NORM_PRIORITY - 2)
                //.memoryCache(new WeakMemoryCache())
                .denyCacheImageMultipleSizesInMemory()
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View v = activity.getCurrentFocus();
        if (v != null) {
            inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
}
