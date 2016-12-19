package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/11/10.
 */
public class UpdateBean {
    /**
     * record : 1
     */

    public int record;

    public int getRecord() {
        return record;
    }

    public void setRecord(int record) {
        this.record = record;
    }

    @Override
    public String toString() {
        return "UpdateBean{" +
                "record=" + record +
                '}';
    }
}
