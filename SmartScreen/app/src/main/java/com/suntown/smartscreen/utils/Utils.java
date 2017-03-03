package com.suntown.smartscreen.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
                toast= Toast.makeText(context,"", Toast.LENGTH_SHORT);
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
    //获取昨天
    public static Date getLastDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, -1);
        date = calendar.getTime();
        return date;
    }
    public static String Time() {
        Date date=new Date();
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = dateFormater.format(date);
        return time;
    }
}
