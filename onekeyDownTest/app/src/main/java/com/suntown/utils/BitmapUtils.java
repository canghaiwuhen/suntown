package com.suntown.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/3.
 */
public class BitmapUtils {
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream baos = null;
        try {
            if (bitmap != null) {
                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                baos.flush();
                baos.close();

                byte[] bitmapBytes = baos.toByteArray();
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.flush();
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public static void saveBitmap(Bitmap bm, String picName) {
        Log.e("save", "保存图片"+picName);
        File f = new File(Environment.getExternalStorageDirectory(),"SunTown");
        if (!f.exists()) {
            f.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(f.getAbsolutePath()+"/"+picName + ".jpg");
            bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            Log.i("save", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromPath(String picName) {
        File f = new File(Environment.getExternalStorageDirectory() + "/SunTown", picName + ".jpg");
        Log.i("getBitmapFromPath", Environment.getExternalStorageDirectory().toString());
        if (!f.exists()) {
            return null;
        }
        byte[] buf = new byte[1024 * 1024];
        Bitmap bitmap = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            int len = fis.read(buf, 0, buf.length);
            bitmap = BitmapFactory.decodeByteArray(buf, 0, len);
            if (bitmap == null) {
                Log.i("getBitmapFromPath", "picName: " + picName + "  could not be decode!!!");
                Log.i("getBitmapFromPath", "len=" + len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
