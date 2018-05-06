package com.application.cool.history.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.MainActivity;
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
                if (passwordLayout.getError() != null) {
                    passwordLayout.setError(null);
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
                break;
            case R.id.btn_login:
                login();
                break;
            case R.id.sign_up:
                Intent intent = new Intent(this, RegisterNameActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void login() {
        if (!validate()) {
            btnLogin.setEnabled(true);
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
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
        btnLogin.setEnabled(false);
        finish();
    }

    private void onLoginFailed() {
        Toast.makeText(getBaseContext(), R.string.login_failed, Toast.LENGTH_LONG).show();
        btnLogin.setEnabled(true);
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


}
