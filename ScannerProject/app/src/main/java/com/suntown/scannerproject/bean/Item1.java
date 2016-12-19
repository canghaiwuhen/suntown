package com.suntown.scannerproject.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class Item1 implements Parcelable {

    public String sname;
    public String sid;

    public Item1() {
    }

    public Item1(String sname, String sid) {
        this.sname = sname;
        this.sid = sid;
    }

    @Override
    public String toString() {
        return "Item1{" +
                "sname='" + sname + '\'' +
                ", sid='" + sid + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sname);
        dest.writeString(this.sid);
    }

    protected Item1(Parcel in) {
        this.sname = in.readString();
        this.sid = in.readString();
    }

    public static final Parcelable.Creator<Item1> CREATOR = new Parcelable.Creator<Item1>() {
        @Override
        public Item1 createFromParcel(Parcel source) {
            return new Item1(source);
        }

        @Override
        public Item1[] newArray(int size) {
            return new Item1[size];
        }
    };
}
