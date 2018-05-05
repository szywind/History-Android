package com.application.cool.history.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;
import com.jkb.vcedittext.VerificationAction;
import com.jkb.vcedittext.VerificationCodeEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationCodeActivity extends AppCompatActivity {

    public static final String PHONE_NUM = "Register_Phone";
    @BindView(R.id.declare_text)
    TextView declareText;
    @BindView(R.id.message_receive)
    TextView messageReceive;
    @BindView(R.id.btn_next)
    Button btnNext;

    String[] alertDialogItems = {"重新发送短信", "使用邮件地址注册", "取消"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

        declareText.setText(declareText.getText() + getIntent().getStringExtra(PHONE_NUM) + "。");
        VerificationCodeEditText verificationCodeEditText = (VerificationCodeEditText) findViewById(R.id.verificationCodeInput);
        verificationCodeEditText.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {
                btnNext.setTextColor(getResources().getColor(R.color.white));
                btnNext.setClickable(false);
            }

            @Override
            public void onInputCompleted(CharSequence s) {
                btnNext.setTextColor(getResources().getColor(R.color.black));
                btnNext.setClickable(true);
            }
        });
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @OnClick({R.id.message_receive, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.message_receive:
                new AlertDialog.Builder(this)
                        .setTitle("未收到短信？")
                        .setItems(alertDialogItems, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case 0:{
                                        //TODO
                                        dialog.dismiss();
                                        break;
                                    }
                                    case 1:{
                                        dialog.dismiss();
                                        break;
                                    }
                                    case 2:{
                                        dialog.dismiss();
                                        break;
                                    }
                                    default:
                                        break;
                                }
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.btn_next:
                Intent intent  = new Intent(this, PasswordSettingPage.class);
                startActivity(intent);
                break;
        }
    }
}
