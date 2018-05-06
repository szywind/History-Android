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
public class EventManager {
    private final static String TAG = "Event Manager";

    private Context context;

    private static EventManager sharedInstance = null;

    private EventManager(Context context){
        this.context = context;
    }

    public static EventManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new EventManager(context);
        }
        return sharedInstance;
    }

    public interface EventResponse {
        void processFinish(List<AVObject> list);
    }

    public void fetchAllEventsFromLC(final EventResponse delegate) {
        AVQuery<AVObject> query = new AVQuery<>(LCConstants.EventKey.className);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                if (e == null) {
                    delegate.processFinish(list);
                } else {
                    Log.e(TAG,"Fetch All Events Error: " + e.toString());
                }
            }
        });
    }
}
