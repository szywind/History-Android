package com.application.cool.history.activities.encyclopedia;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.models.Record;
import com.application.cool.history.util.GlideApp;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.util.Charsets;
import com.koushikdutta.ion.Ion;



/**
 * Created by Zhenyuan Shen on 5/9/18.
 */

public class EncyclopediaDetailActivity extends AppCompatActivity {

    public static final String INTENT_RECORD = "intent_record";

    private ImageView image;
    private TextView title;
    private TextView text;

    private Record record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encyclopedia_detail);

        title = (TextView) findViewById(R.id.record_title);
        image = (ImageView) findViewById(R.id.record_image);
        text = (TextView) findViewById(R.id.record_text);

        Bundle bundle = getIntent().getExtras();

        //Extract the data
        record = bundle.getParcelable(INTENT_RECORD);

        title.setText(record.getName());

        GlideApp.with(this)
                .load(record.getAvatarURL())
                .fitCenter()
                .placeholder(R.drawable.jiangshan)
                .error(R.drawable.jiangshan)
                .into(image);

        Ion.with(this).load(record.getInfoURL()).asString(Charsets.UTF_8).setCallback(new FutureCallback<String>() {
            @Override
            public void onCompleted(Exception e, String result) {
                if (e == null) {
                    text.setText(result);
                } else {
                    text.setText(Constants.Default.defaultInfo);
                }
            }
        });

        // https://stackoverflow.com/questions/4964640/reading-inputstream-as-utf-8
//        try {
//            URL url = new URL(record.getInfoURL());
//            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), StandardCharsets.UTF_8));
//            text.setText(in.toString());
//        }
//        catch (Exception e) {
//            text.setText(Constants.Default.defaultInfo);
//        }

    }
}
