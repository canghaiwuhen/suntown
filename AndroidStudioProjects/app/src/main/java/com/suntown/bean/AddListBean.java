package com.suntown.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class AddListBean {

    /**
     * ROWS : 3
     * RECORD : [{"ID":"21","MEMID":"128","FMID":"1070","ADDDATE":"2016-11-03 13:25:46.0","PASSFLAG":"1","PASSIDEA":"","SELF":{"NICKNAME":"ren","SEX":"","AVATAR":"www.suntowngis.com:8080/tempimg/avatar/128.jpg","TEL":"13355789329"},"FM":{"NICKNAME":"suger","SEX":"0","AVATAR":"www.smartesl.com.cn/tempimg/avatar/1070.jpg","TEL":"18037106850"}},{"ID":"81","MEMID":"1352","FMID":"1070","ADDDATE":"2016-12-26 09:54:03.0","PASSFLAG":"-1","PASSIDEA":"","SELF":{"NICKNAME":"panda","SEX":"","AVATAR":"http://192.168.0.12:8081/TempImages//Avatar/1352.jpg","TEL":"15994299518"},"FM":{"NICKNAME":"suger","SEX":"0","AVATAR":"www.smartesl.com.cn/tempimg/avatar/1070.jpg","TEL":"18037106850"}},{"ID":"62","MEMID":"1070","FMID":"1070","ADDDATE":"2016-12-23 17:07:58.0","PASSFLAG":"-1","PASSIDEA":"","SELF":{"NICKNAME":"suger","SEX":"0","AVATAR":"www.smartesl.com.cn/tempimg/avatar/1070.jpg","TEL":"18037106850"},"FM":{"NICKNAME":"suger","SEX":"0","AVATAR":"www.smartesl.com.cn/tempimg/avatar/1070.jpg","TEL":"18037106850"}}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "AddListBean{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean implements Parcelable {
        /**
         * ID : 21
         * MEMID : 128
         * FMID : 1070
         * ADDDATE : 2016-11-03 13:25:46.0
         * PASSFLAG : 1
         * PASSIDEA : 
         * SELF : {"NICKNAME":"ren","SEX":"","AVATAR":"www.suntowngis.com:8080/tempimg/avatar/128.jpg","TEL":"13355789329"} SELF:发送者
         * FM : {"NICKNAME":"suger","SEX":"0","AVATAR":"www.smartesl.com.cn/tempimg/avatar/1070.jpg","TEL":"18037106850"} FM:接受者
         */

        public String ID;
        public String MEMID;
        public String FMID;
        public String ADDDATE;
        public String PASSFLAG;
        public String PASSIDEA;
        public SELFBean SELF;
        public FMBean FM;

        @Override
        public String toString() {
            return "RECORDBean{" +
                    "ID='" + ID + '\'' +
                    ", MEMID='" + MEMID + '\'' +
                    ", FMID='" + FMID + '\'' +
                    ", ADDDATE='" + ADDDATE + '\'' +
                    ", PASSFLAG='" + PASSFLAG + '\'' +
                    ", PASSIDEA='" + PASSIDEA + '\'' +
                    ", SELF=" + SELF +
                    ", FM=" + FM +
                    '}';
        }


        public static class SELFBean implements Parcelable {
            /**
             * NICKNAME : ren
             * SEX : 
             * AVATAR : www.suntowngis.com:8080/tempimg/avatar/128.jpg
             * TEL : 13355789329
             */

            public String NICKNAME;
            public String SEX;
            public String AVATAR;
            public String TEL;

            @Override
            public String toString() {
                return "SELFBean{" +
                        "NICKNAME='" + NICKNAME + '\'' +
                        ", SEX='" + SEX + '\'' +
                        ", AVATAR='" + AVATAR + '\'' +
                        ", TEL='" + TEL + '\'' +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.NICKNAME);
                dest.writeString(this.SEX);
                dest.writeString(this.AVATAR);
                dest.writeString(this.TEL);
            }

            public SELFBean() {
            }

            protected SELFBean(Parcel in) {
                this.NICKNAME = in.readString();
                this.SEX = in.readString();
                this.AVATAR = in.readString();
                this.TEL = in.readString();
            }

            public static final Creator<SELFBean> CREATOR = new Creator<SELFBean>() {
                @Override
                public SELFBean createFromParcel(Parcel source) {
                    return new SELFBean(source);
                }

                @Override
                public SELFBean[] newArray(int size) {
                    return new SELFBean[size];
                }
            };
        }

        public static class FMBean implements Parcelable {
            /**
             * NICKNAME : suger
             * SEX : 0
             * AVATAR : www.smartesl.com.cn/tempimg/avatar/1070.jpg
             * TEL : 18037106850
             */
            public String NICKNAME;
            public String SEX;
            public String AVATAR;
            public String TEL;

            @Override
            public String toString() {
                return "FMBean{" +
                        "NICKNAME='" + NICKNAME + '\'' +
                        ", SEX='" + SEX + '\'' +
                        ", AVATAR='" + AVATAR + '\'' +
                        ", TEL='" + TEL + '\'' +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.NICKNAME);
                dest.writeString(this.SEX);
                dest.writeString(this.AVATAR);
                dest.writeString(this.TEL);
            }

            public FMBean() {
            }

            protected FMBean(Parcel in) {
                this.NICKNAME = in.readString();
                this.SEX = in.readString();
                this.AVATAR = in.readString();
                this.TEL = in.readString();
            }

            public static final Creator<FMBean> CREATOR = new Creator<FMBean>() {
                @Override
                public FMBean createFromParcel(Parcel source) {
                    return new FMBean(source);
                }

                @Override
                public FMBean[] newArray(int size) {
                    return new FMBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.ID);
            dest.writeString(this.MEMID);
            dest.writeString(this.FMID);
            dest.writeString(this.ADDDATE);
            dest.writeString(this.PASSFLAG);
            dest.writeString(this.PASSIDEA);
            dest.writeParcelable(this.SELF, flags);
            dest.writeParcelable(this.FM, flags);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.ID = in.readString();
            this.MEMID = in.readString();
            this.FMID = in.readString();
            this.ADDDATE = in.readString();
            this.PASSFLAG = in.readString();
            this.PASSIDEA = in.readString();
            this.SELF = in.readParcelable(SELFBean.class.getClassLoader());
            this.FM = in.readParcelable(FMBean.class.getClassLoader());
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
}
