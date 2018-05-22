package com.application.cool.history.managers;

import android.content.Context;
import android.util.Log;

import com.application.cool.history.constants.LCConstants;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;

import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */
public class PersonManager {
    private final static String TAG = "Person Manager";

    private Context context;

    private static PersonManager sharedInstance = null;

    private PersonManager(Context context){
        this.context = context;
    }

    public static PersonManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new PersonManager(context);
        }
        return sharedInstance;
    }

    public interface PersonResponse {
        void processFinish(List<AVObject> list);
    }

    public void fetchAllPeopleFromLC(final PersonResponse delegate) {
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.PersonKey.className);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int number, AVException e) {
                if (e == null) {
                    fetchAllPeopleFromLC(number, delegate);
                } else {
                    Log.e(TAG, "error" + e.toString());
                }
            }
        });
    }

    public void fetchAllPeopleFromLC(int number, final PersonResponse delegate) {
        int base = 0;
        int limit = 100;
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.PersonKey.className);

        query.limit(limit);

        while (base < number) {
            query.skip(base);
            query.findInBackground(new FindCallback<AVObject>() {
                @Override
                public void done(List<AVObject> list, AVException e) {
                    if (e == null) {
                        delegate.processFinish(list);
                    } else {
                        Log.e(TAG,"Fetch All People Error: " + e.toString());
                    }
                }
            });
            base = base + limit;
        }
    }
}
