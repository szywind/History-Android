package com.application.cool.history.managers;

import android.content.Context;

import com.application.cool.history.constants.LCConstants;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;

import java.util.List;

public class SocialManager {
    private final static String TAG = "Social Manager";

    private Context context;

    private static SocialManager sharedInstance = null;

    private SocialManager(Context context) {
        this.context = context;
    }

    public static SocialManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new SocialManager(context);
        }
        return sharedInstance;
    }

    public interface SocialResponse {
        void processFinish(List<AVUser> list);
    }

    public void fetchAllFollowees(AVUser user, final SocialResponse delegate) {
        AVQuery<AVUser> query = AVUser.followeeQuery(user.getObjectId(), AVUser.class);
        query.include(LCConstants.FolloweeKey.followee);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                }
            }
        });
    }

    public void fetchAllFollowers(AVUser user, final SocialResponse delegate) {
        AVQuery<AVUser> query = AVUser.followerQuery(user.getObjectId(), AVUser.class);
        query.include(LCConstants.FollowerKey.follower);
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                }
            }
        });
    }

    public void checkExistFollowee(AVUser user, final SocialResponse delegate) {
        AVQuery<AVUser> query = AVUser.followeeQuery(AVUser.getCurrentUser().getObjectId(), AVUser.class);
        query.whereEqualTo(LCConstants.FolloweeKey.followee, user.getObjectId());
        query.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                }
            }
        });

    }

}
