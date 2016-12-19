package com.suntown.cloudmonitoring.bean.photoBean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/11/11.
 */

public class ShelfBean implements Parcelable {
    /**
     * eslWarn : {"shelfGoods":{"aid":"57156666754","barcode":"33","gclass":"","gname":"得益自热方便米饭（红烧牛肉）","origin":"","sid":"571566667","sname":"仁盈天亿大厦","spec":"","tinyip":"0.0.5.234"}}
     */

    public EslWarnBean eslWarn;


    public static class EslWarnBean implements Parcelable {
        /**
         * shelfGoods : {"aid":"57156666754","barcode":"33","gclass":"","gname":"得益自热方便米饭（红烧牛肉）","origin":"","sid":"571566667","sname":"仁盈天亿大厦","spec":"","tinyip":"0.0.5.234"}
         */

        public ShelfGoodsBean shelfGoods;

        public static class ShelfGoodsBean implements Parcelable {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(this.shelfGoods, flags);
        }

        public EslWarnBean() {
        }

        protected EslWarnBean(Parcel in) {
            this.shelfGoods = in.readParcelable(ShelfGoodsBean.class.getClassLoader());
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

    public ShelfBean() {
    }

    protected ShelfBean(Parcel in) {
        this.eslWarn = in.readParcelable(EslWarnBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShelfBean> CREATOR = new Parcelable.Creator<ShelfBean>() {
        @Override
        public ShelfBean createFromParcel(Parcel source) {
            return new ShelfBean(source);
        }

        @Override
        public ShelfBean[] newArray(int size) {
            return new ShelfBean[size];
        }
    };
}
