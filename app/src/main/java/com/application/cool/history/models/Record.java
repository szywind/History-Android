package com.application.cool.history.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.application.cool.history.db.EventEntity;
import com.application.cool.history.db.PersonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */

public class Record implements Parcelable {

    /**
     * 用于表示百科中的记录词条，对应本地DB和LeanCloud DB中的Person表和Event表
     * 例子：
     *
     * dynasty_detail : 周D王
     * dynasty : 春秋
     * name : 孔子
     * pinyin : kongzi
     * start : -551
     * type : person
     * end : -479
     * avatarURL : http://lc-FCHudlon.cn-n1.lcfile.com/192972c3841dda83fcc4.jpg
     * infoURL : http://lc-FCHudlon.cn-n1.lcfile.com/61bc7320cbc584824603.txt
     * objectId : 5aa959ed2f301e0036541e74
     */

    private String dynastyDetail;
    private String dynasty;
    private String name;
    private String pinyin;
    private int start;
    private String type;
    private int end;
    private String avatarURL;
    private String infoURL;
    private String objectId;

    public Record(String objectId, String avatarURL, String infoURL, String name, String type,
                  int start, int end, String dynasty, String dynastyDetail, String pinyin) {
        this.objectId = "_" + objectId;
        this.avatarURL = avatarURL;
        this.infoURL = infoURL;
        this.name = name;
        this.type = type;
        this.start = start;
        this.end = end;
        this.dynasty = dynasty;
        this.dynastyDetail = dynastyDetail;
        this.pinyin = pinyin;
    }

    public Record(EventEntity event) {
        this.objectId = "e" + event.getEventId();
        this.avatarURL = event.getAvatarURL();
        this.infoURL = event.getInfoURL();
        this.name = event.getName();
        this.type = event.getType();
        this.start = event.getStart();
        this.end = event.getEnd();
        this.dynasty = event.getDynasty();
        this.dynastyDetail = event.getDynastyDetail();
        this.pinyin = event.getPinyin();
    }

    public Record(PersonEntity person) {
        this.objectId = "e" + person.getPersonId();
        this.avatarURL = person.getAvatarURL();
        this.infoURL = person.getInfoURL();
        this.name = person.getName();
        this.type = person.getType();
        this.start = person.getStart();
        this.end = person.getEnd();
        this.dynasty = person.getDynasty();
        this.dynastyDetail = person.getDynastyDetail();
        this.pinyin = person.getPinyin();
    }

    static public List<Record> getRecordsWithEvents(List<EventEntity> eventEntities) {
        List<Record> records = new ArrayList<Record>();
        for (EventEntity eventEntity: eventEntities) {
            records.add(new Record(eventEntity));
        }
        return records;
    }

    static public List<Record> getRecordsWithPeople(List<PersonEntity> personEntities) {
        List<Record> records = new ArrayList<Record>();
        for (PersonEntity personEntity: personEntities) {
            records.add(new Record(personEntity));
        }
        return records;
    }

    public String getDynastyDetail() {
        return dynastyDetail;
    }

    public String getDynasty() {
        return dynasty;
    }

    public String getName() {
        return name;
    }

    public String getPinyin() {
        return pinyin;
    }

    public int getStart() {
        return start;
    }

    public String getType() {
        return type;
    }

    public int getEnd() {
        return end;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public String getInfoURL() {
        return infoURL;
    }

    public String getObjectId() {
        return objectId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.dynastyDetail);
        dest.writeString(this.dynasty);
        dest.writeString(this.name);
        dest.writeString(this.pinyin);
        dest.writeInt(this.start);
        dest.writeString(this.type);
        dest.writeInt(this.end);
        dest.writeString(this.avatarURL);
        dest.writeString(this.infoURL);
        dest.writeString(this.objectId);
    }

    protected Record(Parcel in) {
        this.dynastyDetail = in.readString();
        this.dynasty = in.readString();
        this.name = in.readString();
        this.pinyin = in.readString();
        this.start = in.readInt();
        this.type = in.readString();
        this.end = in.readInt();
        this.avatarURL = in.readString();
        this.infoURL = in.readString();
        this.objectId = in.readString();
    }

    public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel source) {
            return new Record(source);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };
}
