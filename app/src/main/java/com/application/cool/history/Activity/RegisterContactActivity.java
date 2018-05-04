package com.application.cool.history.Activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterContactActivity extends AppCompatActivity {


    private EContactType curContractType = EContactType.E_PHONE_NUM;
    private String curPhone = null;
    private String curEmail = null;

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

    public enum EContactType {
        E_PHONE_NUM, E_EMAIL, E_WECHAT
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_contact);
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
        }

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
                }


                if (curContractType == EContactType.E_PHONE_NUM) {
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

                } else if (curContractType == EContactType.E_EMAIL) {
                    if (!isEmailValid(s1)) {
                        btnNext.setClickable(false);
                        btnNext.setTextColor(getResources().getColor(R.color.white));
                        contactEdit.setError("请输入有效的邮箱地址。");
                    } else {
                        btnNext.setClickable(true);
                        btnNext.setTextColor(getResources().getColor(R.color.white));
                        contactEdit.setError(null);
                        curEmail = s1;
                    }

                }
            }
        });
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
                if (curContractType == EContactType.E_PHONE_NUM) {
                    contactEdit.setHint(R.string.email_hint);
                    contactEdit.setText("");
                    contactEdit.setError(null);
                    contactEdit.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                    contextTitle.setText(R.string.what_is_email);
                    declareText.setText(R.string.email_declare);
                    contactText.setText(R.string.use_phone_num);
                    curContractType = EContactType.E_EMAIL;

                } else if (curContractType == EContactType.E_EMAIL) {
                    contactEdit.setHint(R.string.phone_hint);
                    contactEdit.setText("");
                    contactEdit.setError(null);
                    contactEdit.setInputType(InputType.TYPE_CLASS_PHONE);
                    contextTitle.setText(R.string.what_is_phone);
                    declareText.setText(R.string.phone_declare);
                    contactText.setText(R.string.use_email);
                    curContractType = EContactType.E_PHONE_NUM;
                }
                break;
            case R.id.btn_next:
                if (curContractType == EContactType.E_PHONE_NUM) {
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
                                    //Todo
                                }
                            })
                            .create().show();
                    break;
                } else if (curContractType == EContactType.E_EMAIL) {
                    // Todo
                }

        }
    }


    private boolean isEmailValid(String email) {
        Pattern p = Pattern.compile("^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private boolean isPhoneNumberValid(String num) {
        if (num.startsWith("+86")) {
            num = num.substring(3);
        }

        Pattern p = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(num);
        return m.matches();
    }


    private void savePhoneNumber() {
        // Todo
    }



}
