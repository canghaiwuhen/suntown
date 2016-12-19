package com.suntown.cloudmonitoring.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */

public class TagPhotoBean {

    /**
     * ap : {"apaddr":"10","apid":2,"eslCount":0,"hwtype":0,"hwversion":"0","id":101,"ip":"124.160.216.75","lastDate":1462967478000,"lastDateFormat":"2016-05-11 19:51:18","port":"53727","shop":{"id":"571566667"},"sn":"m1f2A3744a4o","status":0,"swversion":"14","taskSendRecords":[]}
     * bindFlag : true
     * esl : {"activityDate":1456130596000,"ap":{"apaddr":"","apid":0,"eslCount":0,"hwtype":0,"hwversion":"","id":101,"ip":"","lastDateFormat":"","port":"","sn":"","status":0,"swversion":""},"apid_":"","bin":{"colNumber":0,"id":"57156666754","name":"","residualCnt":0,"rowNumber":0},"currentStatus":0,"eslTask":{"mapType":0,"taskid":"","tinyip":"0.0.5.234"},"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lastDate":1456130083000,"lqi":-73,"mac":"","powerDate":1456130596000,"poweroff":1,"rssi_fo":-57,"shop":{"id":"571566667"},"swversion":"13","totalLoginCount":0,"type":5,"voltage":3008,"voltageScope":""}
     * eslModel : {"dm_endtime":"23:00","dm_starttime":"6:00","dx":0,"dy":0,"memprice":0,"oriprice":16,"r_dmCode":375,"r_nowDate":1477324800000,"r_startDate":1477324800000,"r_type":0,"rw_dx":0,"rw_dy":0,"rw_x":0,"rw_y":0,"sid":"571566667","specheight":128,"specwidth":296,"taskId":"1141809","uptprice":16,"x":0,"y":0}
     * eslSendTask : {"esl":{"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""},"sendtime":1450251681000,"taskid":1141809,"tasktype":0,"uptstate":"-1","upttime":1450251781000}
     * eslUptTasks : {"adddate":1450251660000,"esl":{"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"shop":{"id":"571566667","name":"仁盈天亿大厦"},"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""},"goods":{"barcode":"33","bitmapid":0,"brand":"","costPrice":0,"curPrice":0,"gclass":"","gcode":"","gname":"","isStop":0,"kind":"","lastUpdatePerson":"","origin":"","pknumber":0,"provider":"","remarks":"","spec":"","storeNum":0,"storeNumCk":0,"unit":""},"memprice":0,"oriprice":16,"partMap":0,"priceType":0,"pricetype":0,"taskid":1141809,"uptprice":16}
     * goods : {"barcode":"33","bitmapid":0,"brand":"","costPrice":0,"curPrice":0,"gclass":"","gcode":"33","gname":"得益自热方便米饭（红烧牛肉）","goodsprice":{"costprice":16,"curprice":16,"gcode":"33","memPrice":0,"vipPrice":0},"isStop":0,"kind":"","lastUpdatePerson":"","origin":"","pknumber":0,"provider":"","remarks":"","shop":{"id":"571566667"},"spec":"","storeNum":0,"storeNumCk":0,"unit":""}
     * loginFlag : true
     * mapTypeFlag : true
     * modelFlag : true
     * oriMaptype : 0
     * sendFlag : false
     * shelfGoods : {"aid":"57156666754","barcode":"33","gclass":"","gname":"得益自热方便米饭（红烧牛肉）","origin":"","sid":"571566667","sname":"仁盈天亿大厦","spec":"","tinyip":"0.0.5.234"}
     * successSend : false
     * taskFlag : true
     */

    public EslWarnBean eslWarn;

    @Override
    public String toString() {
        return "TagPhotoBean{" +
                "eslWarn=" + eslWarn +
                '}';
    }


    public static class EslWarnBean {
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

        public ApBean ap;
        public boolean bindFlag;
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

        public EslBean esl;
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

        public EslModelBean eslModel;
        /**
         * esl : {"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""}
         * sendtime : 1450251681000
         * taskid : 1141809
         * tasktype : 0
         * uptstate : -1
         * upttime : 1450251781000
         */

        public EslSendTaskBean eslSendTask;
        /**
         * adddate : 1450251660000
         * esl : {"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.5.234","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"shop":{"id":"571566667","name":"仁盈天亿大厦"},"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""}
         * goods : {"barcode":"33","bitmapid":0,"brand":"","costPrice":0,"curPrice":0,"gclass":"","gcode":"","gname":"","isStop":0,"kind":"","lastUpdatePerson":"","origin":"","pknumber":0,"provider":"","remarks":"","spec":"","storeNum":0,"storeNumCk":0,"unit":""}
         * memprice : 0
         * oriprice : 16
         * partMap : 0
         * priceType : 0
         * pricetype : 0
         * taskid : 1141809
         * uptprice : 16
         */

        public EslSendTaskBean.EslUptTasksBean eslUptTasks;
        /**
         * barcode : 33
         * bitmapid : 0
         * brand : 
         * costPrice : 0
         * curPrice : 0
         * gclass : 
         * gcode : 33
         * gname : 得益自热方便米饭（红烧牛肉）
         * goodsprice : {"costprice":16,"curprice":16,"gcode":"33","memPrice":0,"vipPrice":0}
         * isStop : 0
         * kind : 
         * lastUpdatePerson : 
         * origin : 
         * pknumber : 0
         * provider : 
         * remarks : 
         * shop : {"id":"571566667"}
         * spec : 
         * storeNum : 0
         * storeNumCk : 0
         * unit : 
         */

        public EslSendTaskBean.EslUptTasksBean.GoodsBean goods;
        public boolean loginFlag;
        public boolean mapTypeFlag;
        public boolean modelFlag;
        public int oriMaptype;
        public boolean sendFlag;
        /**
         * aid : 57156666754
         * barcode : 33
         * gclass : 
         * gname : 得益自热方便米饭（红烧牛肉）
         * origin : 
         * sid : 571566667
         * sname : 仁盈天亿大厦
         * spec : 
         * tinyip : 0.0.5.234
         */

        public EslSendTaskBean.ShelfGoodsBean shelfGoods;
        public boolean successSend;
        public boolean taskFlag;

        @Override
        public String toString() {
            return "EslWarnBean{" +
                    "ap=" + ap +
                    ", bindFlag=" + bindFlag +
                    ", esl=" + esl +
                    ", eslModel=" + eslModel +
                    ", eslSendTask=" + eslSendTask +
                    ", eslUptTasks=" + eslUptTasks +
                    ", goods=" + goods +
                    ", loginFlag=" + loginFlag +
                    ", mapTypeFlag=" + mapTypeFlag +
                    ", modelFlag=" + modelFlag +
                    ", oriMaptype=" + oriMaptype +
                    ", sendFlag=" + sendFlag +
                    ", shelfGoods=" + shelfGoods +
                    ", successSend=" + successSend +
                    ", taskFlag=" + taskFlag +
                    '}';
        }

        public static class ApBean implements Parcelable {
            public String apaddr;
            public int apid;
            public int eslCount;
            public int hwtype;
            public String hwversion;
            public int id;
            public String ip;
            public long lastDate;
            public String lastDateFormat;
            public String port;
            /**
             * id : 571566667
             */

            public ShopBean shop;
            public String sn;
            public int status;
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

            public static class ShopBean {
                public String id;
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
                dest.writeLong(this.lastDate);
                dest.writeString(this.lastDateFormat);
                dest.writeString(this.port);
                dest.writeParcelable((Parcelable) this.shop, flags);
                dest.writeString(this.sn);
                dest.writeInt(this.status);
                dest.writeString(this.swversion);
                dest.writeStringList(this.taskSendRecords);
            }

            public ApBean() {
            }

            protected ApBean(Parcel in) {
                this.apaddr = in.readString();
                this.apid = in.readInt();
                this.eslCount = in.readInt();
                this.hwtype = in.readInt();
                this.hwversion = in.readString();
                this.id = in.readInt();
                this.ip = in.readString();
                this.lastDate = in.readLong();
                this.lastDateFormat = in.readString();
                this.port = in.readString();
                this.shop = in.readParcelable(ShopBean.class.getClassLoader());
                this.sn = in.readString();
                this.status = in.readInt();
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
            public long activityDate;
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

            public ApBean ap;
            public String apid_;
            /**
             * colNumber : 0
             * id : 57156666754
             * name : 
             * residualCnt : 0
             * rowNumber : 0
             */

            public BinBean bin;
            public int currentStatus;
            /**
             * mapType : 0
             * taskid : 
             * tinyip : 0.0.5.234
             */

            public EslTaskBean eslTask;
            public String eslTypeName;
            public int hwtype;
            public String ip;
            public int isLeader;
            public long lastDate;
            public int lqi;
            public String mac;
            public long powerDate;
            public int poweroff;
            public int rssi_fo;
            /**
             * id : 571566667
             */

            public ShopBean shop;
            public String swversion;
            public int totalLoginCount;
            public int type;
            public int voltage;
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



            public static class BinBean {
                public int colNumber;
                public String id;
                public String name;
                public int residualCnt;
                public int rowNumber;
            }

            public static class EslTaskBean {
                public int mapType;
                public String taskid;
                public String tinyip;
            }

            public class ShopBean{
                public String id;

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
                dest.writeParcelable((Parcelable) this.bin, flags);
                dest.writeInt(this.currentStatus);
                dest.writeParcelable((Parcelable) this.eslTask, flags);
                dest.writeString(this.eslTypeName);
                dest.writeInt(this.hwtype);
                dest.writeString(this.ip);
                dest.writeInt(this.isLeader);
                dest.writeLong(this.lastDate);
                dest.writeInt(this.lqi);
                dest.writeString(this.mac);
                dest.writeLong(this.powerDate);
                dest.writeInt(this.poweroff);
                dest.writeInt(this.rssi_fo);
                dest.writeParcelable((Parcelable) this.shop, flags);
                dest.writeString(this.swversion);
                dest.writeInt(this.totalLoginCount);
                dest.writeInt(this.type);
                dest.writeInt(this.voltage);
                dest.writeString(this.voltageScope);
            }

            public EslBean() {
            }

            protected EslBean(Parcel in) {
                this.activityDate = in.readLong();
                this.ap = in.readParcelable(ApBean.class.getClassLoader());
                this.apid_ = in.readString();
                this.bin = in.readParcelable(BinBean.class.getClassLoader());
                this.currentStatus = in.readInt();
                this.eslTask = in.readParcelable(EslTaskBean.class.getClassLoader());
                this.eslTypeName = in.readString();
                this.hwtype = in.readInt();
                this.ip = in.readString();
                this.isLeader = in.readInt();
                this.lastDate = in.readLong();
                this.lqi = in.readInt();
                this.mac = in.readString();
                this.powerDate = in.readLong();
                this.poweroff = in.readInt();
                this.rssi_fo = in.readInt();
                this.shop = in.readParcelable(ShopBean.class.getClassLoader());
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

        public static class EslModelBean implements Parcelable {
            public String dm_endtime;
            public String dm_starttime;
            public int dx;
            public int dy;
            public int memprice;
            public int oriprice;
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
            public int uptprice;
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
                dest.writeInt(this.memprice);
                dest.writeInt(this.oriprice);
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
                dest.writeInt(this.uptprice);
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
                this.memprice = in.readInt();
                this.oriprice = in.readInt();
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
                this.uptprice = in.readInt();
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

        public static class EslSendTaskBean {
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

            public static class EslBean {
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
            }

        public static class EslUptTasksBean implements Parcelable {
            public long adddate;
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
             * shop : {"id":"571566667","name":"仁盈天亿大厦"}
             * swversion : 
             * totalLoginCount : 0
             * type : 0
             * voltage : 0
             * voltageScope : 
             */

            public EslBean esl;
            /**
             * barcode : 33
             * bitmapid : 0
             * brand : 
             * costPrice : 0
             * curPrice : 0
             * gclass : 
             * gcode : 
             * gname : 
             * isStop : 0
             * kind : 
             * lastUpdatePerson : 
             * origin : 
             * pknumber : 0
             * provider : 
             * remarks : 
             * spec : 
             * storeNum : 0
             * storeNumCk : 0
             * unit : 
             */

            public GoodsBean goods;
            public int memprice;
            public int oriprice;
            public int partMap;
            public int priceType;
            public int pricetype;
            public int taskid;
            public int uptprice;

            @Override
            public String toString() {
                return "EslUptTasksBean{" +
                        "adddate=" + adddate +
                        ", esl=" + esl +
                        ", goods=" + goods +
                        ", memprice=" + memprice +
                        ", oriprice=" + oriprice +
                        ", partMap=" + partMap +
                        ", priceType=" + priceType +
                        ", pricetype=" + pricetype +
                        ", taskid=" + taskid +
                        ", uptprice=" + uptprice +
                        '}';
            }


            public static class EslBean {
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
                /**
                 * id : 571566667
                 * name : 仁盈天亿大厦
                 */

                public ShopBean shop;
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
                            ", shop=" + shop +
                            ", swversion='" + swversion + '\'' +
                            ", totalLoginCount=" + totalLoginCount +
                            ", type=" + type +
                            ", voltage=" + voltage +
                            ", voltageScope='" + voltageScope + '\'' +
                            '}';
                }


                public static class ShopBean {
                    public String id;
                    public String name;

                    @Override
                    public String toString() {
                        return "ShopBean{" +
                                "id='" + id + '\'' +
                                ", name='" + name + '\'' +
                                '}';
                    }
                }
            }

            public static class GoodsBean implements Parcelable {
                public String barcode;
                public int bitmapid;
                public String brand;
                public int costPrice;
                public int curPrice;
                public String gclass;
                public String gcode;
                public String gname;
                public int isStop;
                public String kind;
                public String lastUpdatePerson;
                public String origin;
                public int pknumber;
                public String provider;
                public String remarks;
                public String spec;
                public int storeNum;
                public int storeNumCk;
                public String unit;

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(this.barcode);
                    dest.writeInt(this.bitmapid);
                    dest.writeString(this.brand);
                    dest.writeInt(this.costPrice);
                    dest.writeInt(this.curPrice);
                    dest.writeString(this.gclass);
                    dest.writeString(this.gcode);
                    dest.writeString(this.gname);
                    dest.writeInt(this.isStop);
                    dest.writeString(this.kind);
                    dest.writeString(this.lastUpdatePerson);
                    dest.writeString(this.origin);
                    dest.writeInt(this.pknumber);
                    dest.writeString(this.provider);
                    dest.writeString(this.remarks);
                    dest.writeString(this.spec);
                    dest.writeInt(this.storeNum);
                    dest.writeInt(this.storeNumCk);
                    dest.writeString(this.unit);
                }

                public GoodsBean() {
                }

                protected GoodsBean(Parcel in) {
                    this.barcode = in.readString();
                    this.bitmapid = in.readInt();
                    this.brand = in.readString();
                    this.costPrice = in.readInt();
                    this.curPrice = in.readInt();
                    this.gclass = in.readString();
                    this.gcode = in.readString();
                    this.gname = in.readString();
                    this.isStop = in.readInt();
                    this.kind = in.readString();
                    this.lastUpdatePerson = in.readString();
                    this.origin = in.readString();
                    this.pknumber = in.readInt();
                    this.provider = in.readString();
                    this.remarks = in.readString();
                    this.spec = in.readString();
                    this.storeNum = in.readInt();
                    this.storeNumCk = in.readInt();
                    this.unit = in.readString();
                }

                public static final Parcelable.Creator<GoodsBean> CREATOR = new Parcelable.Creator<GoodsBean>() {
                    @Override
                    public GoodsBean createFromParcel(Parcel source) {
                        return new GoodsBean(source);
                    }

                    @Override
                    public GoodsBean[] newArray(int size) {
                        return new GoodsBean[size];
                    }
                };

                @Override
                public String toString() {
                    return "GoodsBean{" +
                            "barcode='" + barcode + '\'' +
                            ", bitmapid=" + bitmapid +
                            ", brand='" + brand + '\'' +
                            ", costPrice=" + costPrice +
                            ", curPrice=" + curPrice +
                            ", gclass='" + gclass + '\'' +
                            ", gcode='" + gcode + '\'' +
                            ", gname='" + gname + '\'' +
                            ", isStop=" + isStop +
                            ", kind='" + kind + '\'' +
                            ", lastUpdatePerson='" + lastUpdatePerson + '\'' +
                            ", origin='" + origin + '\'' +
                            ", pknumber=" + pknumber +
                            ", provider='" + provider + '\'' +
                            ", remarks='" + remarks + '\'' +
                            ", spec='" + spec + '\'' +
                            ", storeNum=" + storeNum +
                            ", storeNumCk=" + storeNumCk +
                            ", unit='" + unit + '\'' +
                            '}';
                }
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(this.adddate);
                dest.writeParcelable((Parcelable) this.esl, flags);
                dest.writeParcelable(this.goods, flags);
                dest.writeInt(this.memprice);
                dest.writeInt(this.oriprice);
                dest.writeInt(this.partMap);
                dest.writeInt(this.priceType);
                dest.writeInt(this.pricetype);
                dest.writeInt(this.taskid);
                dest.writeInt(this.uptprice);
            }

            public EslUptTasksBean() {
            }

            protected EslUptTasksBean(Parcel in) {
                this.adddate = in.readLong();
                this.esl = in.readParcelable(EslBean.class.getClassLoader());
                this.goods = in.readParcelable(GoodsBean.class.getClassLoader());
                this.memprice = in.readInt();
                this.oriprice = in.readInt();
                this.partMap = in.readInt();
                this.priceType = in.readInt();
                this.pricetype = in.readInt();
                this.taskid = in.readInt();
                this.uptprice = in.readInt();
            }

            public static final Parcelable.Creator<EslUptTasksBean> CREATOR = new Parcelable.Creator<EslUptTasksBean>() {
                @Override
                public EslUptTasksBean createFromParcel(Parcel source) {
                    return new EslUptTasksBean(source);
                }

                @Override
                public EslUptTasksBean[] newArray(int size) {
                    return new EslUptTasksBean[size];
                }
            };
        }

        public static class GoodsBean {
            public String barcode;
            public int bitmapid;
            public String brand;
            public int costPrice;
            public int curPrice;
            public String gclass;
            public String gcode;
            public String gname;
            /**
             * costprice : 16
             * curprice : 16
             * gcode : 33
             * memPrice : 0
             * vipPrice : 0
             */

            public GoodspriceBean goodsprice;
            public int isStop;
            public String kind;
            public String lastUpdatePerson;
            public String origin;
            public int pknumber;
            public String provider;
            public String remarks;
            /**
             * id : 571566667
             */

            public ShopBean shop;
            public String spec;
            public int storeNum;
            public int storeNumCk;
            public String unit;

            @Override
            public String toString() {
                return "GoodsBean{" +
                        "barcode='" + barcode + '\'' +
                        ", bitmapid=" + bitmapid +
                        ", brand='" + brand + '\'' +
                        ", costPrice=" + costPrice +
                        ", curPrice=" + curPrice +
                        ", gclass='" + gclass + '\'' +
                        ", gcode='" + gcode + '\'' +
                        ", gname='" + gname + '\'' +
                        ", goodsprice=" + goodsprice +
                        ", isStop=" + isStop +
                        ", kind='" + kind + '\'' +
                        ", lastUpdatePerson='" + lastUpdatePerson + '\'' +
                        ", origin='" + origin + '\'' +
                        ", pknumber=" + pknumber +
                        ", provider='" + provider + '\'' +
                        ", remarks='" + remarks + '\'' +
                        ", shop=" + shop +
                        ", spec='" + spec + '\'' +
                        ", storeNum=" + storeNum +
                        ", storeNumCk=" + storeNumCk +
                        ", unit='" + unit + '\'' +
                        '}';
            }

            public static class GoodspriceBean {
                public int costprice;
                public int curprice;
                public String gcode;
                public int memPrice;
                public int vipPrice;

                @Override
                public String toString() {
                    return "GoodspriceBean{" +
                            "costprice=" + costprice +
                            ", curprice=" + curprice +
                            ", gcode='" + gcode + '\'' +
                            ", memPrice=" + memPrice +
                            ", vipPrice=" + vipPrice +
                            '}';
                }
            }

            public static class ShopBean {
                public String id;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                @Override
                public String toString() {
                    return "ShopBean{" +
                            "id='" + id + '\'' +
                            '}';
                }
            }
        }

        public static class ShelfGoodsBean implements Parcelable {
            public String aid;
            public String barcode;
            public String gclass;
            public String gname;
            public String origin;
            public String sid;
            public String sname;
            public String spec;
            public String tinyip;

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.aid);
                dest.writeString(this.barcode);
                dest.writeString(this.gclass);
                dest.writeString(this.gname);
                dest.writeString(this.origin);
                dest.writeString(this.sid);
                dest.writeString(this.sname);
                dest.writeString(this.spec);
                dest.writeString(this.tinyip);
            }

            public ShelfGoodsBean() {
            }

            protected ShelfGoodsBean(Parcel in) {
                this.aid = in.readString();
                this.barcode = in.readString();
                this.gclass = in.readString();
                this.gname = in.readString();
                this.origin = in.readString();
                this.sid = in.readString();
                this.sname = in.readString();
                this.spec = in.readString();
                this.tinyip = in.readString();
            }

            public static final Parcelable.Creator<ShelfGoodsBean> CREATOR = new Parcelable.Creator<ShelfGoodsBean>() {
                @Override
                public ShelfGoodsBean createFromParcel(Parcel source) {
                    return new ShelfGoodsBean(source);
                }

                @Override
                public ShelfGoodsBean[] newArray(int size) {
                    return new ShelfGoodsBean[size];
                }
            };

            @Override
            public String toString() {
                return "ShelfGoodsBean{" +
                        "aid='" + aid + '\'' +
                        ", barcode='" + barcode + '\'' +
                        ", gclass='" + gclass + '\'' +
                        ", gname='" + gname + '\'' +
                        ", origin='" + origin + '\'' +
                        ", sid='" + sid + '\'' +
                        ", sname='" + sname + '\'' +
                        ", spec='" + spec + '\'' +
                        ", tinyip='" + tinyip + '\'' +
                        '}';
            }
        }
        }
    }
}
