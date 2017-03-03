package com.suntown.smartscreen.maintain.allocation;

/**
 * Created by Administrator on 2017/2/14.
 */

public class SubBean {
    public String sid;
    public int delete; //( 1 删除  0 插入)


    public SubBean(String sid, int delete) {
        this.sid = sid;
        this.delete = delete;
    }
}
