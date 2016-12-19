package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/10.
 */

public class TagInfoBean implements Parcelable {

    /**
     * ACODE : 571
     * ACTIVITYDATE : 2016-09-27 13:57:19
     * AID : K02G-1-14-1-012-1-1
     * ANAME : 杭州
     * APADDR : 119
     * APIP : 192.168.0.195
     * BARCODE : 0000000513760
     * BATTERY : 3008
     * GCODE : 
     * GNAME : M&M'Sm&ms迷你筒装巧克力30.6g30.6g
     * LASTRETDATE : 2016-09-21 15:30:32
     * LQI : -31
     * PCODE : 8601
     * PORT : 57345
     * SID : 571002001
     * SNAME : 濮家店
     * TINYIP : 0.0.75.150
     */

    public String ACODE;
    public String ACTIVITYDATE;
    public String AID;
    public String ANAME;
    public String APADDR;
    public String APIP;
    public String BARCODE;
    public int BATTERY;
    public String GCODE;
    public String GNAME;
    public String LASTRETDATE;
    public int LQI;
    public String PCODE;
    public String PORT;
    public String SID;
    public String SNAME;
    public String TINYIP;


    public TagInfoBean(String ACODE, String ACTIVITYDATE, String AID, String ANAME, String APADDR, String APIP, String BARCODE, int BATTERY, String GCODE, String GNAME, String LASTRETDATE, int LQI, String PCODE, String PORT, String SID, String SNAME, String TINYIP) {
        this.ACODE = ACODE;
        this.ACTIVITYDATE = ACTIVITYDATE;
        this.AID = AID;
        this.ANAME = ANAME;
        this.APADDR = APADDR;
        this.APIP = APIP;
        this.BARCODE = BARCODE;
        this.BATTERY = BATTERY;
        this.GCODE = GCODE;
        this.GNAME = GNAME;
        this.LASTRETDATE = LASTRETDATE;
        this.LQI = LQI;
        this.PCODE = PCODE;
        this.PORT = PORT;
        this.SID = SID;
        this.SNAME = SNAME;
        this.TINYIP = TINYIP;
    }

    public TagInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.ACODE);
        dest.writeString(this.ACTIVITYDATE);
        dest.writeString(this.AID);
        dest.writeString(this.ANAME);
        dest.writeString(this.APADDR);
        dest.writeString(this.APIP);
        dest.writeString(this.BARCODE);
        dest.writeInt(this.BATTERY);
        dest.writeString(this.GCODE);
        dest.writeString(this.GNAME);
        dest.writeString(this.LASTRETDATE);
        dest.writeInt(this.LQI);
        dest.writeString(this.PCODE);
        dest.writeString(this.PORT);
        dest.writeString(this.SID);
        dest.writeString(this.SNAME);
        dest.writeString(this.TINYIP);
    }

    protected TagInfoBean(Parcel in) {
        this.ACODE = in.readString();
        this.ACTIVITYDATE = in.readString();
        this.AID = in.readString();
        this.ANAME = in.readString();
        this.APADDR = in.readString();
        this.APIP = in.readString();
        this.BARCODE = in.readString();
        this.BATTERY = in.readInt();
        this.GCODE = in.readString();
        this.GNAME = in.readString();
        this.LASTRETDATE = in.readString();
        this.LQI = in.readInt();
        this.PCODE = in.readString();
        this.PORT = in.readString();
        this.SID = in.readString();
        this.SNAME = in.readString();
        this.TINYIP = in.readString();
    }

    public static final Parcelable.Creator<TagInfoBean> CREATOR = new Parcelable.Creator<TagInfoBean>() {
        @Override
        public TagInfoBean createFromParcel(Parcel source) {
            return new TagInfoBean(source);
        }

        @Override
        public TagInfoBean[] newArray(int size) {
            return new TagInfoBean[size];
        }
    };

    @Override
    public String toString() {
        return "TagInfoBean{" +
                "ACODE='" + ACODE + '\'' +
                ", ACTIVITYDATE='" + ACTIVITYDATE + '\'' +
                ", AID='" + AID + '\'' +
                ", ANAME='" + ANAME + '\'' +
                ", APADDR='" + APADDR + '\'' +
                ", APIP='" + APIP + '\'' +
                ", BARCODE='" + BARCODE + '\'' +
                ", BATTERY=" + BATTERY +
                ", GCODE='" + GCODE + '\'' +
                ", GNAME='" + GNAME + '\'' +
                ", LASTRETDATE='" + LASTRETDATE + '\'' +
                ", LQI=" + LQI +
                ", PCODE='" + PCODE + '\'' +
                ", PORT='" + PORT + '\'' +
                ", SID='" + SID + '\'' +
                ", SNAME='" + SNAME + '\'' +
                ", TINYIP='" + TINYIP + '\'' +
                '}';
    }

    public void setACODE(String ACODE) {
        this.ACODE = ACODE;
    }

    public void setACTIVITYDATE(String ACTIVITYDATE) {
        this.ACTIVITYDATE = ACTIVITYDATE;
    }

    public void setAID(String AID) {
        this.AID = AID;
    }

    public void setANAME(String ANAME) {
        this.ANAME = ANAME;
    }

    public void setAPADDR(String APADDR) {
        this.APADDR = APADDR;
    }

    public void setAPIP(String APIP) {
        this.APIP = APIP;
    }

    public void setBARCODE(String BARCODE) {
        this.BARCODE = BARCODE;
    }

    public void setBATTERY(int BATTERY) {
        this.BATTERY = BATTERY;
    }

    public void setGCODE(String GCODE) {
        this.GCODE = GCODE;
    }

    public void setGNAME(String GNAME) {
        this.GNAME = GNAME;
    }

    public void setLASTRETDATE(String LASTRETDATE) {
        this.LASTRETDATE = LASTRETDATE;
    }

    public void setLQI(int LQI) {
        this.LQI = LQI;
    }

    public void setPCODE(String PCODE) {
        this.PCODE = PCODE;
    }

    public void setPORT(String PORT) {
        this.PORT = PORT;
    }

    public void setSID(String SID) {
        this.SID = SID;
    }

    public void setSNAME(String SNAME) {
        this.SNAME = SNAME;
    }

    public void setTINYIP(String TINYIP) {
        this.TINYIP = TINYIP;
    }
}
