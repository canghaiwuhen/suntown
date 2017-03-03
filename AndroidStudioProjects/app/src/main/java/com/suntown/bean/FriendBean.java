package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/12/27.
 */

public class FriendBean implements Parcelable {
    /**
     * TEL : 15994299518
     * ROWS : 1
     * RECORD : [{"MEMID":"1352","TEL":"15994299518","NICKNAME":"panda","WIFIID":"我的WiFi","SEX":"","AVATAR":"http://192.168.0.12:8081/TempImages//Avatar/1352.jpg"}]
     */

    public String TEL;
    public int ROWS;
    public List<RECORDBean> RECORD;

    public static class RECORDBean implements Parcelable {
        /**
         * MEMID : 1352
         * TEL : 15994299518
         * NICKNAME : panda
         * WIFIID : 我的WiFi
         * SEX : 
         * AVATAR : http://192.168.0.12:8081/TempImages//Avatar/1352.jpg
         */

        public String MEMID;
        public String TEL;
        public String NICKNAME;
        public String WIFIID;
        public String SEX;
        public String AVATAR;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.MEMID);
            dest.writeString(this.TEL);
            dest.writeString(this.NICKNAME);
            dest.writeString(this.WIFIID);
            dest.writeString(this.SEX);
            dest.writeString(this.AVATAR);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.MEMID = in.readString();
            this.TEL = in.readString();
            this.NICKNAME = in.readString();
            this.WIFIID = in.readString();
            this.SEX = in.readString();
            this.AVATAR = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.TEL);
        dest.writeInt(this.ROWS);
        dest.writeTypedList(this.RECORD);
    }

    public FriendBean() {
    }

    protected FriendBean(Parcel in) {
        this.TEL = in.readString();
        this.ROWS = in.readInt();
        this.RECORD = in.createTypedArrayList(RECORDBean.CREATOR);
    }

    public static final Parcelable.Creator<FriendBean> CREATOR = new Parcelable.Creator<FriendBean>() {
        @Override
        public FriendBean createFromParcel(Parcel source) {
            return new FriendBean(source);
        }

        @Override
        public FriendBean[] newArray(int size) {
            return new FriendBean[size];
        }
    };
}
