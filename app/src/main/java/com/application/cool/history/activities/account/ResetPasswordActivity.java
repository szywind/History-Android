package com.application.cool.history.activities.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import com.application.cool.history.R;
import com.application.cool.history.util.CommonData;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.contact_edit)
    TextInputEditText contactEdit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.contact_layout)
    TextInputLayout contactLayout;

    private CommonData.EContactType resetMode;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        editor = getSharedPreferences("user_data",MODE_PRIVATE).edit();

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
                if (contactLayout.getError() != null) {
                    contactLayout.setError(null);
                }

                if (contactEdit.getText().toString().trim().length() != 0) {
                    enableBtnNext();
                } else {
                    disableBtnNext();
                }
            }
        });

    }


    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        if (resetMode == CommonData.EContactType.E_EMAIL) {
            Intent intentEmial =  new Intent(this, ResetWithEmailActivity.class);
            intentEmial.putExtra("reset_email", contactEdit.getText().toString());

            startActivity(intentEmial);
        } else {     // E_PHONE
//            Intent intentPhone = new Intent()
        }
    }


    public boolean validate() {
        boolean valid = true;


        String contact = contactEdit.getText().toString();

        if (!contact.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(contact).matches()) {
            resetMode = CommonData.EContactType.E_EMAIL;
        } else if (!contact.isEmpty() && Patterns.PHONE.matcher(contact).matches()) {
            resetMode = CommonData.EContactType.E_PHONE;
        } else {
            valid = false;
            contactLayout.setError(getResources().getString(R.string.valid_id));
        }

        return valid;
    }

    private void disableBtnNext() {
        btnNext.setClickable(false);
        btnNext.setTextColor(getResources().getColor(R.color.white));
    }

    private void enableBtnNext() {
        btnNext.setClickable(true);
        btnNext.setTextColor(getResources().getColor(R.color.black));
    }
}
