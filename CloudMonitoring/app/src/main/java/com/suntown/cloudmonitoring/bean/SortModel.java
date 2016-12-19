package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/9/21.
 */

public class SortModel {
    public String name;   //显示的数据
    public String sortLetters;  //显示数据拼音的首字母

    public void setName(String name) {
        this.name = name;
    }

    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
