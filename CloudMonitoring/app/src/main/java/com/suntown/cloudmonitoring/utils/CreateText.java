package com.suntown.cloudmonitoring.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suntown.cloudmonitoring.R;

/**
 * Created by Administrator on 2016/10/13.
 */

public class CreateText {
    public static View CreateTagView(Context context,String tag){
        int ranHeight = dip2px(context, 30);
//        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ranHeight);
//        lp.setMargins(dip2px(context, 10), 0, dip2px(context, 10), 0);
        TextView tv = new TextView(context);
        tv.setPadding(dip2px(context, 15), 0, dip2px(context, 15), 0);
        tv.setTextColor(Color.parseColor("#FEB73A"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        tv.setGravity(Gravity.CENTER_VERTICAL);
        Drawable drawable= context.getResources().getDrawable(R.drawable.delete);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        tv.setCompoundDrawables(null,null,drawable,null);
        tv.setCompoundDrawablePadding(5);
        tv.setLines(1);
        tv.setBackgroundResource(R.drawable.item_bg);

        return tv;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
