package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/13.
 */

public class FiltrateBean implements Parcelable {
        public String ANAME;
        public String SNAME;

    public FiltrateBean(String ANAME, String SNAME) {
        this.ANAME = ANAME;
        this.SNAME = SNAME;
    }

    @Override
    public boolean equals(Object obj) {
        FiltrateBean filtrateBean = (FiltrateBean) obj;
        return ANAME.equals(filtrateBean.ANAME)&&SNAME.equals(filtrateBean.SNAME);
    }

    @Override
    public int hashCode() {
        String str = ANAME+SNAME;
        return str.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ANAME);
        dest.writeString(this.SNAME);
    }

    public FiltrateBean() {
    }

    protected FiltrateBean(Parcel in) {
        this.ANAME = in.readString();
        this.SNAME = in.readString();
    }

    public static final Parcelable.Creator<FiltrateBean> CREATOR = new Parcelable.Creator<FiltrateBean>() {
        @Override
        public FiltrateBean createFromParcel(Parcel source) {
            return new FiltrateBean(source);
        }

        @Override
        public FiltrateBean[] newArray(int size) {
            return new FiltrateBean[size];
        }
    };

    @Override
    public String toString() {
        return "FiltrateBean{" +
                "ANAME='" + ANAME + '\'' +
                ", SNAME='" + SNAME + '\'' +
                '}';
    }
}
