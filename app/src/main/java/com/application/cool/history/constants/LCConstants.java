package com.application.cool.history.constants;

/**
 * Created by Zhenyuan Shen on 05/06/2018.
 */

public interface LCConstants {

    /**
     *  General keys are used in every tables and every objects.
     *
     *  - important: DO NOT modify this struct.
     */
    interface GeneralKey {
        String objectId = "objectId";
        String updateAt = "updatedAt";
        String createAt = "createdAt";
    }

    /**
     *  Keys used in "_User" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface UserKey {
        String gender = "gender";
        String nickname = "nickname";
        String phone = "mobilePhoneNumber";
        String email = "email";
        String accountType = "accountType";
        String location = "geoPoint";
        String username = "username";
        String avatarURL = "avatarURL";
        String followers = "followers";
        String followees = "followees";
        String subscribeTopics = "subscribeTopics";
        String subscribeList = "subscribeList";
        String likeList = "likeList";
        String dislikeList = "dislikeList";
        String replyList = "replyList";
    }

    /**
     *  Keys used in "Person" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface PersonKey {
        String className = "Person";

        String name = "name";
        String type = "type";
        String start = "start";
        String end = "end";
        String dynasty = "dynasty";
        String dynasty_detail = "dynastyDetail";
        String pinyin = "pinyin";
        String avatarURL = "avatarURL";
        String infoURL = "infoURL";
    }

    /**
     *  Keys used in "Event" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface EventKey {
        String className = "Event";

        String name = "name";
        String type = "type";
        String start = "start";
        String end = "end";
        String dynasty = "dynasty";
        String dynasty_detail = "dynastyDetail";
        String pinyin = "pinyin";
        String avatarURL = "avatarURL";
        String infoURL = "infoURL";
    }

    /**
     *  Keys used in "_Followee" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface FolloweeKey {
        String followee = "followee";
    }

    /**
     *  Keys used in "_Follower" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface FollowerKey {
        String follower = "follower";
    }

    /**
     *  Keys used in "Post" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface PostKey {
        String className = "Post";

        String authorId = "authorId";
        String dislikes = "dislikes";
        String imageURL = "imageURL";
        String likes = "likes";
        String replies = "replies";
        String subscribers = "subscribers";
        String textURL = "textURL";
        String title = "title";
        String dynasty = "dynasty";
        String type = "type";
        String topic = "topic";
        String subtopic = "subtopic";
        String reviews = "reviews";
    }

    /**
     *  Keys used in "Reply" table.
     *
     *  - important: DO NOT modify this struct.
     */
    interface ReplyKey {
        String className = "Reply";

        String authorId = "authorId";
        String dislikes = "dislikes";
        String likes = "likes";
        String replies = "replies";
        String postId = "postId";
        String text = "text";
        String referenceId = "referenceId";
    }

}
