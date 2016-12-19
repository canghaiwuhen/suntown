package com.suntown.scannerproject.scanner.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/25.
 */
@Table(name = "ShelfItemBean")
public class ShelfItemBean {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "tag")
    public String tag;
    @Column(name = "barcode")
    public String barcode;
    @Column(name = "gname")
    public String gname;
    //货架
    @Column(name = "sfid")
    public String sfid;
    //门店
    @Column(name = "sid")
    public String sid;
    //用户
    @Column(name = "user")
    public String user;
    @Column(name = "ColNumber")
    public String ColNumber;
    @Column(name = "RowNumber")
    public String RowNumber;

    @Column(name = "time")
    public long time;

    public boolean isClick;

    public boolean isTouch;

    public ShelfItemBean(String tag, String barcode, String gname) {
        this.tag = tag;
        this.barcode = barcode;
        this.gname = gname;
    }

    public ShelfItemBean() {
    }


    @Override
    public String toString() {
        return "ShelfItemBean{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", barcode='" + barcode + '\'' +
                ", gname='" + gname + '\'' +
                ", sfid='" + sfid + '\'' +
                ", sid='" + sid + '\'' +
                ", user='" + user + '\'' +
                ", ColNumber='" + ColNumber + '\'' +
                ", RowNumber='" + RowNumber + '\'' +
                ", time=" + time +
                ", isClick=" + isClick +
                ", isTouch=" + isTouch +
                '}';
    }
}
