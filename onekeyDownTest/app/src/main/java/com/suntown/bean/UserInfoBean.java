package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/8/12.
 */
public class UserInfoBean {

    /**
     * WIFIID : Albert
     * ROWS : 3
     * RECORD : [{"MEMID":"128","TEL":"13355789329","WIFIID":"Albert","NICKNAME":"ren"},{"MEMID":"1070","TEL":"18037106850","WIFIID":"Albert","NICKNAME":"suger"},{"MEMID":"1352","TEL":"15994299518","WIFIID":"Albert","NICKNAME":"panda"}]
     */

    private String WIFIID;
    private int ROWS;
    /**
     * MEMID : 128
     * TEL : 13355789329
     * WIFIID : Albert
     * NICKNAME : ren
     */

    private List<RECORDBean> RECORD;

    public String getWIFIID() {
        return WIFIID;
    }

    public void setWIFIID(String WIFIID) {
        this.WIFIID = WIFIID;
    }

    public int getROWS() {
        return ROWS;
    }

    public void setROWS(int ROWS) {
        this.ROWS = ROWS;
    }

    public List<RECORDBean> getRECORD() {
        return RECORD;
    }

    public void setRECORD(List<RECORDBean> RECORD) {
        this.RECORD = RECORD;
    }

    public static class RECORDBean implements Parcelable {
        private String MEMID;
        private String TEL;
        private String WIFIID;
        private String NICKNAME;

        public String getMEMID() {
            return MEMID;
        }

        public void setMEMID(String MEMID) {
            this.MEMID = MEMID;
        }

        public String getTEL() {
            return TEL;
        }

        public void setTEL(String TEL) {
            this.TEL = TEL;
        }

        public String getWIFIID() {
            return WIFIID;
        }

        public void setWIFIID(String WIFIID) {
            this.WIFIID = WIFIID;
        }

        public String getNICKNAME() {
            return NICKNAME;
        }

        public void setNICKNAME(String NICKNAME) {
            this.NICKNAME = NICKNAME;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.MEMID);
            dest.writeString(this.TEL);
            dest.writeString(this.WIFIID);
            dest.writeString(this.NICKNAME);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.MEMID = in.readString();
            this.TEL = in.readString();
            this.WIFIID = in.readString();
            this.NICKNAME = in.readString();
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
                    ", TEL='" + TEL + '\'' +
                    ", WIFIID='" + WIFIID + '\'' +
                    ", NICKNAME='" + NICKNAME + '\'' +
                    '}';
        }
    }
}
