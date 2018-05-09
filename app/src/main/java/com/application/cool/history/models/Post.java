package com.application.cool.history.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.application.cool.history.db.PostEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */

public class Post implements Parcelable {

    /**
     * 用于表示社区中的文章，对应LeanCloud DB中的Post表，暂时没有使用
     * 例子：
     *
     * imageURL : http://lc-FCHudlon.cn-n1.lcfile.com/a5211feec7d212cb7058.jpg
     * authorId : 5ae6768cee920a00431886c8
     * dislikes : 0
     * subscribers : 0
     * dynasty : 春秋
     * type : person
     * likes : 0
     * title : 七十而从心所欲不逾矩——失败与荣耀： 世界的孔子_《先师孔子》
     * topic : 先秦思想家
     * subtopic : 孔子
     * replies : 0
     * textURL : http://lc-FCHudlon.cn-n1.lcfile.com/b33b11731d8ce49de9f0.txt
     * reviews : 0
     * objectId : 5aea79ed7f6fd30038690f33
     */

    private String objectId;
    private String imageURL;
    private String authorId;
    private int dislikes;
    private int subscribers;
    private String dynasty;
    private String type;
    private int likes;
    private String title;
    private String topic;
    private String subtopic;
    private int replies;
    private String textURL;
    private int reviews;

    public Post(PostEntity post) {
        this.objectId = post.getPostId();
        this.imageURL = post.getImageURL();
        this.authorId = post.getAuthorId();
        this.dislikes = post.getDislikes();
        this.subscribers = post.getSubscribers();
        this.dynasty = post.getDynasty();
        this.type = post.getType();
        this.likes = post.getLikes();
        this.title = post.getTitle();
        this.topic = post.getTopic();
        this.subtopic = post.getSubtopic();
        this.replies = post.getReplies();
        this.textURL = post.getTextURL();
        this.reviews = post.getReviews();
    }

    public String getObjectId() {
        return objectId;
    }

    public String getImageURL() {
        return imageURL;
    }

    public String getAuthorId() {
        return authorId;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getSubscribers() {
        return subscribers;
    }

    public String getDynasty() {
        return dynasty;
    }

    public String getType() {
        return type;
    }

    public int getLikes() {
        return likes;
    }

    public String getTitle() {
        return title;
    }

    public String getTopic() {
        return topic;
    }

    public String getSubtopic() {
        return subtopic;
    }

    public int getReplies() {
        return replies;
    }

    public String getTextURL() {
        return textURL;
    }

    public int getReviews() {
        return reviews;
    }

    static public List<Post> getPostsWithPost(List<PostEntity> postEntities) {
        List<Post> posts = new ArrayList<Post>();
        for (PostEntity postEntity: postEntities) {
            posts.add(new Post(postEntity));
        }
        return posts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.objectId);
        dest.writeString(this.imageURL);
        dest.writeString(this.authorId);
        dest.writeInt(this.dislikes);
        dest.writeInt(this.subscribers);
        dest.writeString(this.dynasty);
        dest.writeString(this.type);
        dest.writeInt(this.likes);
        dest.writeString(this.title);
        dest.writeString(this.topic);
        dest.writeString(this.subtopic);
        dest.writeInt(this.replies);
        dest.writeString(this.textURL);
        dest.writeInt(this.reviews);
    }

    protected Post(Parcel in) {
        this.objectId = in.readString();
        this.imageURL = in.readString();
        this.authorId = in.readString();
        this.dislikes = in.readInt();
        this.subscribers = in.readInt();
        this.dynasty = in.readString();
        this.type = in.readString();
        this.likes = in.readInt();
        this.title = in.readString();
        this.topic = in.readString();
        this.subtopic = in.readString();
        this.replies = in.readInt();
        this.textURL = in.readString();
        this.reviews = in.readInt();
    }

    public static final Parcelable.Creator<Post> CREATOR = new Parcelable.Creator<Post>() {
        @Override
        public Post createFromParcel(Parcel source) {
            return new Post(source);
        }

        @Override
        public Post[] newArray(int size) {
            return new Post[size];
        }
    };
}
