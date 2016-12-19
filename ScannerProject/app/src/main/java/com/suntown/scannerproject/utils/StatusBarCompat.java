package com.suntown.scannerproject.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * http://blog.csdn.net/lmj623565791/article/details/48649563
 * http://blog.csdn.net/ys408973279/article/details/49994407
 * Created by MrFu on 15/12/21.
 */
public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
    /**
     * 是否是小米或者魅族手机
     */
    private static boolean sIsSpecialPhone = false;
    private static boolean sIsFirstJudge = true;

    public static void compat(Activity activity, int statusColor) {
        toCompatBackground(activity, statusColor);
    }

    /**
     * 小米, 魅族用户
     * @param activity
     * @param statusBackgrounColor 状态栏背景色
     * @param isTextColorDark 文字是否是深色
     */
    public static void specialPhoneDeal(Activity activity, int statusBackgrounColor, boolean isTextColorDark){
        if (sIsFirstJudge){
            toDealSpecialPhoneDeal(activity, statusBackgrounColor, isTextColorDark);
            sIsFirstJudge = false;
        }else {
            if (sIsSpecialPhone){
                toDealSpecialPhoneDeal(activity, statusBackgrounColor, isTextColorDark);
            }
        }

    }

    private static void toDealSpecialPhoneDeal(Activity activity, int statusBackgrounColor, boolean isTextColorDark){
        if (statusBackgrounColor != -1){
            if (flymeSetStatusBarLightMode(activity.getWindow(), isTextColorDark)){//深色字
                sIsSpecialPhone = true;
                toCompatBackground(activity, statusBackgrounColor);//浅色背景  activity.getResources().getColor(R.color.color_white_d90)
            }
            if (mIUISetStatusBarLightMode(activity.getWindow(), isTextColorDark)){//深色字
                sIsSpecialPhone = true;
                toCompatBackground(activity, statusBackgrounColor);//浅色背景
            }
        }
    }

    private static void toCompatBackground(Activity activity, int statusColor){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor != INVALID_VAL) {
                activity.getWindow().setStatusBarColor(statusColor);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_VAL) {
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }
    }

    public static void compat(Activity activity) {
        compat(activity, INVALID_VAL);
    }


    private static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格，Flyme4.0以上
     * 可以用来判断是否为Flyme用户
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    private static boolean flymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
                darkFlag.setAccessible(true);
                meizuFlags.setAccessible(true);
                int bit = darkFlag.getInt(null);
                int value = meizuFlags.getInt(lp);
                if (dark) {
                    value |= bit;
                } else {
                    value &= ~bit;
                }
                meizuFlags.setInt(lp, value);
                window.setAttributes(lp);
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     * @param window 需要设置的窗口
     * @param dark 是否把状态栏字体及图标颜色设置为深色
     * @return  boolean 成功执行返回true
     *
     */
    private static boolean mIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field  field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if(dark){
                    extraFlagField.invoke(window,darkModeFlag,darkModeFlag);//状态栏透明且黑色字体
                }else{
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result=true;
            }catch (Exception e){

            }
        }
        return result;
    }
}
