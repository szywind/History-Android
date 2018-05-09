package com.application.cool.history.activities.community;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.managers.PostManager;
import com.application.cool.history.models.Post;
import com.avos.avoscloud.AVObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.Charsets;
import com.koushikdutta.ion.Ion;

/**
 * Created by Zhenyuan Shen on 5/9/18.
 */
public class PostDetailActivity extends AppCompatActivity {

    public static final String INTENT_RECORD = "intent_record";

    private ImageView image;
    private TextView title;
    private TextView text;

    private AVObject post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        PostManager postManager = PostManager.getSharedInstance(this);

        title = (TextView) findViewById(R.id.post_title);
        image = (ImageView) findViewById(R.id.post_image);
        text = (TextView) findViewById(R.id.post_text);

        Bundle bundle = getIntent().getExtras();

        //Extract the data
        post = bundle.getParcelable(INTENT_RECORD);

        title.setText(postManager.getTitle(post));

        String imageURL = postManager.getImageURL(post);

        // https://stackoverflow.com/questions/11556607/android-difference-between-invisible-and-gone
        if (imageURL == null || imageURL.isEmpty()) {
            image.setVisibility(View.GONE);
        } else {
            image.setVisibility(View.VISIBLE);
            Ion.with(image).fitCenter().placeholder(R.drawable.empty).error(R.drawable.empty).load(imageURL);
        }

//        text.setText(Ion.with(this).load(record.getInfoURL()).toString());

        Ion.with(this).load(postManager.getTextURL(post)).asString(Charsets.UTF_8).setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e == null) {
                    text.setText(result);
                } else {
                    text.setText(Constants.Default.defaultInfo);
                }
            }
        });
    }
}