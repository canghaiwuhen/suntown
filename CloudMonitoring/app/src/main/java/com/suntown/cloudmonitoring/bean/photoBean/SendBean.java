package com.suntown.cloudmonitoring.bean.photoBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/11.
 */

public class SendBean implements Parcelable {
    /**
     * eslSendTask : {"esl":{"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""},"sendtime":1450251681000,"taskid":1141809,"tasktype":0,"uptstate":"-1","upttime":1450251781000}
     */

    public EslSendTaskBean eslSendTask;


    public static class EslSendTaskBean implements Parcelable {
        /**
         * esl : {"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""}
         * sendtime : 1450251681000
         * taskid : 1141809
         * tasktype : 0
         * uptstate : -1
         * upttime : 1450251781000
         */

        public EslBean esl;
        public long sendtime;
        public int taskid;
        public int tasktype;
        public String uptstate;
        public long upttime;

        @Override
        public String toString() {
            return "EslSendTaskBean{" +
                    "esl=" + esl +
                    ", sendtime=" + sendtime +
                    ", taskid=" + taskid +
                    ", tasktype=" + tasktype +
                    ", uptstate='" + uptstate + '\'' +
                    ", upttime=" + upttime +
                    '}';
        }

        public static class EslBean implements Parcelable {
            /**
             * apid_ : 
             * currentStatus : 0
             * eslTypeName : 
             * hwtype : 0
             * ip : 0.0.5.234
             * isLeader : 0
             * lqi : 0
             * mac : 
             * poweroff : 0
             * rssi_fo : 0
             * swversion : 
             * totalLoginCount : 0
             * type : 0
             * voltage : 0
             * voltageScope : 
             */

            public String apid_;
            public int currentStatus;
            public String eslTypeName;
            public int hwtype;
            public String ip;
            public int isLeader;
            public int lqi;
            public String mac;
            public int poweroff;
            public int rssi_fo;
            public String swversion;
            public int totalLoginCount;
            public int type;
            public int voltage;
            public String voltageScope;

            @Override
            public String toString() {
                return "EslBean{" +
                        "apid_='" + apid_ + '\'' +
                        ", currentStatus=" + currentStatus +
                        ", eslTypeName='" + eslTypeName + '\'' +
                        ", hwtype=" + hwtype +
                        ", ip='" + ip + '\'' +
                        ", isLeader=" + isLeader +
                        ", lqi=" + lqi +
                        ", mac='" + mac + '\'' +
                        ", poweroff=" + poweroff +
                        ", rssi_fo=" + rssi_fo +
                        ", swversion='" + swversion + '\'' +
                        ", totalLoginCount=" + totalLoginCount +
                        ", type=" + type +
                        ", voltage=" + voltage +
                        ", voltageScope='" + voltageScope + '\'' +
                        '}';
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.apid_);
                dest.writeInt(this.currentStatus);
                dest.writeString(this.eslTypeName);
                dest.writeInt(this.hwtype);
                dest.writeString(this.ip);
                dest.writeInt(this.isLeader);
                dest.writeInt(this.lqi);
                dest.writeString(this.mac);
                dest.writeInt(this.poweroff);
                dest.writeInt(this.rssi_fo);
                dest.writeString(this.swversion);
                dest.writeInt(this.totalLoginCount);
                dest.writeInt(this.type);
                dest.writeInt(this.voltage);
                dest.writeString(this.voltageScope);
            }

            public EslBean() {
            }

            protected EslBean(Parcel in) {
                this.apid_ = in.readString();
                this.currentStatus = in.readInt();
                this.eslTypeName = in.readString();
                this.hwtype = in.readInt();
                this.ip = in.readString();
                this.isLeader = in.readInt();
                this.lqi = in.readInt();
                this.mac = in.readString();
                this.poweroff = in.readInt();
                this.rssi_fo = in.readInt();
                this.swversion = in.readString();
                this.totalLoginCount = in.readInt();
                this.type = in.readInt();
                this.voltage = in.readInt();
                this.voltageScope = in.readString();
            }

            public static final Parcelable.Creator<EslBean> CREATOR = new Parcelable.Creator<EslBean>() {
                @Override
                public EslBean createFromParcel(Parcel source) {
                    return new EslBean(source);
                }

                @Override
                public EslBean[] newArray(int size) {
                    return new EslBean[size];
                }
            };
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.esl, flags);
            dest.writeLong(this.sendtime);
            dest.writeInt(this.taskid);
            dest.writeInt(this.tasktype);
            dest.writeString(this.uptstate);
            dest.writeLong(this.upttime);
        }

        public EslSendTaskBean() {
        }

        protected EslSendTaskBean(Parcel in) {
            this.esl = in.readParcelable(EslBean.class.getClassLoader());
            this.sendtime = in.readLong();
            this.taskid = in.readInt();
            this.tasktype = in.readInt();
            this.uptstate = in.readString();
            this.upttime = in.readLong();
        }

        public static final Parcelable.Creator<EslSendTaskBean> CREATOR = new Parcelable.Creator<EslSendTaskBean>() {
            @Override
            public EslSendTaskBean createFromParcel(Parcel source) {
                return new EslSendTaskBean(source);
            }

            @Override
            public EslSendTaskBean[] newArray(int size) {
                return new EslSendTaskBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.eslSendTask, flags);
    }

    public SendBean() {
    }

    protected SendBean(Parcel in) {
        this.eslSendTask = in.readParcelable(EslSendTaskBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<SendBean> CREATOR = new Parcelable.Creator<SendBean>() {
        @Override
        public SendBean createFromParcel(Parcel source) {
            return new SendBean(source);
        }

        @Override
        public SendBean[] newArray(int size) {
            return new SendBean[size];
        }
    };
}
