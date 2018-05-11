package com.application.cool.history.managers;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;

import com.application.cool.history.constants.Constants;
import com.application.cool.history.util.SimpleLocation;
import com.avos.avoscloud.AVGeoPoint;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    public String getAddressFromLoc(SimpleLocation loc){
        String address = Constants.Default.defaultString;
        // if we can't access the location yet
        if (!loc.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(context);
        }
        SimpleLocation.Point pt = loc.getPosition();

        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());
//        StringBuilder builder = new StringBuilder();
        List<String> addressStr = new ArrayList<>();
        try {
            List<Address> addressDict = geoCoder.getFromLocation(pt.latitude, pt.longitude, 1);
            if(!address.isEmpty()) {

                int maxLineIndex = addressDict.get(0).getMaxAddressLineIndex();
                for (int i = 0; i < Math.max(1,maxLineIndex); i++) {
                    addressStr.add(addressDict.get(0).getAddressLine(i));
                }
                address = TextUtils.join(" ", addressStr); //This is the complete address.
            }
        } catch (IOException | NullPointerException ignored) {}
        return address;
    }
}
