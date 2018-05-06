package com.application.cool.history.managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.application.cool.history.constants.LCConstants;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Zhenyuan Shen on 5/6/18.
 */
public class AvatarManager {
    private Context context;

    private final static String TAG = "AvatarManager";

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public AvatarManagerResponse delegate = null;

    public interface AvatarManagerResponse {
        void processFinish(String output);
    }

    private static AvatarManager sharedInstance = null;
    private AvatarManager(Context context, AvatarManagerResponse delegate){
        this.delegate = delegate;
        this.context = context;
    }

    private AvatarManager(Context context){
        this.context = context;
    }

    public static AvatarManager getSharedInstance(Context context){
        if(sharedInstance == null){
            sharedInstance = new AvatarManager(context);
        }
        return sharedInstance;
    }

    public static AvatarManager getSharedInstance(Context context, AvatarManagerResponse delegate){

        if(sharedInstance == null){
            sharedInstance = new AvatarManager(context, delegate);
        }
        return sharedInstance;
    }

    public void updateAvatarWithImage(String filePath, final SaveCallback saveCallback) throws IOException {
        byte[] bytes = getByteArrayFromImage(filePath);
        final AVFile file = new AVFile("avatar", bytes);
        file.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e!=null){

                }else{
                    // no error
                    AVUser.getCurrentUser().put(LCConstants.UserKey.avatarURL, file.getUrl());
                    AVUser.getCurrentUser().saveInBackground(saveCallback);
                }
            }
        });
    }

    private byte[] getByteArrayFromImage(String filePath) throws FileNotFoundException, IOException {

        File file = new File(filePath);
        System.out.println(file.exists() + "!!");

        FileInputStream fis = new FileInputStream(file);
        //create FileInputStream which obtains input bytes from a file in a file system
        //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

        //InputStream in = resource.openStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        try {
            for (int readNum; (readNum = fis.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
                //no doubt here is 0
                /*Writes len bytes from the specified byte array starting at offset
                off to this byte array output stream.*/
                System.out.println("read " + readNum + " bytes,");
            }
        } catch (IOException ex) {
            Log.d("error","error");
        }
        byte[] bytes = bos.toByteArray();
        return bytes;
    }


    public File saveImage(File file) {
        Log.d("Save imgae", file.getName());

        try {
            // BitmapFactory options to downsize the image
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            o.inSampleSize = 6;
            FileInputStream inputStream = new FileInputStream(file);
            BitmapFactory.decodeStream(inputStream, null, o);
            inputStream.close();
            // The new size we want to scale to
            final int REQUIRED_SIZE=75;
            // Find the correct scale value. It should be the power of 2.
            int scale = 1;
            while(o.outWidth / scale / 2 >= REQUIRED_SIZE &&
                    o.outHeight / scale / 2 >= REQUIRED_SIZE) {
                scale *= 2;
            }
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, o2);
            inputStream.close();

            // here i override the original image file
            File newFile = new File(context.getFilesDir(), UUID.randomUUID().toString() + ".jpg");

            if (newFile.createNewFile()) {
                FileOutputStream outputStream = new FileOutputStream(newFile);

                selectedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

                return newFile;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }
}
