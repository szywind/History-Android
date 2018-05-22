package com.application.cool.history.managers.DBManagers;

import android.content.Context;

import com.application.cool.history.db.EventEntity;
import com.application.cool.history.db.EventEntityDao;
import com.application.cool.history.models.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Zhenyuan Shen on 5/7/18.
 */
public class EventStore extends LocalStore {

    private static EventStore sharedInstance = null;

    private EventStore(Context context){
        initDB(context);
    }

    public static EventStore getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new EventStore(context);
        }
        return sharedInstance;
    }

    private static EventEntityDao eventDao;

    public void initDB(Context context) {
        if (daoSession == null) {
            super.initDB(context);
        }
        eventDao = daoSession.getEventEntityDao();
    }

    public void saveEvent(EventEntity eventEntity) {
        eventDao.insertOrReplace(eventEntity);
    }

    public List<Record> fetchAllEvents() {
        List<EventEntity> eventEntities = eventDao.queryBuilder()
                .orderAsc(EventEntityDao.Properties.Pinyin).list();
        return Record.getRecordsWithEvents(eventEntities);
    }

    public List<Record> fetchFilteredEvents(String type) {
        List<EventEntity> eventEntities = eventDao.queryBuilder()
                .where(EventEntityDao.Properties.Type.eq(type)).list();
        return Record.getRecordsWithEvents(eventEntities);
    }

    public List<Record> fetchFilteredEvents(Set<String> topics) {
        List<EventEntity> eventEntities = eventDao.queryBuilder()
                .where(EventEntityDao.Properties.Name.in(topics)).list();
        return Record.getRecordsWithEvents(eventEntities);
    }
}
