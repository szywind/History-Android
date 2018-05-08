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

    static final List<String> index2dynasty = Arrays.asList("夏", "商", "西周", "春秋", "战国", "秦", "西汉", "东汉", "三国", "西晋", "东晋", "南北朝",
            "隋", "唐", "五代十国", "北宋", "辽", "金", "南宋", "元", "明", "清");

    static final Map<String, Integer> dynasty2index;
    static
    {
        dynasty2index = new HashMap<String, Integer>();
        dynasty2index.put("夏", 0);
        dynasty2index.put("商", 1);
        dynasty2index.put("西周", 2);
        dynasty2index.put("春秋", 3);
        dynasty2index.put("战国", 4);
        dynasty2index.put("秦", 5);
        dynasty2index.put("西汉", 6);
        dynasty2index.put("东汉", 7);
        dynasty2index.put("三国", 8);
        dynasty2index.put("西晋", 9);
        dynasty2index.put("东晋", 10);
        dynasty2index.put("南北朝", 11);
        dynasty2index.put("隋", 12);
        dynasty2index.put("唐", 13);
        dynasty2index.put("五代十国", 14);
        dynasty2index.put("北宋", 15);
        dynasty2index.put("辽", 16);
        dynasty2index.put("金", 17);
        dynasty2index.put("南宋", 18);
        dynasty2index.put("元", 19);
        dynasty2index.put("明", 20);
        dynasty2index.put("清", 21);
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





