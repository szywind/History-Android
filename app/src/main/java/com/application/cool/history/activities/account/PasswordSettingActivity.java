package com.application.cool.history.activities.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.constants.LCConstants;
import com.application.cool.history.util.ActivityCollector;
import com.application.cool.history.util.CommonData;
import com.application.cool.history.util.LogUtil;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordSettingActivity extends AppCompatActivity {

    private final static String TAG = "Password Setting";

    @BindView(R.id.password_edit)
    TextInputEditText passwordEdit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    private String intentMode;
    private String verifyCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting_page);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

        intentMode = getIntent().getStringExtra("intentMode");
        verifyCode = getIntent().getStringExtra("verifyCode");
        passwordEdit.setHint(R.string.password_hint);

        passwordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                String s1 = s.toString();

                if (TextUtils.isEmpty(s1)) {
                    btnNext.setClickable(false);
                    btnNext.setTextColor(getResources().getColor(R.color.white));
                    return;
                }

                if (!isPasswordValid(s1)) {
                    passwordLayout.setError("密码至少要有8个字符");
                    btnNext.setClickable(false);
                    btnNext.setTextColor(getResources().getColor(R.color.white));
                } else {
                    passwordLayout.setError(null);
                    btnNext.setClickable(true);
                    btnNext.setTextColor(getResources().getColor(R.color.black));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {

        if (intentMode.equals("resetPassword")) {
            resetPassword();
        } else if (intentMode.equals("register")) {
            signUp();
        } else {
            LogUtil.d(TAG, "unknown intent mode");
        }

    }

    private boolean isPasswordValid(String s) {
        return s.length() >= 8;
    }

    private void signUp() {
        AVUser user = new AVUser();
        SharedPreferences pref = getSharedPreferences("user_data", MODE_PRIVATE);
        String nickname = pref.getString("user_name", "");

        if (RegisterContactActivity.getContractType() == CommonData.EContactType.E_EMAIL) {
            String email = pref.getString("email", "");
            user.setEmail(email);
            user.setUsername(email);


        } else if (RegisterContactActivity.getContractType() == CommonData.EContactType.E_PHONE) {
            String phone = pref.getString("phone", "");
            user.setMobilePhoneNumber(phone);
            user.setUsername(phone);
        }

        user.setPassword(passwordEdit.getText().toString());
        user.put(LCConstants.UserKey.nickname, nickname);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ActivityCollector.finishAll();
                } else {
                    Toast.makeText(PasswordSettingActivity.this, "注册失败", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void resetPassword() {
        AVUser.resetPasswordBySmsCodeInBackground(verifyCode, passwordEdit.getText().toString(), new UpdatePasswordCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    ActivityCollector.finishAll();
                } else {
                    Toast.makeText(PasswordSettingActivity.this, "验证码错误，请重新输入验证码", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}
