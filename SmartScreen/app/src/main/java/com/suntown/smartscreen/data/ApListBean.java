package com.suntown.smartscreen.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ApListBean {
    /**
     * ROWS : 11
     * RECORD : [{"LASTTASKDATE":"2017-01-23 19:02:43.0","APID":"6","PORTNO":"57345","APLIST":"","APADDR":"72","SN":"K0F2F4B68VIA","RSSIVALUE":"2","LASTDATE":"2016-07-26 10:05:04.0","SWVERSION":"27","SID":"571002002","F1":"0","HWVERSION":"0","F0":"21","SYSTEMINFOVERSION":"240","APRESETINDEX":"54","ID":"1","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":"261"},{"LASTTASKDATE":"2016-12-14 09:51:56.0","APID":"1","PORTNO":"52567","APLIST":"","APADDR":"4","SN":"X0O294568AO9","RSSIVALUE":"2","LASTDATE":"2016-05-31 11:56:56.0","SWVERSION":"28","SID":"571002002","F1":"0","HWVERSION":"0","F0":"15","SYSTEMINFOVERSION":"40","APRESETINDEX":"69","ID":"41","HWTYPE":"0","APIP":"117.136.78.186","RSSICOUNT":"2","SWID":""},{"LASTTASKDATE":"2016-07-05 17:39:00.0","APID":"11","PORTNO":"57345","APLIST":"","APADDR":"77","SN":"O0W2R4A68VXP","RSSIVALUE":"2","LASTDATE":"2016-07-05 17:35:52.0","SWVERSION":"27","SID":"571002002","F1":"0","HWVERSION":"0","F0":"27","SYSTEMINFOVERSION":"90","APRESETINDEX":"9","ID":"565","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":"261"},{"LASTTASKDATE":"2017-01-23 13:53:28.0","APID":"12","PORTNO":"57347","APLIST":"","APADDR":"78","SN":"204214668V0N","RSSIVALUE":"2","LASTDATE":"2016-11-24 15:49:05.0","SWVERSION":"28","SID":"571002002","F1":"0","HWVERSION":"0","F0":"30","SYSTEMINFOVERSION":"200","APRESETINDEX":"72","ID":"566","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":"261"},{"LASTTASKDATE":"2017-02-10 14:40:30.0","APID":"13","PORTNO":"57345","APLIST":"","APADDR":"79","SN":"G0U2N4768V3I","RSSIVALUE":"2","LASTDATE":"2017-01-09 09:42:46.0","SWVERSION":"52","SID":"571002002","F1":"0","HWVERSION":"0","F0":"36","SYSTEMINFOVERSION":"130","APRESETINDEX":"13","ID":"567","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":"261"},{"LASTTASKDATE":"2016-11-17 09:00:20.0","APID":"14","PORTNO":"57345","APLIST":"","APADDR":"81","SN":"90M2H4M68YFE","RSSIVALUE":"2","LASTDATE":"2016-06-13 14:51:15.0","SWVERSION":"13","SID":"571002002","F1":"0","HWVERSION":"0","F0":"18","SYSTEMINFOVERSION":"40","APRESETINDEX":"4","ID":"586","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":"261"},{"LASTTASKDATE":"2016-05-31 16:07:05.0","APID":"21","PORTNO":"1000","APLIST":"","APADDR":"89","SN":"R0N2P4C68Y3L","RSSIVALUE":"2","LASTDATE":"","SWVERSION":"","SID":"571002002","F1":"0","HWVERSION":"","F0":"","SYSTEMINFOVERSION":"0","APRESETINDEX":"0","ID":"625","HWTYPE":"","APIP":"192.168.0.142","RSSICOUNT":"2","SWID":""},{"LASTTASKDATE":"2017-02-13 16:15:16.0","APID":"27","PORTNO":"57345","APLIST":"","APADDR":"100","SN":"V0T22426BACU","RSSIVALUE":"2","LASTDATE":"2016-07-11 13:15:34.0","SWVERSION":"28","SID":"571002002","F1":"0","HWVERSION":"0","F0":"33","SYSTEMINFOVERSION":"200","APRESETINDEX":"72","ID":"686","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":""},{"LASTTASKDATE":"2016-11-16 10:10:02.0","APID":"30","PORTNO":"57345","APLIST":"","APADDR":"103","SN":"W052T4O6BALI","RSSIVALUE":"2","LASTDATE":"2016-07-27 08:54:02.0","SWVERSION":"13","SID":"571002002","F1":"0","HWVERSION":"0","F0":"39","SYSTEMINFOVERSION":"20","APRESETINDEX":"2","ID":"726","HWTYPE":"0","APIP":"115.236.37.19","RSSICOUNT":"2","SWID":""},{"LASTTASKDATE":"2016-11-15 19:22:24.0","APID":"49","PORTNO":"1000","APLIST":"","APADDR":"127","SN":"U042S4U6BGXM","RSSIVALUE":"2","LASTDATE":"","SWVERSION":"","SID":"571002002","F1":"0","HWVERSION":"","F0":"","SYSTEMINFOVERSION":"0","APRESETINDEX":"0","ID":"994","HWTYPE":"","APIP":"192.168.0.199","RSSICOUNT":"2","SWID":""},{"LASTTASKDATE":"2017-01-04 15:31:20.0","APID":"50","PORTNO":"1000","APLIST":"","APADDR":"308","SN":"I0E2O4B6HA0K","RSSIVALUE":"2","LASTDATE":"","SWVERSION":"","SID":"571002002","F1":"0","HWVERSION":"","F0":"","SYSTEMINFOVERSION":"0","APRESETINDEX":"0","ID":"1270","HWTYPE":"","APIP":"192.168.0.211","RSSICOUNT":"2","SWID":""}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "ApListBean{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean implements Parcelable {
        /**
         * LASTTASKDATE : 2017-01-23 19:02:43.0
         * APID : 6
         * PORTNO : 57345
         * APLIST :
         * APADDR : 72
         * SN : K0F2F4B68VIA
         * RSSIVALUE : 2
         * LASTDATE : 2016-07-26 10:05:04.0
         * SWVERSION : 27
         * SID : 571002002
         * F1 : 0
         * HWVERSION : 0
         * F0 : 21
         * SYSTEMINFOVERSION : 240
         * APRESETINDEX : 54
         * ID : 1
         * HWTYPE : 0
         * APIP : 115.236.37.19
         * RSSICOUNT : 2
         * SWID : 261
         */

        public String LASTTASKDATE;
        public String APID;
        public String PORTNO;
        public String APLIST;
        public String APADDR;
        public String SN;
        public String RSSIVALUE;
        public String LASTDATE;
        public String SWVERSION;
        public String SID;
        public String F1;
        public String HWVERSION;
        public String F0;
        public String SYSTEMINFOVERSION;
        public String APRESETINDEX;
        public String ID;
        public String HWTYPE;
        public String APIP;
        public String RSSICOUNT;
        public String SWID;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.LASTTASKDATE);
            dest.writeString(this.APID);
            dest.writeString(this.PORTNO);
            dest.writeString(this.APLIST);
            dest.writeString(this.APADDR);
            dest.writeString(this.SN);
            dest.writeString(this.RSSIVALUE);
            dest.writeString(this.LASTDATE);
            dest.writeString(this.SWVERSION);
            dest.writeString(this.SID);
            dest.writeString(this.F1);
            dest.writeString(this.HWVERSION);
            dest.writeString(this.F0);
            dest.writeString(this.SYSTEMINFOVERSION);
            dest.writeString(this.APRESETINDEX);
            dest.writeString(this.ID);
            dest.writeString(this.HWTYPE);
            dest.writeString(this.APIP);
            dest.writeString(this.RSSICOUNT);
            dest.writeString(this.SWID);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.LASTTASKDATE = in.readString();
            this.APID = in.readString();
            this.PORTNO = in.readString();
            this.APLIST = in.readString();
            this.APADDR = in.readString();
            this.SN = in.readString();
            this.RSSIVALUE = in.readString();
            this.LASTDATE = in.readString();
            this.SWVERSION = in.readString();
            this.SID = in.readString();
            this.F1 = in.readString();
            this.HWVERSION = in.readString();
            this.F0 = in.readString();
            this.SYSTEMINFOVERSION = in.readString();
            this.APRESETINDEX = in.readString();
            this.ID = in.readString();
            this.HWTYPE = in.readString();
            this.APIP = in.readString();
            this.RSSICOUNT = in.readString();
            this.SWID = in.readString();
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
                    "LASTTASKDATE='" + LASTTASKDATE + '\'' +
                    ", APID='" + APID + '\'' +
                    ", PORTNO='" + PORTNO + '\'' +
                    ", APLIST='" + APLIST + '\'' +
                    ", APADDR='" + APADDR + '\'' +
                    ", SN='" + SN + '\'' +
                    ", RSSIVALUE='" + RSSIVALUE + '\'' +
                    ", LASTDATE='" + LASTDATE + '\'' +
                    ", SWVERSION='" + SWVERSION + '\'' +
                    ", SID='" + SID + '\'' +
                    ", F1='" + F1 + '\'' +
                    ", HWVERSION='" + HWVERSION + '\'' +
                    ", F0='" + F0 + '\'' +
                    ", SYSTEMINFOVERSION='" + SYSTEMINFOVERSION + '\'' +
                    ", APRESETINDEX='" + APRESETINDEX + '\'' +
                    ", ID='" + ID + '\'' +
                    ", HWTYPE='" + HWTYPE + '\'' +
                    ", APIP='" + APIP + '\'' +
                    ", RSSICOUNT='" + RSSICOUNT + '\'' +
                    ", SWID='" + SWID + '\'' +
                    '}';
        }
    }
}
