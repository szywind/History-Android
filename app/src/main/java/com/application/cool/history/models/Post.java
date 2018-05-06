package com.application.cool.history.models;

import com.application.cool.history.db.PostEntity;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */

public class Post {

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
}
