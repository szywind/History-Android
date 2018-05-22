package com.application.cool.history.managers;

import android.content.Context;

import com.application.cool.history.db.EventEntity;
import com.application.cool.history.db.PersonEntity;
import com.application.cool.history.managers.DBManagers.EventStore;
import com.application.cool.history.managers.DBManagers.PersonStore;
import com.application.cool.history.models.Record;
import com.application.cool.history.models.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */
public class LocalDataManager {
    private final static String TAG = "LocalData Manager";

    private Context context;

    private static LocalDataManager sharedInstance = null;

    private LocalDataManager(Context context){
        this.context = context;
    }

    public List<Record> allPeople = new ArrayList<>();
    public List<Record> allEvents = new ArrayList<>();
    public List<Record> events = new ArrayList<>();
    public List<Record> geo = new ArrayList<>();
    public List<Record> art = new ArrayList<>();
    public List<Record> tech = new ArrayList<>();
    public List<Record> allRecords = new ArrayList<>();

    static final List<String> index2dynasty = Arrays.asList("三皇五帝", "夏", "商", "西周", "春秋", "战国", "秦", "西汉", "东汉", "三国", "西晋", "东晋", "南北朝",
            "隋", "唐", "五代十国", "北宋", "辽", "金", "南宋", "元", "明", "清");

    static final Map<String, Integer> dynasty2index;
    static
    {
        dynasty2index = new HashMap<String, Integer>();
        for (int i=0; i<index2dynasty.size(); i++) {
            dynasty2index.put(index2dynasty.get(i), i);
        }
    }



    public static LocalDataManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new LocalDataManager(context);
        }
        return sharedInstance;
    }

    public void setupEncyclopediaRecords() {
        allPeople = PersonStore.getSharedInstance(context).fetchAllPeople();
        allEvents = EventStore.getSharedInstance(context).fetchAllEvents();
        allRecords.addAll(allPeople);
        allRecords.addAll(allEvents);

        for(Record event: allEvents) {
            switch(event.getType()) {
                case "event":
                    events.add(event);
                    break;
                case "geography":
                    geo.add(event);
                    break;
                case "art":
                    art.add(event);
                    break;
                case "technology":
                    tech.add(event);
                    break;
                default:
                    break;
            }
        }
    }

    public void addRecord(EventEntity eventEntity) {
        Record record = new Record(eventEntity);
        allEvents.add(record);
        allRecords.add(record);
        switch(record.getType()) {
            case "event":
                events.add(record);
                break;
            case "geography":
                geo.add(record);
                break;
            case "art":
                art.add(record);
                break;
            case "technology":
                tech.add(record);
                break;
            default:
                break;
        }
    }

    public void addRecord(PersonEntity personEntity) {
        Record record = new Record(personEntity);
        allPeople.add(record);
        allRecords.add(record);
    }

    public List<Record> getFollowingTopics() {
        List<Record> followingTopics = new ArrayList<>();
        followingTopics.addAll(PersonStore.getSharedInstance(context).fetchFilteredPeople(State.currentSubscribeTopics));
        followingTopics.addAll(EventStore.getSharedInstance(context).fetchFilteredEvents(State.currentSubscribeTopics));

        return followingTopics;
    }

    public void clearAll() {
        allPeople.clear();
        allRecords.clear();
        allEvents.clear();
        events.clear();
        geo.clear();
        art.clear();
        tech.clear();
    }
}





