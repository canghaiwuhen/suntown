package com.suntown.smartscreen.price;


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
    public int id;
    @Column(name = "time")
    public long time;
    @Column(name = "sid")
    public String sid;
    //商品条码
    @Column(name = "BARCODE")
    public String BARCODE;
    //店铺名
    @Column(name = "name")
    public String name;
    //商品名称
    @Column(name = "GNAME")
    public String GNAME;
    //标签名称
    @Column(name = "TINYIP")
    public String TINYIP;
    @Column(name = "userid")
    public String userid;
    //服务器名称
    @Column(name = "serverip")
    public String serverip;
    @Column(name = "CURPRICE")
    public String CURPRICE;
    @Column(name = "COSTPRICE")
    public String COSTPRICE;
    @Column(name = "MEMPRICE")
    public String MEMPRICE;

    public Person() {

    }


    public Person( long time, String sid, String BARCODE, String name, String GNAME, String TINYIP, String userid, String serverip, String CURPRICE, String COSTPRICE, String MEMPRICE) {
        this.time = time;
        this.sid = sid;
        this.BARCODE = BARCODE;
        this.name = name;
        this.GNAME = GNAME;
        this.TINYIP = TINYIP;
        this.userid = userid;
        this.serverip = serverip;
        this.CURPRICE = CURPRICE;
        this.COSTPRICE = COSTPRICE;
        this.MEMPRICE = MEMPRICE;
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
        dest.writeString(this.BARCODE);
        dest.writeString(this.name);
        dest.writeString(this.GNAME);
        dest.writeString(this.TINYIP);
        dest.writeString(this.userid);
        dest.writeString(this.serverip);
        dest.writeString(this.CURPRICE);
        dest.writeString(this.COSTPRICE);
        dest.writeString(this.MEMPRICE);
    }

    protected Person(Parcel in) {
        this.id = in.readInt();
        this.time = in.readLong();
        this.sid = in.readString();
        this.BARCODE = in.readString();
        this.name = in.readString();
        this.GNAME = in.readString();
        this.TINYIP = in.readString();
        this.userid = in.readString();
        this.serverip = in.readString();
        this.CURPRICE = in.readString();
        this.COSTPRICE = in.readString();
        this.MEMPRICE = in.readString();
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        @Override
        public Person createFromParcel(Parcel source) {
            return new Person(source);
        }

        @Override
        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", time=" + time +
                ", sid='" + sid + '\'' +
                ", BARCODE='" + BARCODE + '\'' +
                ", name='" + name + '\'' +
                ", GNAME='" + GNAME + '\'' +
                ", TINYIP='" + TINYIP + '\'' +
                ", userid='" + userid + '\'' +
                ", serverip='" + serverip + '\'' +
                ", CURPRICE='" + CURPRICE + '\'' +
                ", COSTPRICE='" + COSTPRICE + '\'' +
                ", MEMPRICE='" + MEMPRICE + '\'' +
                '}';
    }
}
