package com.application.cool.history.managers.DBManagers;

import android.content.Context;

import com.application.cool.history.db.DaoMaster;
import com.application.cool.history.db.DaoSession;

/**
 * Created by Zhenyuan Shen on 5/7/18.
 */
public class LocalStore {
    public DaoSession daoSession;
    public DaoMaster.DevOpenHelper helper;

    public void initDB(Context context) {
        helper = new DaoMaster.DevOpenHelper(context, "history.sqlite", null);
        DaoMaster daoMaster = new DaoMaster(helper.getReadableDatabase());
        daoSession = daoMaster.newSession();
    }

    public void closeDatabase()
    {
        daoSession.clear();
        helper.close();
    }
}
