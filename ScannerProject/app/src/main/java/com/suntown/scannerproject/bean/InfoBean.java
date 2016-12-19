package com.suntown.scannerproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/21.
 */

public class InfoBean implements Parcelable {
    public String sid;
    public String tinyip;

    public InfoBean(String sid, String tinyip) {
        this.sid = sid;
        this.tinyip = tinyip;
    }

    @Override
    public String toString() {
        return "InfoBean{" +
                "sid='" + sid + '\'' +
                ", tinyip='" + tinyip + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sid);
        dest.writeString(this.tinyip);
    }

    protected InfoBean(Parcel in) {
        this.sid = in.readString();
        this.tinyip = in.readString();
    }

    public static final Creator<InfoBean> CREATOR = new Creator<InfoBean>() {
        @Override
        public InfoBean createFromParcel(Parcel source) {
            return new InfoBean(source);
        }

        @Override
        public InfoBean[] newArray(int size) {
            return new InfoBean[size];
        }
    };
}
