package com.suntown.cloudmonitoring.bean.photoBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/11.
 */

public class ModelFlagBean implements Parcelable {

    /**
     * eslWarn : {"eslModel":{"dm_endtime":"23:00","dm_starttime":"6:00","dx":0,"dy":0,"memprice":0,"oriprice":16,"r_dmCode":375,"r_nowDate":1477324800000,"r_startDate":1477324800000,"r_type":0,"rw_dx":0,"rw_dy":0,"rw_x":0,"rw_y":0,"sid":"571566667","specheight":128,"specwidth":296,"taskId":"1141809","uptprice":16,"x":0,"y":0}}
     */

    public EslWarnBean eslWarn;


    public static class EslWarnBean implements Parcelable {
        /**
         * eslModel : {"dm_endtime":"23:00","dm_starttime":"6:00","dx":0,"dy":0,"memprice":0,"oriprice":16,"r_dmCode":375,"r_nowDate":1477324800000,"r_startDate":1477324800000,"r_type":0,"rw_dx":0,"rw_dy":0,"rw_x":0,"rw_y":0,"sid":"571566667","specheight":128,"specwidth":296,"taskId":"1141809","uptprice":16,"x":0,"y":0}
         */

        public EslModelBean eslModel;

        @Override
        public String toString() {
            return "EslWarnBean{" +
                    "eslModel=" + eslModel +
                    '}';
        }


        public static class EslModelBean implements Parcelable {
            /**
             * dm_endtime : 23:00
             * dm_starttime : 6:00
             * dx : 0
             * dy : 0
             * memprice : 0
             * oriprice : 16
             * r_dmCode : 375
             * r_nowDate : 1477324800000
             * r_startDate : 1477324800000
             * r_type : 0
             * rw_dx : 0
             * rw_dy : 0
             * rw_x : 0
             * rw_y : 0
             * sid : 571566667
             * specheight : 128
             * specwidth : 296
             * taskId : 1141809
             * uptprice : 16
             * x : 0
             * y : 0
             */

            public String dm_endtime;
            public String dm_starttime;
            public int dx;
            public int dy;
            public String memprice;
            public String oriprice;
            public int r_dmCode;
            public long r_nowDate;
            public long r_startDate;
            public int r_type;
            public int rw_dx;
            public int rw_dy;
            public int rw_x;
            public int rw_y;
            public String sid;
            public int specheight;
            public int specwidth;
            public String taskId;
            public String uptprice;
            public int x;
            public int y;


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.dm_endtime);
                dest.writeString(this.dm_starttime);
                dest.writeInt(this.dx);
                dest.writeInt(this.dy);
                dest.writeString(this.memprice);
                dest.writeString(this.oriprice);
                dest.writeInt(this.r_dmCode);
                dest.writeLong(this.r_nowDate);
                dest.writeLong(this.r_startDate);
                dest.writeInt(this.r_type);
                dest.writeInt(this.rw_dx);
                dest.writeInt(this.rw_dy);
                dest.writeInt(this.rw_x);
                dest.writeInt(this.rw_y);
                dest.writeString(this.sid);
                dest.writeInt(this.specheight);
                dest.writeInt(this.specwidth);
                dest.writeString(this.taskId);
                dest.writeString(this.uptprice);
                dest.writeInt(this.x);
                dest.writeInt(this.y);
            }

            public EslModelBean() {
            }

            protected EslModelBean(Parcel in) {
                this.dm_endtime = in.readString();
                this.dm_starttime = in.readString();
                this.dx = in.readInt();
                this.dy = in.readInt();
                this.memprice = in.readString();
                this.oriprice = in.readString();
                this.r_dmCode = in.readInt();
                this.r_nowDate = in.readLong();
                this.r_startDate = in.readLong();
                this.r_type = in.readInt();
                this.rw_dx = in.readInt();
                this.rw_dy = in.readInt();
                this.rw_x = in.readInt();
                this.rw_y = in.readInt();
                this.sid = in.readString();
                this.specheight = in.readInt();
                this.specwidth = in.readInt();
                this.taskId = in.readString();
                this.uptprice = in.readString();
                this.x = in.readInt();
                this.y = in.readInt();
            }

            public static final Parcelable.Creator<EslModelBean> CREATOR = new Parcelable.Creator<EslModelBean>() {
                @Override
                public EslModelBean createFromParcel(Parcel source) {
                    return new EslModelBean(source);
                }

                @Override
                public EslModelBean[] newArray(int size) {
                    return new EslModelBean[size];
                }
            };

            @Override
            public String toString() {
                return "EslModelBean{" +
                        "dm_endtime='" + dm_endtime + '\'' +
                        ", dm_starttime='" + dm_starttime + '\'' +
                        ", dx=" + dx +
                        ", dy=" + dy +
                        ", memprice=" + memprice +
                        ", oriprice=" + oriprice +
                        ", r_dmCode=" + r_dmCode +
                        ", r_nowDate=" + r_nowDate +
                        ", r_startDate=" + r_startDate +
                        ", r_type=" + r_type +
                        ", rw_dx=" + rw_dx +
                        ", rw_dy=" + rw_dy +
                        ", rw_x=" + rw_x +
                        ", rw_y=" + rw_y +
                        ", sid='" + sid + '\'' +
                        ", specheight=" + specheight +
                        ", specwidth=" + specwidth +
                        ", taskId='" + taskId + '\'' +
                        ", uptprice=" + uptprice +
                        ", x=" + x +
                        ", y=" + y +
                        '}';
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.eslModel, flags);
        }

        public EslWarnBean() {
        }

        protected EslWarnBean(Parcel in) {
            this.eslModel = in.readParcelable(EslModelBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<EslWarnBean> CREATOR = new Parcelable.Creator<EslWarnBean>() {
            @Override
            public EslWarnBean createFromParcel(Parcel source) {
                return new EslWarnBean(source);
            }

            @Override
            public EslWarnBean[] newArray(int size) {
                return new EslWarnBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.eslWarn, flags);
    }

    public ModelFlagBean() {
    }

    protected ModelFlagBean(Parcel in) {
        this.eslWarn = in.readParcelable(EslWarnBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ModelFlagBean> CREATOR = new Parcelable.Creator<ModelFlagBean>() {
        @Override
        public ModelFlagBean createFromParcel(Parcel source) {
            return new ModelFlagBean(source);
        }

        @Override
        public ModelFlagBean[] newArray(int size) {
            return new ModelFlagBean[size];
        }
    };
}
