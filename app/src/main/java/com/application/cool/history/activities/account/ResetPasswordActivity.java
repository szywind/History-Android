package com.application.cool.history.activities.account;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.application.cool.history.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.contact_edit)
    TextInputEditText contactEdit;
    @BindView(R.id.btn_next)
    Button btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        contactEdit.setHint(R.string.login_hint);
        contactEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }


    @OnClick({R.id.contact_edit, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact_edit:
                break;
            case R.id.btn_next:
                break;
        }
    }
}
