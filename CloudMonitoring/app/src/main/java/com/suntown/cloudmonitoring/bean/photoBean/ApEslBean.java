package com.suntown.cloudmonitoring.bean.photoBean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/11/11.
 */

public class ApEslBean implements Parcelable {

    /**
     * eslWarn : {"ap":{"apaddr":"10","apid":2,"eslCount":0,"hwtype":0,"hwversion":"0","id":101,"ip":"124.160.216.75","lastDate":1462967478000,"lastDateFormat":"2016-05-11 19:51:18","port":"53727","shop":{"id":"571566667"},"sn":"m1f2A3744a4o","status":0,"swversion":"14","taskSendRecords":[]},"esl":{"activityDate":1456130596000,"ap":{"apaddr":"","apid":0,"eslCount":0,"hwtype":0,"hwversion":"","id":101,"ip":"","lastDateFormat":"","port":"","sn":"","status":0,"swversion":""},"apid_":"","bin":{"colNumber":0,"id":"57156666754","name":"","residualCnt":0,"rowNumber":0},"currentStatus":0,"eslTask":{"mapType":0,"taskid":"","tinyip":"0.0.5.234"},"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lastDate":1456130083000,"lqi":-73,"mac":"","powerDate":1456130596000,"poweroff":1,"rssi_fo":-57,"shop":{"id":"571566667"},"swversion":"13","totalLoginCount":0,"type":5,"voltage":3008,"voltageScope":""}}
     */

    public EslWarnBean eslWarn;

    @Override
    public String toString() {
        return "ApEslBean{" +
                "eslWarn=" + eslWarn +
                '}';
    }


    public static class EslWarnBean implements Parcelable {
        /**
         * ap : {"apaddr":"10","apid":2,"eslCount":0,"hwtype":0,"hwversion":"0","id":101,"ip":"124.160.216.75","lastDate":1462967478000,"lastDateFormat":"2016-05-11 19:51:18","port":"53727","shop":{"id":"571566667"},"sn":"m1f2A3744a4o","status":0,"swversion":"14","taskSendRecords":[]}
         * esl : {"activityDate":1456130596000,"ap":{"apaddr":"","apid":0,"eslCount":0,"hwtype":0,"hwversion":"","id":101,"ip":"","lastDateFormat":"","port":"","sn":"","status":0,"swversion":""},"apid_":"","bin":{"colNumber":0,"id":"57156666754","name":"","residualCnt":0,"rowNumber":0},"currentStatus":0,"eslTask":{"mapType":0,"taskid":"","tinyip":"0.0.5.234"},"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lastDate":1456130083000,"lqi":-73,"mac":"","powerDate":1456130596000,"poweroff":1,"rssi_fo":-57,"shop":{"id":"571566667"},"swversion":"13","totalLoginCount":0,"type":5,"voltage":3008,"voltageScope":""}
         */

        public ApBean ap;
        public EslBean esl;

        @Override
        public String toString() {
            return "EslWarnBean{" +
                    "ap=" + ap +
                    ", esl=" + esl +
                    '}';
        }


        public static class ApBean implements Parcelable {
            /**
             * apaddr : 10
             * apid : 2
             * eslCount : 0
             * hwtype : 0
             * hwversion : 0
             * id : 101
             * ip : 124.160.216.75
             * lastDate : 1462967478000
             * lastDateFormat : 2016-05-11 19:51:18
             * port : 53727
             * shop : {"id":"571566667"}
             * sn : m1f2A3744a4o
             * status : 0
             * swversion : 14
             * taskSendRecords : []
             */

            public String apaddr;
            public String apid;
            public String eslCount;
            public String hwtype;
            public String hwversion;
            public String id;
            public String ip;
            public String lastDate;
            public String lastDateFormat;
            public String port;
            public ShopBean shop;
            public String sn;
            public String status;
            public String swversion;
            public List<String> taskSendRecords;

            @Override
            public String toString() {
                return "ApBean{" +
                        "apaddr='" + apaddr + '\'' +
                        ", apid=" + apid +
                        ", eslCount=" + eslCount +
                        ", hwtype=" + hwtype +
                        ", hwversion='" + hwversion + '\'' +
                        ", id=" + id +
                        ", ip='" + ip + '\'' +
                        ", lastDate=" + lastDate +
                        ", lastDateFormat='" + lastDateFormat + '\'' +
                        ", port='" + port + '\'' +
                        ", shop=" + shop +
                        ", sn='" + sn + '\'' +
                        ", status=" + status +
                        ", swversion='" + swversion + '\'' +
                        ", taskSendRecords=" + taskSendRecords +
                        '}';
            }


            public static class ShopBean implements Parcelable {
                /**
                 * id : 571566667
                 */

                public String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.id);
                }

                public ShopBean() {
                }

                protected ShopBean(Parcel in) {
                    this.id = in.readString();
                }

                public static final Creator<ShopBean> CREATOR = new Creator<ShopBean>() {
                    @Override
                    public ShopBean createFromParcel(Parcel source) {
                        return new ShopBean(source);
                    }

                    @Override
                    public ShopBean[] newArray(int size) {
                        return new ShopBean[size];
                    }
                };
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.apaddr);
                dest.writeString(this.apid);
                dest.writeString(this.eslCount);
                dest.writeString(this.hwtype);
                dest.writeString(this.hwversion);
                dest.writeString(this.id);
                dest.writeString(this.ip);
                dest.writeString(this.lastDate);
                dest.writeString(this.lastDateFormat);
                dest.writeString(this.port);
                dest.writeParcelable(this.shop, flags);
                dest.writeString(this.sn);
                dest.writeString(this.status);
                dest.writeString(this.swversion);
                dest.writeStringList(this.taskSendRecords);
            }

            public ApBean() {
            }

            protected ApBean(Parcel in) {
                this.apaddr = in.readString();
                this.apid = in.readString();
                this.eslCount = in.readString();
                this.hwtype = in.readString();
                this.hwversion = in.readString();
                this.id = in.readString();
                this.ip = in.readString();
                this.lastDate = in.readString();
                this.lastDateFormat = in.readString();
                this.port = in.readString();
                this.shop = in.readParcelable(ShopBean.class.getClassLoader());
                this.sn = in.readString();
                this.status = in.readString();
                this.swversion = in.readString();
                this.taskSendRecords = in.createStringArrayList();
            }

            public static final Parcelable.Creator<ApBean> CREATOR = new Parcelable.Creator<ApBean>() {
                @Override
                public ApBean createFromParcel(Parcel source) {
                    return new ApBean(source);
                }

                @Override
                public ApBean[] newArray(int size) {
                    return new ApBean[size];
                }
            };
        }

        public static class EslBean implements Parcelable {
            /**
             * activityDate : 1456130596000
             * ap : {"apaddr":"","apid":0,"eslCount":0,"hwtype":0,"hwversion":"","id":101,"ip":"","lastDateFormat":"","port":"","sn":"","status":0,"swversion":""}
             * apid_ : 
             * bin : {"colNumber":0,"id":"57156666754","name":"","residualCnt":0,"rowNumber":0}
             * currentStatus : 0
             * eslTask : {"mapType":0,"taskid":"","tinyip":"0.0.5.234"}
             * eslTypeName : 
             * hwtype : 0
             * ip : 0.0.5.234
             * isLeader : 0
             * lastDate : 1456130083000
             * lqi : -73
             * mac : 
             * powerDate : 1456130596000
             * poweroff : 1
             * rssi_fo : -57
             * shop : {"id":"571566667"}
             * swversion : 13
             * totalLoginCount : 0
             * type : 5
             * voltage : 3008
             * voltageScope : 
             */

            public long activityDate;
            public ApBeanX ap;
            public String apid_;
            public BinBean bin;
            public String currentStatus;
            public EslTaskBean eslTask;
            public String eslTypeName;
            public String hwtype;
            public String ip;
            public String isLeader;
            public long lastDate;
            public String lqi;
            public String mac;
            public long powerDate;
            public int poweroff;
            public String rssi_fo;
            public ShopBeanX shop;
            public String swversion;
            public String totalLoginCount;
            public String type;
            public String voltage;
            public String voltageScope;

            @Override
            public String toString() {
                return "EslBean{" +
                        "activityDate=" + activityDate +
                        ", ap=" + ap +
                        ", apid_='" + apid_ + '\'' +
                        ", bin=" + bin +
                        ", currentStatus=" + currentStatus +
                        ", eslTask=" + eslTask +
                        ", eslTypeName='" + eslTypeName + '\'' +
                        ", hwtype=" + hwtype +
                        ", ip='" + ip + '\'' +
                        ", isLeader=" + isLeader +
                        ", lastDate=" + lastDate +
                        ", lqi=" + lqi +
                        ", mac='" + mac + '\'' +
                        ", powerDate=" + powerDate +
                        ", poweroff=" + poweroff +
                        ", rssi_fo=" + rssi_fo +
                        ", shop=" + shop +
                        ", swversion='" + swversion + '\'' +
                        ", totalLoginCount=" + totalLoginCount +
                        ", type=" + type +
                        ", voltage=" + voltage +
                        ", voltageScope='" + voltageScope + '\'' +
                        '}';
            }


            public static class ApBeanX implements Parcelable {
                /**
                 * apaddr : 
                 * apid : 0
                 * eslCount : 0
                 * hwtype : 0
                 * hwversion : 
                 * id : 101
                 * ip : 
                 * lastDateFormat : 
                 * port : 
                 * sn : 
                 * status : 0
                 * swversion : 
                 */

                public String apaddr;
                public int apid;
                public int eslCount;
                public int hwtype;
                public String hwversion;
                public int id;
                public String ip;
                public String lastDateFormat;
                public String port;
                public String sn;
                public int status;
                public String swversion;


                @Override
                public String toString() {
                    return "ApBeanX{" +
                            "apaddr='" + apaddr + '\'' +
                            ", apid=" + apid +
                            ", eslCount=" + eslCount +
                            ", hwtype=" + hwtype +
                            ", hwversion='" + hwversion + '\'' +
                            ", id=" + id +
                            ", ip='" + ip + '\'' +
                            ", lastDateFormat='" + lastDateFormat + '\'' +
                            ", port='" + port + '\'' +
                            ", sn='" + sn + '\'' +
                            ", status=" + status +
                            ", swversion='" + swversion + '\'' +
                            '}';
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.apaddr);
                    dest.writeInt(this.apid);
                    dest.writeInt(this.eslCount);
                    dest.writeInt(this.hwtype);
                    dest.writeString(this.hwversion);
                    dest.writeInt(this.id);
                    dest.writeString(this.ip);
                    dest.writeString(this.lastDateFormat);
                    dest.writeString(this.port);
                    dest.writeString(this.sn);
                    dest.writeInt(this.status);
                    dest.writeString(this.swversion);
                }

                public ApBeanX() {
                }

                protected ApBeanX(Parcel in) {
                    this.apaddr = in.readString();
                    this.apid = in.readInt();
                    this.eslCount = in.readInt();
                    this.hwtype = in.readInt();
                    this.hwversion = in.readString();
                    this.id = in.readInt();
                    this.ip = in.readString();
                    this.lastDateFormat = in.readString();
                    this.port = in.readString();
                    this.sn = in.readString();
                    this.status = in.readInt();
                    this.swversion = in.readString();
                }

                public static final Parcelable.Creator<ApBeanX> CREATOR = new Parcelable.Creator<ApBeanX>() {
                    @Override
                    public ApBeanX createFromParcel(Parcel source) {
                        return new ApBeanX(source);
                    }

                    @Override
                    public ApBeanX[] newArray(int size) {
                        return new ApBeanX[size];
                    }
                };
            }

            public static class BinBean implements Parcelable {
                /**
                 * colNumber : 0
                 * id : 57156666754
                 * name :
                 * residualCnt : 0
                 * rowNumber : 0
                 */

                public int colNumber;
                public String id;
                public String name;
                public int residualCnt;
                public int rowNumber;

                @Override
                public String toString() {
                    return "BinBean{" +
                            "colNumber=" + colNumber +
                            ", id='" + id + '\'' +
                            ", name='" + name + '\'' +
                            ", residualCnt=" + residualCnt +
                            ", rowNumber=" + rowNumber +
                            '}';
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.colNumber);
                    dest.writeString(this.id);
                    dest.writeString(this.name);
                    dest.writeInt(this.residualCnt);
                    dest.writeInt(this.rowNumber);
                }

                public BinBean() {
                }

                protected BinBean(Parcel in) {
                    this.colNumber = in.readInt();
                    this.id = in.readString();
                    this.name = in.readString();
                    this.residualCnt = in.readInt();
                    this.rowNumber = in.readInt();
                }

                public static final Parcelable.Creator<BinBean> CREATOR = new Parcelable.Creator<BinBean>() {
                    @Override
                    public BinBean createFromParcel(Parcel source) {
                        return new BinBean(source);
                    }

                    @Override
                    public BinBean[] newArray(int size) {
                        return new BinBean[size];
                    }
                };
            }

            public static class EslTaskBean implements Parcelable {
                /**
                 * mapType : 0
                 * taskid : 
                 * tinyip : 0.0.5.234
                 */

                public int mapType;
                public String taskid;
                public String tinyip;


                @Override
                public String toString() {
                    return "EslTaskBean{" +
                            "mapType=" + mapType +
                            ", taskid='" + taskid + '\'' +
                            ", tinyip='" + tinyip + '\'' +
                            '}';
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeInt(this.mapType);
                    dest.writeString(this.taskid);
                    dest.writeString(this.tinyip);
                }

                public EslTaskBean() {
                }

                protected EslTaskBean(Parcel in) {
                    this.mapType = in.readInt();
                    this.taskid = in.readString();
                    this.tinyip = in.readString();
                }

                public static final Parcelable.Creator<EslTaskBean> CREATOR = new Parcelable.Creator<EslTaskBean>() {
                    @Override
                    public EslTaskBean createFromParcel(Parcel source) {
                        return new EslTaskBean(source);
                    }

                    @Override
                    public EslTaskBean[] newArray(int size) {
                        return new EslTaskBean[size];
                    }
                };
            }

            public static class ShopBeanX implements Parcelable {
                /**
                 * id : 571566667
                 */

                public String id;


                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.id);
                }

                public ShopBeanX() {
                }

                protected ShopBeanX(Parcel in) {
                    this.id = in.readString();
                }

                public static final Parcelable.Creator<ShopBeanX> CREATOR = new Parcelable.Creator<ShopBeanX>() {
                    @Override
                    public ShopBeanX createFromParcel(Parcel source) {
                        return new ShopBeanX(source);
                    }

                    @Override
                    public ShopBeanX[] newArray(int size) {
                        return new ShopBeanX[size];
                    }
                };

                @Override
                public String toString() {
                    return "ShopBeanX{" +
                            "id='" + id + '\'' +
                            '}';
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.activityDate);
                dest.writeParcelable(this.ap, flags);
                dest.writeString(this.apid_);
                dest.writeParcelable(this.bin, flags);
                dest.writeString(this.currentStatus);
                dest.writeParcelable(this.eslTask, flags);
                dest.writeString(this.eslTypeName);
                dest.writeString(this.hwtype);
                dest.writeString(this.ip);
                dest.writeString(this.isLeader);
                dest.writeLong(this.lastDate);
                dest.writeString(this.lqi);
                dest.writeString(this.mac);
                dest.writeLong(this.powerDate);
                dest.writeInt(this.poweroff);
                dest.writeString(this.rssi_fo);
                dest.writeParcelable(this.shop, flags);
                dest.writeString(this.swversion);
                dest.writeString(this.totalLoginCount);
                dest.writeString(this.type);
                dest.writeString(this.voltage);
                dest.writeString(this.voltageScope);
            }

            public EslBean() {
            }

            protected EslBean(Parcel in) {
                this.activityDate = in.readLong();
                this.ap = in.readParcelable(ApBeanX.class.getClassLoader());
                this.apid_ = in.readString();
                this.bin = in.readParcelable(BinBean.class.getClassLoader());
                this.currentStatus = in.readString();
                this.eslTask = in.readParcelable(EslTaskBean.class.getClassLoader());
                this.eslTypeName = in.readString();
                this.hwtype = in.readString();
                this.ip = in.readString();
                this.isLeader = in.readString();
                this.lastDate = in.readLong();
                this.lqi = in.readString();
                this.mac = in.readString();
                this.powerDate = in.readLong();
                this.poweroff = in.readInt();
                this.rssi_fo = in.readString();
                this.shop = in.readParcelable(ShopBeanX.class.getClassLoader());
                this.swversion = in.readString();
                this.totalLoginCount = in.readString();
                this.type = in.readString();
                this.voltage = in.readString();
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
            dest.writeParcelable(this.ap, flags);
            dest.writeParcelable(this.esl, flags);
        }

        public EslWarnBean() {
        }

        protected EslWarnBean(Parcel in) {
            this.ap = in.readParcelable(ApBean.class.getClassLoader());
            this.esl = in.readParcelable(EslBean.class.getClassLoader());
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

    public ApEslBean() {
    }

    protected ApEslBean(Parcel in) {
        this.eslWarn = in.readParcelable(EslWarnBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ApEslBean> CREATOR = new Parcelable.Creator<ApEslBean>() {
        @Override
        public ApEslBean createFromParcel(Parcel source) {
            return new ApEslBean(source);
        }

        @Override
        public ApEslBean[] newArray(int size) {
            return new ApEslBean[size];
        }
    };
}
