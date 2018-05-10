package com.application.cool.history.managers;

import android.content.Context;
import android.util.Log;

import com.application.cool.history.constants.LCConstants;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.SaveCallback;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */
public class PostManager {
    private final static String TAG = "Post Manager";

    private Context context;

    private static PostManager sharedInstance = null;

    private PostManager(Context context){
        this.context = context;
    }

    public static PostManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new PostManager(context);
        }
        return sharedInstance;
    }

    public interface PostResponse {
        void processFinish(List<AVObject> list);
    }

    /* get function */
    public String getTitle(AVObject post) {
        return post.getString(LCConstants.PostKey.title);
    }
    public String getImageURL(AVObject post) {
        return post.getString(LCConstants.PostKey.imageURL);
    }
    public String getTextURL(AVObject post) {
        return post.getString(LCConstants.PostKey.textURL);
    }
    public int getLikes(AVObject post) {
        return post.getInt(LCConstants.PostKey.likes);
    }
    public int getDislikes(AVObject post) {
        return post.getInt(LCConstants.PostKey.dislikes);
    }
    public int getSubscribers(AVObject post) {
        return post.getInt(LCConstants.PostKey.subscribers);
    }
    public int getReplies(AVObject post) {
        return post.getInt(LCConstants.PostKey.replies);
    }
    public int getReviews(AVObject post) {
        return post.getInt(LCConstants.PostKey.reviews);
    }


    public void getAuthor(AVObject post, FindCallback<AVUser> findCallback) {
        String authorId = post.getString(LCConstants.PostKey.authorId);

        UserManager.getSharedInstance(context).findUser(LCConstants.GeneralKey.objectId, authorId, findCallback);
    }

    /* update counters including likes, dislikes, subscribers, replies and reviews */
    public void updateCounter(final String key, final int amount, final AVObject post) {
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e != null) {
                    // error
                    Log.d(TAG, "Update Counter Error: " + e.toString());
                } else {
                    // no error
                    post.increment(key, amount);
                    post.setFetchWhenSave(true);
                    post.saveInBackground();
                }
            }
        });
    }

    public void fetchAllPostsFromLC(final PostResponse delegate) {
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.PostKey.className);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                } else {
                    Log.e(TAG,"Fetch All Posts Error: " + e.toString());
                }
            }
        });
    }

    public void fetchPostFromLC(String key, String value, final PostResponse delegate) {
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.PostKey.className);
        query.whereEqualTo(key, value);
        query.orderByDescending(LCConstants.GeneralKey.createAt);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                } else {
                    Log.e(TAG,"Fetch Specific Posts Error: " + e.toString());
                }
            }
        });
    }

    public void searchPostFromLC(String key, String value, final PostResponse delegate) {
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.PostKey.className);
        query.whereContains(key, value);
        query.orderByDescending(LCConstants.GeneralKey.createAt);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                } else {
                    Log.e(TAG,"Find Posts Error: " + e.toString());
                }
            }
        });
    }
}

