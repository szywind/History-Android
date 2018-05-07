package com.application.cool.history.activities.account;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.btn_register)
    Button btnRegister;
    @BindView(R.id.login)
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/lishu.ttf");
        textView.setTypeface(typeface);
    }

    @OnClick({R.id.btn_register, R.id.login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                Intent signupIntent = new Intent(this, RegisterNameActivity.class);
                startActivity(signupIntent);
                break;
            case R.id.login:
//                Intent loginIntent = new Intent(this, LoginActivity.class);
//                startActivity(loginIntent);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }
}

