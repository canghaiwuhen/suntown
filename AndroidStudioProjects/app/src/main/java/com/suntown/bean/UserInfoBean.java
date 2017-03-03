package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class UserInfoBean implements Parcelable {

    /**
     * ROWS : 2
     * RECORD : [{"MEMID":"128","NAME":"13355789329","LOGINNAME":"","TEL":"13355789329","ADDRESS":"","NICKNAME":"ren","PUSHID":"13065ffa4e0114c01d2","AVATAR":"www.suntowngis.com:8080/tempimg/avatar/128.jpg"},{"MEMID":"1431","NAME":"朱小刀","LOGINNAME":"","TEL":"15092857577","ADDRESS":"","NICKNAME":"朱小刀","PUSHID":"191e35f7e04c7bb8035","AVATAR":"www.smartesl.com.cn/tempimg/avatar/1431.jpg"}]
     * MEMID : 1070
     */

    public int ROWS;
    public String MEMID;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "UserInfoBean{" +
                "ROWS=" + ROWS +
                ", MEMID='" + MEMID + '\'' +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean implements Parcelable {
        /**
         * MEMID : 128
         * NAME : 13355789329
         * LOGINNAME : 
         * TEL : 13355789329
         * ADDRESS : 
         * NICKNAME : ren
         * PUSHID : 13065ffa4e0114c01d2
         * AVATAR : www.suntowngis.com:8080/tempimg/avatar/128.jpg
         */

        public String MEMID;
        public String NAME;
        public String LOGINNAME;
        public String TEL;
        public String ADDRESS;
        public String NICKNAME;
        public String PUSHID;
        public String AVATAR;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.MEMID);
            dest.writeString(this.NAME);
            dest.writeString(this.LOGINNAME);
            dest.writeString(this.TEL);
            dest.writeString(this.ADDRESS);
            dest.writeString(this.NICKNAME);
            dest.writeString(this.PUSHID);
            dest.writeString(this.AVATAR);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.MEMID = in.readString();
            this.NAME = in.readString();
            this.LOGINNAME = in.readString();
            this.TEL = in.readString();
            this.ADDRESS = in.readString();
            this.NICKNAME = in.readString();
            this.PUSHID = in.readString();
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

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "MEMID='" + MEMID + '\'' +
                    ", NAME='" + NAME + '\'' +
                    ", LOGINNAME='" + LOGINNAME + '\'' +
                    ", TEL='" + TEL + '\'' +
                    ", ADDRESS='" + ADDRESS + '\'' +
                    ", NICKNAME='" + NICKNAME + '\'' +
                    ", PUSHID='" + PUSHID + '\'' +
                    ", AVATAR='" + AVATAR + '\'' +
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
        dest.writeString(this.MEMID);
        dest.writeTypedList(this.RECORD);
    }

    public UserInfoBean() {
    }

    protected UserInfoBean(Parcel in) {
        this.ROWS = in.readInt();
        this.MEMID = in.readString();
        this.RECORD = in.createTypedArrayList(RECORDBean.CREATOR);
    }

    public static final Parcelable.Creator<UserInfoBean> CREATOR = new Parcelable.Creator<UserInfoBean>() {
        @Override
        public UserInfoBean createFromParcel(Parcel source) {
            return new UserInfoBean(source);
        }

        @Override
        public UserInfoBean[] newArray(int size) {
            return new UserInfoBean[size];
        }
    };
}
