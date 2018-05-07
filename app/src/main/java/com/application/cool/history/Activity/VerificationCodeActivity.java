package com.application.cool.history.Activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.jkb.vcedittext.VerificationCodeEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerificationCodeActivity extends AppCompatActivity {

    @BindView(R.id.declare_text)
    TextView declareText;
    @BindView(R.id.message_receive)
    TextView messageReceive;
    @BindView(R.id.btn_next)
    Button btnNext;

    String[] alertDialogItems = {"重新发送短信", "更换号码重新注册", "取消"};
    VerificationCodeEditText verificationCodeEditText;
    String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_code);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

        phoneNumber = getSharedPreferences("user_data", MODE_PRIVATE).getString("phone", "null");

        declareText.setText(declareText.getText() + phoneNumber + "。");
        verificationCodeEditText = (VerificationCodeEditText) findViewById(R.id.verificationCodeInput);
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
                                        AVSMSOption option = new AVSMSOption();
                                        option.setTtl(10);                     // 验证码有效时间为 10 分钟
                                        option.setApplicationName("History");
                                        option.setOperation("注册");
                                        AVSMS.requestSMSCodeInBackground(phoneNumber, option, new RequestMobileCodeCallback() {
                                            @Override
                                            public void done(AVException e) {
                                                if (e == null) {

                                                } else {
                                                    // failed
                                                    Toast.makeText(VerificationCodeActivity.this,
                                                            "发送短信失败，发送次数已达到上限！", Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                                        break;
                                    }
                                    case 1:{
                                        dialog.dismiss();
                                        finish();
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
                AVSMS.verifySMSCodeInBackground(verificationCodeEditText.getText().toString(), phoneNumber, new AVMobilePhoneVerifyCallback() {
                    @Override
                    public void done(AVException e) {
                        if (null == e) {
                            /* 验证成功 */
                            Intent intent  = new Intent(VerificationCodeActivity.this, PasswordSettingActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            /* 验证失败 */
                            verificationCodeEditText.setText("");
                            Toast.makeText(VerificationCodeActivity.this, "验证码错误，请重新输入。", Toast.LENGTH_LONG).show();
                            btnNext.setTextColor(getResources().getColor(R.color.white));
                            btnNext.setClickable(false);
                        }
                    }
                });

                break;
        }
    }
}
