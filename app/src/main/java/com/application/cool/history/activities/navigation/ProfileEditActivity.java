package com.application.cool.history.activities.navigation;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.application.cool.history.R;
import com.application.cool.history.constants.Constants;
import com.application.cool.history.managers.AvatarManager;
import com.application.cool.history.managers.UserManager;
import com.application.cool.history.util.MessageEvent;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileEditActivity extends AppCompatActivity {

    @BindView(R.id.save_profile)
    TextView saveProfile;
    @BindView(R.id.cancel_profile)
    TextView cancelProfile;
    @BindView(R.id.avatar_img)
    CircleImageView avatarImg;
    @BindView(R.id.nickname_edit)
    EditText nicknameEdit;

    private final static int CAMERA_REQUEST_CODE = 0;
    private final static int GALLERY_REQUEST_CODE = 1;
    private Uri imageUri;
    private File outputImage;
    private File avatarImageFile;
    private UserManager userManager;
    private AvatarManager avatarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this);

        userManager = UserManager.getSharedInstance(this);
        avatarManager = AvatarManager.getSharedInstance(this);
        nicknameEdit.setText(userManager.getNickname());
        nicknameEdit.setSelection(userManager.getNickname().length());

        nicknameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    nicknameEdit.setError("昵称不能为空");

                    return;
                } else if (s.toString().length() > 20) {
                    nicknameEdit.setError("昵称不能超过20个字符");
                    return;
                }

                if (nicknameEdit.getError() != null) {
                    nicknameEdit.setError(null);
                }

            }
        });
    }

    @OnClick({R.id.save_profile, R.id.cancel_profile, R.id.avatar_img})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.save_profile:
                String nickName = nicknameEdit.getText().toString();
                String imgPath = avatarImageFile == null ? null : avatarImageFile.getAbsolutePath();
                userManager.updateUser(nickName, imgPath, new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e == null) {
                            finish();
                            EventBus.getDefault().post(new MessageEvent(Constants.EventType.EVENT_UPDATE_USER));
                        } else {
                            Toast.makeText(ProfileEditActivity.this, "保存失败", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                break;
            case R.id.cancel_profile:
                finish();
                break;
            case R.id.avatar_img:
                String title = "选择获取图片方式";
                String[] items = {"拍照", "相册"};
                new AlertDialog.Builder(this)
                        .setTitle(title)
                        .setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                switch (which) {
                                    case 0:
                                        selectImageFormCamera();
                                        break;
                                    case 1:
                                        selectImageFormAlbum();
                                        break;
                                    default:
                                        break;
                                }
                            }
                        })
                        .create().show();
                break;
        }
    }

    private void selectImageFormAlbum() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
            choosePhoto();
        }
    }

    private void selectImageFormCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_REQUEST_CODE);
        } else {
            takePhoto();
        }

    }


    private void takePhoto() {
        outputImage = new File(getExternalCacheDir(), "output_image.jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Build.VERSION.SDK_INT >= 24) {
            imageUri = FileProvider.getUriForFile(this, "com.application.cool.history.activities.menu", outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);

    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    avatarImageFile = avatarManager.saveImage(outputImage);
                    if (avatarImageFile != null) {
                        try {
                            avatarManager.updateAvatarWithImage(avatarImageFile.getPath(), new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if (e == null) {
                                        try {
                                            FileInputStream inputStream = new FileInputStream(avatarImageFile);
                                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                            avatarImg.setImageBitmap(rotateImage(bitmap));
                                            inputStream.close();
                                        } catch (IOException e1) {
                                            e1.printStackTrace();
                                        }
                                    } else {
                                        Toast.makeText(ProfileEditActivity.this, "头像上传失败", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case GALLERY_REQUEST_CODE:
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else {
                        handleImageBeforeKitKat(data);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (requestCode == CAMERA_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                takePhoto();
            } else
            {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == GALLERY_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                choosePhoto();
            } else
            {
                // Permission Denied
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        String imagePath = null;
        Uri uri = data.getData();
        if (DocumentsContract.isDocumentUri(this, uri)) {
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.androidl.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            imagePath = getImagePath(uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            imagePath = uri.getPath();
        }

        displayImagePath(imagePath);
    }

    private void handleImageBeforeKitKat(Intent data) {
        Uri uri = data.getData();
        String imagePath = getImagePath(uri, null);
        displayImagePath(imagePath);
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;

        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private void displayImagePath(final String imagePath) {
        if (imagePath != null) {

            try {
                File file = new File(imagePath);
                avatarImageFile = avatarManager.saveImage(file);
                avatarManager.updateAvatarWithImage(avatarImageFile.getAbsolutePath(), new SaveCallback() {
                    @Override
                    public void done(AVException e) {
                        if (e != null) {
                            Toast.makeText(ProfileEditActivity.this, "头像上传失败", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                FileInputStream inputStream = new FileInputStream(avatarImageFile);
                                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                                avatarImg.setImageBitmap(/*rotateImage(bitmap)*/bitmap);
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Bitmap rotateImage(Bitmap srcImage) {
        Matrix matrix = new Matrix();  //android 照片默认被旋转了90度，需要旋转回来
        matrix.reset();
        matrix.setRotate(90);
        return Bitmap.createBitmap(srcImage, 0, 0, srcImage.getWidth(), srcImage.getHeight(),matrix, true);
    }
}
