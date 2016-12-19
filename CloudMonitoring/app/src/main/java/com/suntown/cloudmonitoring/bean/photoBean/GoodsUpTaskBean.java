package com.suntown.cloudmonitoring.bean.photoBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/11.
 */

public class GoodsUpTaskBean implements Parcelable {


    /**
     * eslWarn : {"eslUptTasks":{"adddate":1463387402000,"esl":{"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.0.65","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"shop":{"id":"571002002","name":"施家桥店"},"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""},"goods":{"barcode":"6921581596048","bitmapid":0,"brand":"","costPrice":0,"curPrice":0,"gclass":"","gcode":"","gname":"","isStop":0,"kind":"","lastUpdatePerson":"","origin":"","pknumber":0,"provider":"","remarks":"","spec":"","storeNum":0,"storeNumCk":0,"unit":""},"memprice":0,"oriprice":2.9,"partMap":0,"priceType":0,"pricetype":0,"taskid":1245856,"uptprice":1630}}
     */

    public EslWarnBean eslWarn;


    public static class EslWarnBean implements Parcelable {
        /**
         * eslUptTasks : {"adddate":1463387402000,"esl":{"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.0.65","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"shop":{"id":"571002002","name":"施家桥店"},"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""},"goods":{"barcode":"6921581596048","bitmapid":0,"brand":"","costPrice":0,"curPrice":0,"gclass":"","gcode":"","gname":"","isStop":0,"kind":"","lastUpdatePerson":"","origin":"","pknumber":0,"provider":"","remarks":"","spec":"","storeNum":0,"storeNumCk":0,"unit":""},"memprice":0,"oriprice":2.9,"partMap":0,"priceType":0,"pricetype":0,"taskid":1245856,"uptprice":1630}
         */

        public EslUptTasksBean eslUptTasks;


        public static class EslUptTasksBean implements Parcelable {
            /**
             * adddate : 1463387402000
             * esl : {"apid_":"","currentStatus":0,"eslTypeName":"","hwtype":0,"ip":"0.0.0.65","isLeader":0,"lqi":0,"mac":"","poweroff":0,"rssi_fo":0,"shop":{"id":"571002002","name":"施家桥店"},"swversion":"","totalLoginCount":0,"type":0,"voltage":0,"voltageScope":""}
             * goods : {"barcode":"6921581596048","bitmapid":0,"brand":"","costPrice":0,"curPrice":0,"gclass":"","gcode":"","gname":"","isStop":0,"kind":"","lastUpdatePerson":"","origin":"","pknumber":0,"provider":"","remarks":"","spec":"","storeNum":0,"storeNumCk":0,"unit":""}
             * memprice : 0
             * oriprice : 2.9
             * partMap : 0
             * priceType : 0
             * pricetype : 0
             * taskid : 1245856
             * uptprice : 1630
             */

            public long adddate;
            public EslBean esl;
            public GoodsBean goods;
            public String memprice;
            public String oriprice;
            public int partMap;
            public int priceType;
            public int pricetype;
            public int taskid;
            public String uptprice;

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


            public static class EslBean implements Parcelable {
                /**
                 * apid_ :
                 * currentStatus : 0
                 * eslTypeName :
                 * hwtype : 0
                 * ip : 0.0.0.65
                 * isLeader : 0
                 * lqi : 0
                 * mac :
                 * poweroff : 0
                 * rssi_fo : 0
                 * shop : {"id":"571002002","name":"施家桥店"}
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


                public static class ShopBean implements Parcelable {
                    /**
                     * id : 571002002
                     * name : 施家桥店
                     */

                    public String id;
                    public String name;


                    @Override
                    public int describeContents() {
                        return 0;
                    }

                    @Override
                    public void writeToParcel(Parcel dest, int flags) {
                        dest.writeString(this.id);
                        dest.writeString(this.name);
                    }

                    public ShopBean() {
                    }

                    protected ShopBean(Parcel in) {
                        this.id = in.readString();
                        this.name = in.readString();
                    }

                    public static final Parcelable.Creator<ShopBean> CREATOR = new Parcelable.Creator<ShopBean>() {
                        @Override
                        public ShopBean createFromParcel(Parcel source) {
                            return new ShopBean(source);
                        }

                        @Override
                        public ShopBean[] newArray(int size) {
                            return new ShopBean[size];
                        }
                    };

                    @Override
                    public String toString() {
                        return "ShopBean{" +
                                "id='" + id + '\'' +
                                ", name='" + name + '\'' +
                                '}';
                    }
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
                    dest.writeParcelable(this.shop, flags);
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

            public static class GoodsBean implements Parcelable {
                /**
                 * barcode : 6921581596048
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

                public String barcode;
                public int bitmapid;
                public String brand;
                public String costPrice;
                public String curPrice;
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
                    dest.writeString(this.costPrice);
                    dest.writeString(this.curPrice);
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
                    this.costPrice = in.readString();
                    this.curPrice = in.readString();
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
                dest.writeParcelable(this.esl, flags);
                dest.writeParcelable(this.goods, flags);
                dest.writeString(this.memprice);
                dest.writeString(this.oriprice);
                dest.writeInt(this.partMap);
                dest.writeInt(this.priceType);
                dest.writeInt(this.pricetype);
                dest.writeInt(this.taskid);
                dest.writeString(this.uptprice);
            }

            public EslUptTasksBean() {
            }

            protected EslUptTasksBean(Parcel in) {
                this.adddate = in.readLong();
                this.esl = in.readParcelable(EslBean.class.getClassLoader());
                this.goods = in.readParcelable(GoodsBean.class.getClassLoader());
                this.memprice = in.readString();
                this.oriprice = in.readString();
                this.partMap = in.readInt();
                this.priceType = in.readInt();
                this.pricetype = in.readInt();
                this.taskid = in.readInt();
                this.uptprice = in.readString();
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.eslUptTasks, flags);
        }

        public EslWarnBean() {
        }

        protected EslWarnBean(Parcel in) {
            this.eslUptTasks = in.readParcelable(EslUptTasksBean.class.getClassLoader());
        }

        public static final Creator<EslWarnBean> CREATOR = new Creator<EslWarnBean>() {
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
        dest.writeParcelable((Parcelable) this.eslWarn, flags);
    }

    public GoodsUpTaskBean() {
    }

    protected GoodsUpTaskBean(Parcel in) {
        this.eslWarn = in.readParcelable(EslWarnBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<GoodsUpTaskBean> CREATOR = new Parcelable.Creator<GoodsUpTaskBean>() {
        @Override
        public GoodsUpTaskBean createFromParcel(Parcel source) {
            return new GoodsUpTaskBean(source);
        }

        @Override
        public GoodsUpTaskBean[] newArray(int size) {
            return new GoodsUpTaskBean[size];
        }
    };
}
