package com.application.cool.history.activities.account;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetWithEmailActivity extends AppCompatActivity {

    @BindView(R.id.declare_text)
    TextView declareText;
    @BindView(R.id.email_receive)
    TextView emailReceive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_with_email);
        ButterKnife.bind(this);

        ActivityCollector.addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    @OnClick(R.id.email_receive)
    public void onViewClicked() {

    }


}
