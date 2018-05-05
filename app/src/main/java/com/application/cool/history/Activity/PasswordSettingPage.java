package com.application.cool.history.Activity;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PasswordSettingPage extends AppCompatActivity {

    @BindView(R.id.password_edit)
    TextInputEditText passwordEdit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting_page);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);

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
        ActivityCollector.finishAll();
    }

    private boolean isPasswordValid(String s) {
        return s.length() >= 8;
    }
}
