package com.suntown.scannerproject.query.bean;


import android.os.Parcel;
import android.os.Parcelable;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

/**
 * Created by Administrator on 2016/10/18.
 */
@Table(name = "person")
public class Person implements Parcelable {
    @Column(name = "id",isId = true,autoGen = true)
    private int id;
    @Column(name = "time")
    public long time;
    @Column(name = "sid")
    public String sid;
    //商品条码
    @Column(name = "barcode")
    public String barcode;
    //店铺名
    @Column(name = "name")
    public String name;
    //商品名称
    @Column(name = "gname")
    public String gname;
    //标签名称
    @Column(name = "ip")
    public String ip;
    @Column(name = "userid")
    public String userid;
    //服务器名称
    @Column(name = "serverip")
    public String serverip;

    public Person() {

    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGname(String gname) {
        this.gname = gname;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public long getTime() {
        return time;
    }

    public String getSid() {
        return sid;
    }

    public String getBarcode() {
        return barcode;
    }

    public String getName() {
        return name;
    }

    public String getGname() {
        return gname;
    }

    public String getIp() {
        return ip;
    }

    public String getUserid() {
        return userid;
    }

    public String getServerip() {
        return serverip;
    }

    public void setServerip(String serverip) {
        this.serverip = serverip;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", time=" + time +
                ", sid='" + sid + '\'' +
                ", barcode='" + barcode + '\'' +
                ", name='" + name + '\'' +
                ", gname='" + gname + '\'' +
                ", ip='" + ip + '\'' +
                ", userid='" + userid + '\'' +
                ", serverip='" + serverip + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeLong(this.time);
        dest.writeString(this.sid);
        dest.writeString(this.barcode);
        dest.writeString(this.name);
        dest.writeString(this.gname);
        dest.writeString(this.ip);
        dest.writeString(this.userid);
        dest.writeString(this.serverip);
    }

    protected Person(Parcel in) {
        this.id = in.readInt();
        this.time = in.readLong();
        this.sid = in.readString();
        this.barcode = in.readString();
        this.name = in.readString();
        this.gname = in.readString();
        this.ip = in.readString();
        this.userid = in.readString();
        this.serverip = in.readString();
    }

    public static final Creator<Person> CREATOR = new Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };
}
