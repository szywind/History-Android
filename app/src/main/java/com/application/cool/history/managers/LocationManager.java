package com.application.cool.history.managers;

import android.content.Context;

import com.application.cool.history.util.SimpleLocation;
import com.avos.avoscloud.AVGeoPoint;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */
public class LocationManager {

    private Context context;

    private static LocationManager sharedInstance = null;

    private LocationManager(Context context){
        this.context = context;
    }

    public static LocationManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new LocationManager(context);
        }
        return sharedInstance;
    }

    static public SimpleLocation.Point cvtAVGeopointToPoint(AVGeoPoint loc) {
        return new SimpleLocation.Point(loc.getLatitude(), loc.getLongitude());
    }

    static public boolean isInvalid(SimpleLocation.Point loc) {
        return loc == null || (loc.latitude== 0 && loc.longitude == 0);
    }

    static public boolean isInvalid(AVGeoPoint loc) {
        return loc == null || (loc.getLatitude() ==0 && loc.getLongitude() == 0);
    }
}
