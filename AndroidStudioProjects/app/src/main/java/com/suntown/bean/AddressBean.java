package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/8/22.
 */
public class AddressBean {

    /**>
     * RESULT : 0
     * RECORD : [{"ID":"761","ADDDATE":"2016-08-22 13:48:14.0","MEMID":"1352","ADDRESS":"杭州西湖","ISDEFAULT":"0","PHONE":"15537450621","RECEIVER":"汤振"},{"ID":"762","ADDDATE":"2016-08-22 14:00:34.0","MEMID":"1352","ADDRESS":"浙江省杭州市西湖区振华路西港新界西区，A憧16楼","ISDEFAULT":"1","PHONE":"12345678977","RECEIVER":"汤振"}]
     */

    private String RESULT;
    /**
     * ID : 761
     * ADDDATE : 2016-08-22 13:48:14.0
     * MEMID : 1352
     * ADDRESS : 杭州西湖
     * ISDEFAULT : 0
     * PHONE : 15537450621
     * RECEIVER : 汤振
     */

    private List<RECORDBean> RECORD;

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }

    public List<RECORDBean> getRECORD() {
        return RECORD;
    }

    public void setRECORD(List<RECORDBean> RECORD) {
        this.RECORD = RECORD;
    }

    @Override
    public String toString() {
        return "AddressBean{" +
                "RESULT='" + RESULT + '\'' +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean implements Parcelable {
        public String ID;
        public String ADDDATE;
        public String MEMID;
        public String ADDRESS;
        public String ISDEFAULT;
        public String PHONE;
        public String RECEIVER;
        public boolean isClick=false;

        public RECORDBean(String ID, String ADDDATE, String MEMID, String ADDRESS, String ISDEFAULT, String PHONE, String RECEIVER) {
            this.ID = ID;
            this.ADDDATE = ADDDATE;
            this.MEMID = MEMID;
            this.ADDRESS = ADDRESS;
            this.ISDEFAULT = ISDEFAULT;
            this.PHONE = PHONE;
            this.RECEIVER = RECEIVER;
        }


        @Override
        public String toString() {
            return "RECORDBean{" +
                    "ID='" + ID + '\'' +
                    ", ADDDATE='" + ADDDATE + '\'' +
                    ", MEMID='" + MEMID + '\'' +
                    ", ADDRESS='" + ADDRESS + '\'' +
                    ", ISDEFAULT='" + ISDEFAULT + '\'' +
                    ", PHONE='" + PHONE + '\'' +
                    ", RECEIVER='" + RECEIVER + '\'' +
                    ", isClick=" + isClick +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ID);
            dest.writeString(this.ADDDATE);
            dest.writeString(this.MEMID);
            dest.writeString(this.ADDRESS);
            dest.writeString(this.ISDEFAULT);
            dest.writeString(this.PHONE);
            dest.writeString(this.RECEIVER);
            dest.writeByte(this.isClick ? (byte) 1 : (byte) 0);
        }

        protected RECORDBean(Parcel in) {
            this.ID = in.readString();
            this.ADDDATE = in.readString();
            this.MEMID = in.readString();
            this.ADDRESS = in.readString();
            this.ISDEFAULT = in.readString();
            this.PHONE = in.readString();
            this.RECEIVER = in.readString();
            this.isClick = in.readByte() != 0;
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
