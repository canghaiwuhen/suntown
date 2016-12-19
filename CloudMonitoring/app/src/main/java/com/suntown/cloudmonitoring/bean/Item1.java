package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/10/26.
 */

public class Item1 {
    public String FALLRANGE;
    public int  ROWS;

    public Item1() {
    }

    public Item1(String FALLRANGE, int ROWS) {
        this.FALLRANGE = FALLRANGE;
        this.ROWS = ROWS;
    }

    @Override
    public String toString() {
        return "Item1{" +
                "FALLRANGE='" + FALLRANGE + '\'' +
                ", ROWS=" + ROWS +
                '}';
    }
}
