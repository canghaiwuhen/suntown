package com.suntown.smartscreen.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/2/9.
 */

public class GoodsInfo implements Parcelable {
    /**
     * ROWS : 8
     * RECORD : [{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.111.245","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.111.230","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.111.95","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.111.38","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.111.31","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.136.44","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.56.73","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"},{"MEMPRICE":"","BARCODE":"026102251904","CURPRICE":"7.1","TINYIP":"0.0.52.33","COSTPRICE":"0","GNAME":"弓箭南尼斯直身杯12178FB30"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "GoodsInfo{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }


    public static class RECORDBean implements Parcelable {
        /**
         * MEMPRICE :  会员价
         * BARCODE : 026102251904
         * CURPRICE : 7.1   现价
         * TINYIP : 0.0.111.245
         * COSTPRICE : 0   原价
         * GNAME : 弓箭南尼斯直身杯12178FB30
         */

        public String MEMPRICE;
        public String BARCODE;
        public String CURPRICE;
        public String TINYIP;
        public String COSTPRICE;
        public String GNAME;
        public String STARTTIME;
        public String ENDTIME;
        public String VIP;
        public String TYPE;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.MEMPRICE);
            dest.writeString(this.BARCODE);
            dest.writeString(this.CURPRICE);
            dest.writeString(this.TINYIP);
            dest.writeString(this.COSTPRICE);
            dest.writeString(this.GNAME);
            dest.writeString(this.STARTTIME);
            dest.writeString(this.ENDTIME);
            dest.writeString(this.VIP);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.MEMPRICE = in.readString();
            this.BARCODE = in.readString();
            this.CURPRICE = in.readString();
            this.TINYIP = in.readString();
            this.COSTPRICE = in.readString();
            this.GNAME = in.readString();
            this.STARTTIME = in.readString();
            this.ENDTIME = in.readString();
            this.VIP = in.readString();
        }

        public static final Creator<RECORDBean> CREATOR = new Creator<RECORDBean>() {
            @Override
            public RECORDBean createFromParcel(Parcel source) {
                return new RECORDBean(source);
            }

            @Override
            public RECORDBean[] newArray(int size) {
                return new RECORDBean[size];
            }
        };

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "MEMPRICE='" + MEMPRICE + '\'' +
                    ", BARCODE='" + BARCODE + '\'' +
                    ", CURPRICE='" + CURPRICE + '\'' +
                    ", TINYIP='" + TINYIP + '\'' +
                    ", COSTPRICE='" + COSTPRICE + '\'' +
                    ", GNAME='" + GNAME + '\'' +
                    ", STARTTIME='" + STARTTIME + '\'' +
                    ", ENDTIME='" + ENDTIME + '\'' +
                    ", VIP='" + VIP + '\'' +
                    ", TYPE='" + TYPE + '\'' +
                    '}';
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ROWS);
        dest.writeTypedList(this.RECORD);
    }

    public GoodsInfo() {
    }

    protected GoodsInfo(Parcel in) {
        this.ROWS = in.readInt();
        this.RECORD = in.createTypedArrayList(RECORDBean.CREATOR);
    }

    public static final Parcelable.Creator<GoodsInfo> CREATOR = new Parcelable.Creator<GoodsInfo>() {
        @Override
        public GoodsInfo createFromParcel(Parcel source) {
            return new GoodsInfo(source);
        }

        @Override
        public GoodsInfo[] newArray(int size) {
            return new GoodsInfo[size];
        }
    };
}
