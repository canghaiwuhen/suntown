package com.suntown.cloudmonitoring.adapter;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2016/9/14.
 */
public class ViewHolder {
    public static <T extends View> T get(View view,int id){
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder==null){
            viewHolder= new SparseArray<>();
            view.setTag(viewHolder);
        }
        View childView = viewHolder.get(id);
        if (childView==null){
            childView=view.findViewById(id);
            viewHolder.put(id,childView);
        }
        return (T) childView;
    }
}
