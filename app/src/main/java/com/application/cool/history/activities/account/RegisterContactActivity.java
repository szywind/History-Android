package com.application.cool.history.activities.account;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.util.ActivityCollector;
import com.application.cool.history.util.CommonData;
import com.application.cool.history.managers.UserManager;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVSMS;
import com.avos.avoscloud.AVSMSOption;
import com.avos.avoscloud.RequestMobileCodeCallback;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterContactActivity extends AppCompatActivity {


    private  static CommonData.EContactType curContractType = CommonData.EContactType.E_PHONE;
    private String curPhone = null;
    private String curEmail = null;

    private SharedPreferences.Editor editor;

    @BindView(R.id.contact_edit)
    TextInputEditText contactEdit;
    @BindView(R.id.contact_text)
    TextView contactText;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.context_title)
    TextView contextTitle;
    @BindView(R.id.declare_text)
    TextView declareText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_contact);
        ActivityCollector.addActivity(this);
        ButterKnife.bind(this);
        contactEdit.setHint(R.string.phone_hint);
        if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_PHONE_STATE
        )) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE}, 1);

        } else {
            TelephonyManager phoneMgr = (TelephonyManager) this.getSystemService(
                    Context.TELEPHONY_SERVICE);
            contactEdit.setText(phoneMgr.getLine1Number());
            btnNext.setClickable(true);
            btnNext.setTextColor(getResources().getColor(R.color.black));
        }

        editor = getSharedPreferences("user_data", MODE_PRIVATE).edit();

        contactEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return false;
            }
        });

        contactEdit.addTextChangedListener(new TextWatcher() {
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


                if (curContractType == CommonData.EContactType.E_PHONE) {
                    if (!isPhoneNumberValid(s1)) {
                        btnNext.setClickable(false);
                        btnNext.setTextColor(getResources().getColor(R.color.white));
                        contactEdit.setError("请输入有效的电话号码。");
                    } else {
                        btnNext.setClickable(true);
                        btnNext.setTextColor(getResources().getColor(R.color.black));
                        contactEdit.setError(null);
                        curPhone = s1;
                    }

                } else if (curContractType == CommonData.EContactType.E_EMAIL) {
                    if (!isEmailValid(s1)) {
                        btnNext.setClickable(false);
                        btnNext.setTextColor(getResources().getColor(R.color.white));
                        contactEdit.setError("请输入有效的邮箱地址。");
                    } else {
                        btnNext.setClickable(true);
                        btnNext.setTextColor(getResources().getColor(R.color.black));
                        contactEdit.setError(null);
                        curEmail = s1;
                    }

                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        TelephonyManager phoneMgr = (TelephonyManager) this.getSystemService(
                                Context.TELEPHONY_SERVICE);
                        contactEdit.setText(phoneMgr.getLine1Number());
                        btnNext.setClickable(true);
                        btnNext.setTextColor(getResources().getColor(R.color.black));
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @OnClick({R.id.contact_edit, R.id.contact_text, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.contact_edit:
                break;
            case R.id.contact_text:
                if (curContractType == CommonData.EContactType.E_PHONE) {
                    contactEdit.setHint(R.string.email_hint);
                    contactEdit.setText("");
                    contactEdit.setError(null);
                    contactEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    contextTitle.setText(R.string.what_is_email);
                    declareText.setText(R.string.email_declare);
                    contactText.setText(R.string.use_phone_num);
                    curContractType = CommonData.EContactType.E_EMAIL;

                } else if (curContractType == CommonData.EContactType.E_EMAIL) {
                    contactEdit.setHint(R.string.phone_hint);
                    contactEdit.setText("");
                    contactEdit.setError(null);
                    contactEdit.setInputType(InputType.TYPE_CLASS_PHONE);
                    contextTitle.setText(R.string.what_is_phone);
                    declareText.setText(R.string.phone_declare);
                    contactText.setText(R.string.use_email);
                    curContractType = CommonData.EContactType.E_PHONE;
                }
                break;
            case R.id.btn_next:
                if (curContractType == CommonData.EContactType.E_PHONE) {
                    new AlertDialog.Builder(this)
                            .setTitle("验证手机")
                            .setMessage("我们会发送你的验证码到\n" + curPhone + "。可能收取短信费用")
                            .setNegativeButton("编辑", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final ProgressDialog progressDialog = new ProgressDialog(RegisterContactActivity.this);
                                    progressDialog.setIndeterminate(true);
                                    progressDialog.setMessage("请稍等...");
                                    progressDialog.show();

                                    AVSMSOption option = new AVSMSOption();
                                    option.setTtl(10);                     // 验证码有效时间为 10 分钟
                                    option.setApplicationName("History");
                                    option.setOperation("注册");
                                    AVSMS.requestSMSCodeInBackground(contactEdit.getText().toString(), option, new RequestMobileCodeCallback() {
                                        @Override
                                        public void done(AVException e) {
                                            if (e == null) {
                                                progressDialog.dismiss();

                                                String phone = contactEdit.getText().toString();
                                                if (phone.startsWith("+86")) {
                                                    editor.putString("phone", phone.substring(3));
                                                } else {
                                                    editor.putString("phone", phone);
                                                }

                                                editor.apply();

                                                Intent intent = new Intent(RegisterContactActivity.this, VerificationCodeActivity.class);
                                                startActivity(intent);
                                            } else {
                                                // failed
                                                progressDialog.dismiss();
                                                contactEdit.setError("此号码已经被注册");
                                            }
                                        }
                                    });
                                }
                            })
                            .create().show();
                    break;
                } else if (curContractType == CommonData.EContactType.E_EMAIL) {
                    // Todo
                    editor.putString("email", contactEdit.getText().toString());
                    editor.apply();

                    Intent intent = new Intent(this, PasswordSettingActivity.class);
                    startActivity(intent);
                }

        }
    }


    private boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPhoneNumberValid(String num) {
        if (num.startsWith("+86")) {
            num = num.substring(3);
        }

        Pattern p = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    public static CommonData.EContactType getContractType() {
        return curContractType;
    }
}

