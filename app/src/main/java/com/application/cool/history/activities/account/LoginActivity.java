package com.application.cool.history.activities.account;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;
import com.application.cool.history.util.CommonData;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.sign_up)
    TextView signUp;
    @BindView(R.id.user_id_edit)
    TextInputEditText userIdEdit;
    @BindView(R.id.password_edit)
    TextInputEditText passwordEdit;
    @BindView(R.id.forget_password)
    TextView forgetPassword;
    @BindView(R.id.btn_login)
    Button btnLogin;

    CommonData.EContactType loginType;
    @BindView(R.id.user_id_layout)
    TextInputLayout userIdLayout;
    @BindView(R.id.password_layout)
    TextInputLayout passwordLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        ButterKnife.bind(this);
        ActivityCollector.addActivity(this);

        userIdEdit.setHint(R.string.login_hint);
        userIdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (userIdLayout.getError() != null) {
                    userIdLayout.setError(null);
                }

                if (passwordEdit.getText().toString().trim().length() != 0 && userIdEdit.getText().toString().trim().length() != 0) {
                    enableBtnLogin();
                } else {
                    disableBtnLogin();
                }
            }
        });

        passwordEdit.setHint(R.string.password_hint);
        passwordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (passwordLayout.getError() != null) {
                    passwordLayout.setError(null);
                }

                if (passwordEdit.getText().toString().trim().length() != 0 && userIdEdit.getText().toString().trim().length() != 0) {
                    enableBtnLogin();
                } else {
                    disableBtnLogin();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @OnClick({R.id.forget_password, R.id.btn_login, R.id.sign_up})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.forget_password:
                Intent intentReset = new Intent(this, ResetPasswordActivity.class);
                startActivity(intentReset);
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.sign_up:
                Intent intentRegister = new Intent(this, RegisterNameActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void login() {
        if (!validate()) {
            disableBtnLogin();
            return;
        }
        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        AVUser.logInInBackground(userIdEdit.getText().toString(), passwordEdit.getText().toString(), new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
                if (e == null) {
                    onLoginSuccess();
                    finish();
                } else {
                    progressDialog.dismiss();
                    onLoginFailed();
                }
            }
        });

        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {
                        // On complete call either onLoginSuccess or onLoginFailed

                        // onLoginFailed();

                    }
                }, 2000);
    }

    private void onLoginSuccess() {
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
        disableBtnLogin();
    }


    public boolean validate() {
        boolean valid = true;

        String id = userIdEdit.getText().toString();
        String password = passwordEdit.getText().toString();

        if (!id.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
            loginType = CommonData.EContactType.E_EMAIL;
        } else if (!id.isEmpty() && Patterns.PHONE.matcher(id).matches()) {
            loginType = CommonData.EContactType.E_PHONE;
        } else {
            valid = false;
            userIdLayout.setError(getResources().getString(R.string.valid_id));
        }

        if (password.isEmpty() || password.length() < 8) {
            passwordLayout.setError(getResources().getString(R.string.valid_password));
            valid = false;
        }

        return valid;
    }

    private void disableBtnLogin() {
        btnLogin.setClickable(false);
        btnLogin.setTextColor(getResources().getColor(R.color.white));
    }

    private void enableBtnLogin() {
        btnLogin.setClickable(true);
        btnLogin.setTextColor(getResources().getColor(R.color.black));
    }


}
