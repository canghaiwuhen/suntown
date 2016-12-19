package com.suntown.cloudmonitoring.bean;

import com.chad.library.adapter.base.entity.AbstractExpandableItem;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.suntown.cloudmonitoring.adapter.ExceptionAdapter;

/**
 * Created by Shinelon on 2016/9/18.
 */
public class ExceptionLevel0Item {
    //    String aname = recordBean.ANAME;
//    String sid = recordBean.SID;
    public String sname;
    public String sid;

    public ExceptionLevel0Item(String sname, String sid) {
        this.sname = sname;
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "ExceptionLevel0Item{" +
                "sname='" + sname + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }
}
