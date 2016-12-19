package com.suntown.cloudmonitoring.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.suntown.cloudmonitoring.adapter.ExceptionAdapter;

/**
 * Created by Shinelon on 2016/9/18.
 */
public class ExceptionLevel1Item{


    //APIP : 192.168.0.87
    //APADDR : 1
    // LASTDATE
    //EDCOUNT

    public String APADDR;
    public String APIP;
    public String LASTDATE;
    public int STATUS;
    public int EDCOUNT;


    @Override
    public String toString() {
        return "ExceptionLevel1Item{" +
                "APADDR='" + APADDR + '\'' +
                ", APIP='" + APIP + '\'' +
                ", LASTDATE='" + LASTDATE + '\'' +
                ", STATUS=" + STATUS +
                ", EDCOUNT=" + EDCOUNT +
                '}';
    }

    public ExceptionLevel1Item(int EDCOUNT, String APADDR, String APIP, String LASTDATE , int STATUS) {
        this.EDCOUNT = EDCOUNT;
        this.APADDR = APADDR;
        this.APIP = APIP;
        this.LASTDATE = LASTDATE;
        this.STATUS=STATUS;
    }

}
