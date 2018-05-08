package com.application.cool.history.activities.account;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.util.ActivityCollector;
import com.application.cool.history.util.LogUtil;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.RequestPasswordResetCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.internal.Util;

public class ResetPasswordActivity extends AppCompatActivity {

    private final static String TAG = "Reset Password";
    @BindView(R.id.contact_edit)
    TextInputEditText contactEdit;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.contact_layout)
    TextInputLayout contactLayout;

    private Constants.EContactType resetMode;
    private SharedPreferences.Editor editor;
    private String contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ButterKnife.bind(this);

        ActivityCollector.addActivity(this);
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @OnClick({R.id.btn_next})
    public void onViewClicked(View view) {
        if (!validate()) {
            disableBtnNext();
            return;
        }

        contact = contactEdit.getText().toString();
        final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("正在查找账户，请稍后...");
        progressDialog.show();
        UserManager.getSharedInstance(this).findUser("username", contact, new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if (e == null) {
                    progressDialog.dismiss();
                    if (list.size() != 0) {
                        resetPassword();
                    } else {
                        contactLayout.setError("没有查找到此账户，请重新输入。");
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(ResetPasswordActivity.this, "查找用户失败", Toast.LENGTH_LONG).show();
                }
            }
        });


    }


    public boolean validate() {
        boolean valid = true;


        String contact = contactEdit.getText().toString();

        if (!contact.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(contact).matches()) {
            resetMode = Constants.EContactType.E_EMAIL;
        } else if (!contact.isEmpty() && Patterns.PHONE.matcher(contact).matches()) {
            resetMode = Constants.EContactType.E_PHONE;
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


    private void resetPassword() {
        if (resetMode == Constants.EContactType.E_EMAIL) {
            editor.putString("email", contactEdit.getText().toString());
            editor.apply();
            UserManager.getSharedInstance(this).resetPasswordWithEmail(contact, new RequestPasswordResetCallback() {
                @Override
                public void done(AVException e) {
                    if (e == null) {
                        disableBtnNext();
                        Intent intentEmail =  new Intent(ResetPasswordActivity.this, ResetWithEmailActivity.class);
                        intentEmail.putExtra("email", contact);
                        startActivity(intentEmail);
                    } else {
                        Toast.makeText(ResetPasswordActivity.this, "重置密码操作失败，请采用其他方式", Toast.LENGTH_LONG).show();
                        enableBtnNext();
                    }
                }
            });


        } else {     // E_PHONE
            editor.putString("phone", contactEdit.getText().toString());
            editor.apply();
            new AlertDialog.Builder(this)
                    .setTitle("验证手机")
                    .setMessage("我们会发送你的验证码到\n" + contact + "。可能收取短信费用")
                    .setNegativeButton("编辑", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            final ProgressDialog progressDialog = new ProgressDialog(ResetPasswordActivity.this);
                            progressDialog.setIndeterminate(true);
                            progressDialog.setMessage("请稍等...");
                            progressDialog.show();

                            AVUser.requestPasswordResetBySmsCodeInBackground(contactEdit.getText().toString(), new RequestMobileCodeCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        progressDialog.dismiss();
                                        disableBtnNext();
                                        Intent intentPhone = new Intent(ResetPasswordActivity.this, VerificationCodeActivity.class);
                                        intentPhone.putExtra("intentMode", "resetPassword");
                                        startActivity(intentPhone);
                                    } else {
                                        progressDialog.dismiss();
                                        Toast.makeText(ResetPasswordActivity.this, "获取验证码失败", Toast.LENGTH_LONG).show();
                                        enableBtnNext();
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    })
                    .create().show();
        }
    }
}
