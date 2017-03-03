package com.suntown.smartscreen.maintain.allocation;

/**
 * Created by Administrator on 2017/2/14.
 */

public class InfoBean {
    public String name;
    public String sid;
    public boolean isShow;
    public boolean isCheched = false;

    public InfoBean(String name,boolean isShow) {
        this.name = name;
        this.isShow = isShow;
    }
}
