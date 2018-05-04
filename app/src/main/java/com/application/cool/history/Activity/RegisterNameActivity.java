package com.application.cool.history.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.application.cool.history.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterNameActivity extends AppCompatActivity {


    @BindView(R.id.name_edit)
    EditText nameEdit;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_name);
        ButterKnife.bind(this);


        btnNext.setClickable(false);
        btnNext.setTextColor(getResources().getColor(R.color.white));

        nameEdit.setHint(R.string.username_hint);
        nameEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL) {
                    return true;
                }
                return false;
            }
        });

        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (nameEdit.getError() != null) {
                    nameEdit.setError(null);
                }

                String s1 = s.toString();
                if (s1.equals("")) {
                    btnNext.setClickable(false);
                    btnNext.setTextColor(getResources().getColor(R.color.white));
                } else {
                    btnNext.setClickable(true);
                    btnNext.setTextColor(getResources().getColor(R.color.black));
                }

                if (!isUserNameValid(s1)) {
                    nameEdit.setError("用户名不能超过20个字符");
                    btnNext.setClickable(false);
                    btnNext.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

    }

    @OnClick(R.id.btn_next)
    public void onViewClicked() {
        saveUserName();
        Intent intent = new Intent(this, RegisterContactActivity.class);
        startActivity(intent);
    }

    private boolean isUserNameValid(String userName) {
        return userName.length() <= 20;
    }

    private void saveUserName() {

    }
}
