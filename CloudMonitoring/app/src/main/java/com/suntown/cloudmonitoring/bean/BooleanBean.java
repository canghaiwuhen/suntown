package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/11/4.
 */
public class BooleanBean {
    public boolean istoogleon;

    public BooleanBean(boolean istoogleon) {
        this.istoogleon = istoogleon;
    }

    public BooleanBean() {
    }

    @Override
    public String toString() {
        return "BooleanBean{" +
                "istoogleon=" + istoogleon +
                '}';
    }
}
