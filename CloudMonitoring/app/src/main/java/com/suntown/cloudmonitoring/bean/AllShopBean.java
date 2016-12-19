package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/9/20.
 */

public class AllShopBean {

    /**
     * NAME :
     * ROWS : 16
     * USERID : admin
     * RECORD : [{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571003050","SNAME":"北田商贸文二西路店"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"999990067","SNAME":"测试门店67"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571003046","SNAME":"长龙加油站"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002032","SNAME":"图特"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"999990100","SNAME":"测试门店100"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002006","SNAME":"乐购大家钱塘府"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002007","SNAME":"乐木几杭州武林广场店"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571003011","SNAME":"suntown测试门店2"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002005","SNAME":"乐购新塘景芳店"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571566668","SNAME":"仁盈西湖科技园"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571566667","SNAME":"仁盈天亿大厦"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571566666","SNAME":"仁盈天亿大厦test"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571003001","SNAME":"支付宝"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002003","SNAME":"朝晖店"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002002","SNAME":"施家桥店"},{"ACODE":"571","ANAME":"杭州","PCODE":"-1","SID":"571002001","SNAME":"濮家店"}]
     */

    public String NAME;
    public int ROWS;
    public String USERID;
    /**
     * ACODE : 571
     * ANAME : 杭州
     * PCODE : -1
     * SID : 571003050
     * SNAME : 北田商贸文二西路店
     */

    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "AllShopBean{" +
                "NAME='" + NAME + '\'' +
                ", ROWS=" + ROWS +
                ", USERID='" + USERID + '\'' +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean implements Parcelable {
        public String ANAME;
        public String ACODE;
        public String PCODE;
        public String SID;
        public String SNAME;

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "ANAME='" + ANAME + '\'' +
                    ", ACODE='" + ACODE + '\'' +
                    ", PCODE='" + PCODE + '\'' +
                    ", SID='" + SID + '\'' +
                    ", SNAME='" + SNAME + '\'' +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ANAME);
            dest.writeString(this.ACODE);
            dest.writeString(this.PCODE);
            dest.writeString(this.SID);
            dest.writeString(this.SNAME);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.ANAME = in.readString();
            this.ACODE = in.readString();
            this.PCODE = in.readString();
            this.SID = in.readString();
            this.SNAME = in.readString();
        }

        public static final Parcelable.Creator<RECORDBean> CREATOR = new Parcelable.Creator<RECORDBean>() {
            @Override
            public RECORDBean createFromParcel(Parcel source) {
                return new RECORDBean(source);
            }

            @Override
            public RECORDBean[] newArray(int size) {
                return new RECORDBean[size];
            }
        };
    }

}
