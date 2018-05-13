package com.application.cool.history.managers.DBManagers;

import android.content.Context;

import com.application.cool.history.db.PostEntity;
import com.application.cool.history.db.PostEntityDao;
import com.application.cool.history.models.Post;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhenyuan Shen on 5/7/18.
 */
public class PostStore extends LocalStore {
    private static PostStore sharedInstance = null;

    private PostStore(Context context){
        initDB(context);
    }

    public static PostStore getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new PostStore(context);
        }
        return sharedInstance;
    }

    private static PostEntityDao postDao;

    public void initDB(Context context) {
        if (daoSession == null) {
            super.initDB(context, "post.sqlite");
        }
        postDao = daoSession.getPostEntityDao();
    }

    public void savePost(PostEntity postEntity) {
        postDao.insertOrReplace(postEntity);
    }

    public List<Post> getPosts() {
        List<PostEntity> postEntities = postDao.queryBuilder().list();
        return Post.getPostsWithPost(postEntities);
    }
}
