package com.application.cool.history.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.application.cool.history.MainActivity;
import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.constants.LCConstants;
import com.application.cool.history.models.State;
import com.application.cool.history.util.SimpleLocation;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.avos.avoscloud.SaveCallback;

import java.io.IOException;
import java.util.List;

import static com.application.cool.history.managers.LocationManager.isInvalid;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */
public class UserManager implements Constants {
    private final static String TAG = "User Manager";

    private SharedPreferences pref;

    private Context context;

    private static UserManager sharedInstance = null;

    public AVUser currentUser(){
        return AVUser.getCurrentUser();
    }

    private UserManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(SharedPref.PREF_NAME, Context.MODE_PRIVATE);
    }

    public static UserManager getSharedInstance(Context context){

        if(sharedInstance == null){
            sharedInstance = new UserManager(context);
        }
        return sharedInstance;
    }

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public interface UserResponse {
        void processFinish(String output);
        void processFinish(List<AVUser> list);
    }


    // Create login session
    public void createLoginSession(String uid, String nickname, String type){
        SharedPreferences.Editor editor = pref.edit();
        // Storing login value as TRUE
        editor.putBoolean(SharedPref.PREF_IS_LOGIN, true);
        editor.putString(SharedPref.PREF_USER_ID, uid);
        editor.putString(SharedPref.PREF_ACC_TYPE, type);
        editor.putString(SharedPref.PREF_NICKNAME, nickname);
        editor.putString(SharedPref.PREF_IMG_NAME, Default.defaultAvatar);

        // commit changes
        editor.apply();
    }


    /* 检查是否已经登录 */
    public boolean isLogin() {
        return pref.getBoolean(SharedPref.PREF_IS_LOGIN, false);
    }


    /* 获取当前用户的信息 */
    public String getUserId() {
        return pref.getString(SharedPref.PREF_USER_ID, null);
    }
    public String getAccountType() {
        return pref.getString(SharedPref.PREF_ACC_TYPE, null);
    }
    public String getNickname() {
        return pref.getString(SharedPref.PREF_NICKNAME, null);
    }
//    public UserInfo getCurrentUser() {
//        UserInfo currentUser = new UserInfo(getNickname(), getAccountType());
//        return currentUser;
//    }


    /* 获取LeanCloud上用户的信息 */
    public String getUserId(AVUser user) {
        return user.getObjectId();
    }
    public String getAccountType(AVUser user) {
        return user.getString(LCConstants.UserKey.accountType);
    }
    public String getNickname(AVUser user){
        return user.getString(LCConstants.UserKey.nickname);
    }
    public String getEmail(AVUser user) {
        return user.getEmail();
    }
    public String getPhone(AVUser user) {
        return user.getMobilePhoneNumber();
    }
    public String getAvatarURL(AVUser user) {
        return user.getString(LCConstants.UserKey.avatarURL);
    }

    public List<String> getSubscribeTopics(AVUser user) {
        List<String> list = (List<String>) user.get(LCConstants.UserKey.subscribeTopics);
        return list;
    }
    public List<String> getSubscribeList(AVUser user) {
        List<String> list = (List<String>) user.get(LCConstants.UserKey.subscribeList);
        return list;
    }
    public List<String> getLikeList(AVUser user) {
        List<String> list = (List<String>) user.get(LCConstants.UserKey.likeList);
        return list;
    }
    public List<String> getDislikeList(AVUser user) {
        List<String> list = (List<String>) user.get(LCConstants.UserKey.dislikeList);
        return list;
    }
    public List<String> getReplyList(AVUser user) {
        List<String> list = (List<String>) user.get(LCConstants.UserKey.replyList);
        return list;
    }
    public List<String> getList(AVUser user, String key) {
        List<String> list = (List<String>) user.get(key);
        return list;
    }


    public AVGeoPoint getUserLocation(AVUser user) {
        return user.getAVGeoPoint(LCConstants.UserKey.location);
    }


    /* 更新LeanCloud上用户的信息 */
    public void setNickname(String nickname) {
        currentUser().put(LCConstants.UserKey.nickname, nickname);
    }

    public void setSubscribeTopics(SaveCallback saveCallback) {
        currentUser().put(LCConstants.UserKey.subscribeTopics, State.currentSubscribeTopics.toArray());
        currentUser().saveInBackground(saveCallback);
    }
    public void setSubscribeList(String[] list, SaveCallback saveCallback) {
        currentUser().put(LCConstants.UserKey.subscribeList, list);
        currentUser().saveInBackground(saveCallback);
    }
    public void setLikeList(String[] list, SaveCallback saveCallback) {
        currentUser().put(LCConstants.UserKey.likeList, list);
        currentUser().saveInBackground(saveCallback);
    }
    public void setDislikeList(String[] list, SaveCallback saveCallback) {
        currentUser().put(LCConstants.UserKey.dislikeList, list);
        currentUser().saveInBackground(saveCallback);
    }
    public void setReplyList(String[] list, SaveCallback saveCallback) {
        currentUser().put(LCConstants.UserKey.replyList, list);
        currentUser().saveInBackground(saveCallback);
    }
    public void setList(String[] list, String key, SaveCallback saveCallback) {
        currentUser().put(key, list);
        currentUser().saveInBackground(saveCallback);
    }

    public AVGeoPoint setUserLocation() {
        SimpleLocation loc = new SimpleLocation(context);
        SimpleLocation.Point pt = loc.getPosition();
        AVGeoPoint curLocation = new AVGeoPoint();
        if (!isInvalid(pt)) {
            curLocation = new AVGeoPoint(pt.latitude, pt.longitude);
            currentUser().put(LCConstants.UserKey.location, curLocation);
        }
        // currentUser().saveInBackground(saveCallback);
        return curLocation;
    }


    // save profile leancloud
    public void saveMyProfile(String nickname, String phone, String email, String accountType, final UserResponse delegate){
        currentUser().put(LCConstants.UserKey.nickname, nickname);
        currentUser().put(LCConstants.UserKey.phone, phone);
        currentUser().put(LCConstants.UserKey.email, email);
        currentUser().put(LCConstants.UserKey.accountType, accountType);
        setUserLocation();
        saveMyProfile(delegate);
    }

    public void saveMyProfile(final UserResponse delegate){
        currentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    // no error
                    Toast.makeText(context, context.getResources().getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                    delegate.processFinish("Done");
                }
            }
        });
    }

    // store profile
    public void saveProfile(AVObject needStoreProfileLC, final UserResponse delegate){
        needStoreProfileLC.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null) {
                    delegate.processFinish("Done");
                }
            }
        });
    }

    public void resetPasswordWithEmail(String email, RequestPasswordResetCallback resetCallback){
        AVUser.requestPasswordResetInBackground(email, resetCallback);
    }

    public void logout() {
        SharedPreferences.Editor editor = pref.edit();
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.apply();
        currentUser().logOut();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(context, MainActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        context.startActivity(i);
    }


    public void searchUserFromLC(String key, String value, final UserResponse delegate) {
        AVQuery<AVUser> query = AVUser.getQuery();
//        query.whereEqualTo(key, value);
        query.whereContains(key, value);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                }
            }
        });
    }

    public void findUser(String key, String value, FindCallback<AVUser> findCallback) {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.whereEqualTo(key, value);
        query.findInBackground(findCallback);
    }

    public void findHotUsers(int base, int pageSize, FindCallback<AVUser> findCallback) {
        AVQuery<AVUser> query = AVUser.getQuery();
        query.orderByDescending(LCConstants.UserKey.followers);
        query.limit(pageSize);
        query.skip(base);
        query.findInBackground(findCallback);
    }

    public void updateCounter(final String key, final int amount) {
        currentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e != null){
                    // error
                    Log.d(TAG, "Update Counter Error: " + e.toString());
                }else{
                    // no error
                    currentUser().increment(key, amount);
                    currentUser().setFetchWhenSave(true);
                    currentUser().saveInBackground();
                }
            }
        });
    }

    public void updateUser(final String nickname, String imgPath, final SaveCallback saveCallback) {

        try {
            AvatarManager.getSharedInstance(context).updateAvatarWithImage(imgPath, new SaveCallback() {
                @Override
                public void done(AVException e) {}
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateUser(nickname, saveCallback);
    }

    public void updateUser(final String nickname, final SaveCallback saveCallback) {
        setNickname(nickname);
        setUserLocation();
        currentUser().saveInBackground(saveCallback);
    }
}
