package com.application.cool.history.constants;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */

public interface Constants {

    interface Default {
        String defaultInfo = "不详";
        String defaultAvatar = "default";
        int defaultUsernameLimit = 20;
        int defaultSmsCodeLength = 6;
        int defaultPasswordLimit = 8;
        String defaultString = "None";
    }

    /*************************
     * Shared Preference
     *************************/
    interface SharedPref {
        String PREF_NAME = "history";

        // Name in shared preference.
        String PREF_USER_ID = LCConstants.GeneralKey.objectId;
        String PREF_IMG_NAME = LCConstants.UserKey.avatarURL;
        String PREF_ACC_TYPE = LCConstants.UserKey.accountType;
        String PREF_NICKNAME = LCConstants.UserKey.nickname;
        String PREF_SNS_TOKEN = "SnsToken";
        String PREF_IS_LOGIN = "IsLoggedIn";
        String PREF_APP_IS_VISIBLE = "AppIsVisible";
        String PREF_NOTIFICATION = "Notification";
    }

    /***********************
     * Permission required
     **********************/
    interface Permission {
        int CODE_LOCATION_PERMISSION_REQUEST = 0;
        int CODE_CAMERA_PERMISSION_REQUEST = 0;
        int CODE_READ_STORAGE_PERMISSION_REQUEST = 0;
    }
    /***********************
     * enum
     **********************/

    enum MessageDirection {
        incoming, outgoing
    }

    enum EContactType {
        E_PHONE,
        E_EMAIL,
        E_WECHAT
    }

    interface EventType {
        int EVENT_LOGIN = 10001;
        int EVENT_SIGN_UP = 10002;
    }

}

