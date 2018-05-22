package com.application.cool.history.managers.DBManagers;

import android.content.Context;

import com.application.cool.history.db.PersonEntity;
import com.application.cool.history.db.PersonEntityDao;
import com.application.cool.history.models.Record;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Zhenyuan Shen on 5/7/18.
 */
public class PersonStore extends LocalStore {

    private static PersonStore sharedInstance = null;

    private PersonStore(Context context) {
        initDB(context);
    }

    public static PersonStore getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new PersonStore(context);
        }
        return sharedInstance;
    }

    private static PersonEntityDao personDao;

    public void initDB(Context context) {
        if (daoSession == null) {
            super.initDB(context);
        }
        personDao = daoSession.getPersonEntityDao();
    }

    public void savePerson(PersonEntity personEntity) {
        personDao.insertOrReplace(personEntity);
    }

    public List<Record> fetchAllPeople() {
        List<PersonEntity> personEntities = personDao.queryBuilder()
                .orderAsc(PersonEntityDao.Properties.Pinyin).list();
        return Record.getRecordsWithPeople(personEntities);
    }

    public List<Record> fetchFilteredPeople(String type) {
        List<PersonEntity> personEntities = personDao.queryBuilder()
                .where(PersonEntityDao.Properties.Type.eq(type)).list();
        return Record.getRecordsWithPeople(personEntities);
    }

    public List<Record> fetchFilteredPeople(Set<String> topics) {

        List<PersonEntity> personEntities = personDao.queryBuilder()
                .where(PersonEntityDao.Properties.Name.in(topics)).list();
        return Record.getRecordsWithPeople(personEntities);
    }
}