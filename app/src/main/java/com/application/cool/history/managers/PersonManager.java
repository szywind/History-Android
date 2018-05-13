package com.application.cool.history.managers;

import android.content.Context;
import android.util.Log;

import com.application.cool.history.constants.LCConstants;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
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
        fetchAllPeopleFromLC(delegate, true);
    }

    public void fetchAllPeopleFromLC(final PersonResponse delegate, boolean sortByPinyin) {
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.PersonKey.className);
        if (sortByPinyin) {
            query.orderByAscending(LCConstants.PersonKey.pinyin);
        }
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
    }
}
