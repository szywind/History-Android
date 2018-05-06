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
        addPostEntities(schema);
    }

    // This is use to describe the colums of your table
    private static Entity addPersonEntities(final Schema schema) {
        Entity person = schema.addEntity("PersonEntity");
//        person.addIdProperty().primaryKey().autoincrement();
        person.addStringProperty("personId").primaryKey().notNull();
        person.addStringProperty("avatarURL");
        person.addStringProperty("infoURL");
        person.addStringProperty("name");
        person.addStringProperty("type");
        person.addIntProperty("start");
        person.addIntProperty("end");
        person.addStringProperty("dynasty");
        person.addStringProperty("dynastyDetail");
        person.addStringProperty("pinyin");

        return person;
    }

    private static Entity addEventEntities(final Schema schema) {
        Entity event = schema.addEntity("EventEntity");
//        event.addIdProperty().primaryKey().autoincrement();
        event.addStringProperty("eventId").primaryKey().notNull();
        event.addStringProperty("avatarURL");
        event.addStringProperty("infoURL");
        event.addStringProperty("name");
        event.addStringProperty("type");
        event.addIntProperty("start");
        event.addIntProperty("end");
        event.addStringProperty("dynasty");
        event.addStringProperty("dynastyDetail");
        event.addStringProperty("pinyin");

        return event;
    }

    private static Entity addPostEntities(final Schema schema) {
        Entity post = schema.addEntity("PostEntity");
//        person.addIdProperty().primaryKey().autoincrement();
        post.addStringProperty("postId").primaryKey().notNull();
        post.addStringProperty("imageURL");
        post.addStringProperty("textURL");
        post.addStringProperty("title");
        post.addStringProperty("type");
        post.addStringProperty("authorId");
        post.addStringProperty("dynasty");
        post.addStringProperty("topic");
        post.addStringProperty("subtopic");
        post.addIntProperty("likes");
        post.addIntProperty("dislikes");
        post.addIntProperty("subscribers");
        post.addIntProperty("replies");
        post.addIntProperty("reviews");

        return post;
    }
}
