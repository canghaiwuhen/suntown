package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/11/2.
 */
@Table(name = "inOutBean")
public class InOutBean implements Parcelable {
    @Column(name = "id",isId = true)
    public int id;
    @Column(name = "orderNum")
    public String orderNum;
    @Column(name = "gname")
    public String gname;
    @Column(name = "barcode")
    public String barcode;
    //入库 1  或者 出库 2
    @Column(name = "moudleName")
    public int moudleName;

    @Column(name = "goodsNum")
    public String goodsNum;

    @Column(name = "boxNum")
    public String boxNum;

    @Column(name = "puductDate")
    public String puductDate;

//    @Column(name = "date")
//    public String date;

    @Column(name = "sid")
    public String sid;

    @Column(name = "userId")
    public String userId;

    public InOutBean() {
    }


    public InOutBean(String orderNum, String gname, String barcode, int moudleName, String goodsNum, String boxNum, String puductDate, String sid, String userId) {
        this.orderNum = orderNum;
        this.gname = gname;
        this.barcode = barcode;
        this.moudleName = moudleName;
        this.goodsNum = goodsNum;
        this.boxNum = boxNum;
        this.puductDate = puductDate;
        this.sid = sid;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "InOutBean{" +
                "id=" + id +
                ", orderNum='" + orderNum + '\'' +
                ", gname='" + gname + '\'' +
                ", barcode='" + barcode + '\'' +
                ", moudleName=" + moudleName +
                ", goodsNum='" + goodsNum + '\'' +
                ", boxNum='" + boxNum + '\'' +
                ", puductDate='" + puductDate + '\'' +
                ", sid='" + sid + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.orderNum);
        dest.writeString(this.gname);
        dest.writeString(this.barcode);
        dest.writeInt(this.moudleName);
        dest.writeString(this.goodsNum);
        dest.writeString(this.boxNum);
        dest.writeString(this.puductDate);
        dest.writeString(this.sid);
        dest.writeString(this.userId);
    }

    protected InOutBean(Parcel in) {
        this.id = in.readInt();
        this.orderNum = in.readString();
        this.gname = in.readString();
        this.barcode = in.readString();
        this.moudleName = in.readInt();
        this.goodsNum = in.readString();
        this.boxNum = in.readString();
        this.puductDate = in.readString();
        this.sid = in.readString();
        this.userId = in.readString();
    }

    public static final Parcelable.Creator<InOutBean> CREATOR = new Parcelable.Creator<InOutBean>() {
        @Override
        public InOutBean createFromParcel(Parcel source) {
            return new InOutBean(source);
        }

        @Override
        public InOutBean[] newArray(int size) {
            return new InOutBean[size];
        }
    };
}
