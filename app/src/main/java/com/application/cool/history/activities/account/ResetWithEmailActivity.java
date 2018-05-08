package com.application.cool.history.activities.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.util.ActivityCollector;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.RequestPasswordResetCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ResetWithEmailActivity extends AppCompatActivity {

    @BindView(R.id.declare_text)
    TextView declareText;
    @BindView(R.id.email_receive)
    TextView emailReceive;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_with_email);
        ButterKnife.bind(this);

        ActivityCollector.addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);
    }


    @OnClick(R.id.email_receive)
    public void onViewClicked() {
        new AlertDialog.Builder(this)
                .setTitle("未收到邮件？")
                .setMessage("是否需要重新发送邮件？点击确定重新发送。")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        UserManager.getSharedInstance(ResetWithEmailActivity.this).resetPasswordWithEmail(email, new RequestPasswordResetCallback() {
                            @Override
                            public void done(AVException e) {
                                if (e == null) {
                                    dialog.dismiss();
                                    Toast.makeText(ResetWithEmailActivity.this, "邮件已发送，请及时查看邮箱", Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(ResetWithEmailActivity.this, "邮件发送失败，点击确定重新发送", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                        dialog.dismiss();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create().show();
    }


}
