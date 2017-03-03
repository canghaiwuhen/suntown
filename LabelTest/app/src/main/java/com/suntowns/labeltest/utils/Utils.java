package com.suntowns.labeltest.utils;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/6/30.
 */
public class Utils {

    private static ExecutorService executorService = Executors.newCachedThreadPool();
    //在子线程中执行
    public static void runInThread(Runnable task){
        executorService.execute(task);
    }
    //在主线程中执行
    private static Handler handler=new Handler(Looper.getMainLooper());
    public static void runOnUIThread(Runnable task){
        handler.post(task);
    }
    //万能的吐司
    private static Toast toast;
    public static void showToast(final Context context, final String text){
        runOnUIThread(() -> {
            if (toast==null){
                toast=Toast.makeText(context,"",Toast.LENGTH_SHORT);
            }
            toast.setText(text);
            toast.show();
        });
    }

    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 2000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }


    public static String getSsid(WifiManager wm) {
//        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        if (wm != null) {
            WifiInfo wi = wm.getConnectionInfo();
            if (wi != null) {
                String ssid = wi.getSSID();
                String bssid = wi.getBSSID();
                if ("".equals(ssid)||"".equals(bssid)){
                    return "";
                }
                if (ssid.length() > 2 && ssid.startsWith("\"") && ssid.endsWith("\"")) {
                    return ssid.substring(1, ssid.length() - 1);
                } else {
                    return ssid;
                }
            }
        }
        return "";
    }

    public static String replaceString(String avatar) {
        if (avatar.contains("tempimg")){
            avatar = avatar.replace("tempimg","TempImages");
        }
        if (avatar.contains("avatar")){
            avatar = avatar.replace("avatar","Avatar");
        }if (!avatar.startsWith("http")){
            avatar="http://"+avatar;
        }
        return avatar;
    }
}
