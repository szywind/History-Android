package com.example.greendaogenerator;

import org.greenrobot.greendao.generator.DaoGenerator;
import org.greenrobot.greendao.generator.Entity;
import org.greenrobot.greendao.generator.Schema;

public class MyGenerator {
    public static void main(String[] args) {
        Schema schema = new Schema(1, "com.application.cool.history.db"); // Your app package name and the (.db) is the folder where the DAO files will be generated into.
        schema.enableKeepSectionsByDefault();

        addTables(schema);

        try {
            new DaoGenerator().generateAll(schema,"./app/src/main/java");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addTables(final Schema schema) {
        addPersonEntities(schema);
        addEventEntities(schema);
    }

    // This is use to describe the colums of your table
    private static Entity addPersonEntities(final Schema schema) {
        Entity person = schema.addEntity("Person");
//        person.addIdProperty().primaryKey().autoincrement();
        person.addIntProperty("person_id").primaryKey().notNull();
        person.addStringProperty("avatar_url");
        person.addStringProperty("info_url");
        person.addStringProperty("name");
        person.addStringProperty("type");
        person.addIntProperty("start");
        person.addIntProperty("end");
        person.addStringProperty("dynasty");
        person.addStringProperty("dynasty_detail");
        person.addStringProperty("pinyin");

        return person;
    }

    private static Entity addEventEntities(final Schema schema) {
        Entity event = schema.addEntity("Event");
//        person.addIdProperty().primaryKey().autoincrement();
        event.addIntProperty("event_id").primaryKey().notNull();
        event.addStringProperty("avatar_url");
        event.addStringProperty("info_url");
        event.addStringProperty("name");
        event.addStringProperty("type");
        event.addIntProperty("start");
        event.addIntProperty("end");
        event.addStringProperty("dynasty");
        event.addStringProperty("dynasty_detail");
        event.addStringProperty("pinyin");

        return event;
    }
}