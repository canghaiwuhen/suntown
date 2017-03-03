package com.suntown.smartscreen.data;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.suntown.smartscreen.shopCenter.detial.ExpandableItemAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/2/15.
 */

public class ShopBoardBean implements Parcelable {
    /**
     * ROWS : 37
     * RECORD : [{"TYPENAME":"价格模板","DMSPECID":"121","ANGLE":"90","DMNAME":"2016121555","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"uu","ISSPLASHNAME":"无闪屏","DMCODE":"769","SPECNAME":"11","ISACTIVE":"1","TYPE":"0","LIGHTCODE":""},{"TYPENAME":"陈列模板","DMSPECID":"121","ANGLE":"0","DMNAME":"23232","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","ISSPLASHNAME":"无闪屏","DMCODE":"347","SPECNAME":"11","ISACTIVE":"1","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"陈列模板","DMSPECID":"141","ANGLE":"0","DMNAME":"2.8寸陈列模板","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","ISSPLASHNAME":"无闪屏","DMCODE":"359","SPECNAME":"2.9寸","ISACTIVE":"1","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"促销模板","DMSPECID":"6","ANGLE":"0","DMNAME":"SALE","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"807","SPECNAME":"2英寸","ISACTIVE":"0","TYPE":"2","LIGHTCODE":""},{"TYPENAME":"会员价模板","DMSPECID":"6","ANGLE":"0","DMNAME":"施家桥店会员价格","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"341","SPECNAME":"2英寸","ISACTIVE":"1","TYPE":"3","LIGHTCODE":"3"},{"TYPENAME":"补货模板","DMSPECID":"6","ANGLE":"0","DMNAME":"补货模板","ISSPLASH":"0","LIGHTPERIOD":"50","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"146","SPECNAME":"2英寸","ISACTIVE":"1","TYPE":"5","LIGHTCODE":"0"},{"TYPENAME":"临保模板","DMSPECID":"6","ANGLE":"0","DMNAME":"临保模板","ISSPLASH":"0","LIGHTPERIOD":"80","LIGHTCODENAME":"红灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"145","SPECNAME":"2英寸","ISACTIVE":"0","TYPE":"4","LIGHTCODE":"1"},{"TYPENAME":"会员价模板","DMSPECID":"6","ANGLE":"0","DMNAME":"会员价格模板","ISSPLASH":"0","LIGHTPERIOD":"50","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"143","SPECNAME":"2英寸","ISACTIVE":"1","TYPE":"3","LIGHTCODE":"0"},{"TYPENAME":"促销模板","DMSPECID":"6","ANGLE":"0","DMNAME":"促销模板","ISSPLASH":"0","LIGHTPERIOD":"100","LIGHTCODENAME":"绿灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"142","SPECNAME":"2英寸","ISACTIVE":"1","TYPE":"2","LIGHTCODE":"2"},{"TYPENAME":"促销模板","DMSPECID":"6","ANGLE":"0","DMNAME":"促销价格","ISSPLASH":"0","LIGHTPERIOD":"200","LIGHTCODENAME":"绿灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"281","SPECNAME":"2英寸","ISACTIVE":"0","TYPE":"2","LIGHTCODE":"2"},{"TYPENAME":"陈列模板","DMSPECID":"6","ANGLE":"0","DMNAME":"陈列模板","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","ISSPLASHNAME":"无闪屏","DMCODE":"141","SPECNAME":"2英寸","ISACTIVE":"1","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"价格模板","DMSPECID":"6","ANGLE":"0","DMNAME":"施家桥价格模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"301","SPECNAME":"2英寸","ISACTIVE":"1","TYPE":"0","LIGHTCODE":"0"},{"TYPENAME":"促销模板","DMSPECID":"6","ANGLE":"0","DMNAME":"施家桥促销价格","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"红灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"321","SPECNAME":"2英寸","ISACTIVE":"0","TYPE":"2","LIGHTCODE":"1"},{"TYPENAME":"陈列模板","DMSPECID":"161","ANGLE":"0","DMNAME":"2.04寸陈列模板","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","ISSPLASHNAME":"无闪屏","DMCODE":"345","SPECNAME":"2.04英寸","ISACTIVE":"1","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"促销模板","DMSPECID":"161","ANGLE":"0","DMNAME":"2.04寸促销模板","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"红灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"344","SPECNAME":"2.04英寸","ISACTIVE":"1","TYPE":"2","LIGHTCODE":"1"},{"TYPENAME":"价格模板","DMSPECID":"161","ANGLE":"0","DMNAME":"2.04寸价格模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"343","SPECNAME":"2.04英寸","ISACTIVE":"1","TYPE":"0","LIGHTCODE":"0"},{"TYPENAME":"会员价模板","DMSPECID":"161","ANGLE":"0","DMNAME":"2.04会员价","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"362","SPECNAME":"2.04英寸","ISACTIVE":"1","TYPE":"3","LIGHTCODE":"0"},{"TYPENAME":"会员价模板","DMSPECID":"164","ANGLE":"0","DMNAME":"2.13会员模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"361","SPECNAME":"2.13(212*104)","ISACTIVE":"1","TYPE":"3","LIGHTCODE":"0"},{"TYPENAME":"陈列模板","DMSPECID":"164","ANGLE":"0","DMNAME":"2.13陈列模板（濮家店）","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"498","SPECNAME":"2.13(212*104)","ISACTIVE":"0","TYPE":"1","LIGHTCODE":""},{"TYPENAME":"陈列模板","DMSPECID":"164","ANGLE":"0","DMNAME":"2.13寸陈列模板","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","ISSPLASHNAME":"无闪屏","DMCODE":"356","SPECNAME":"2.13(212*104)","ISACTIVE":"0","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"缺货模板","DMSPECID":"164","ANGLE":"0","DMNAME":"2.13寸缺货","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"357","SPECNAME":"2.13(212*104)","ISACTIVE":"1","TYPE":"7","LIGHTCODE":"0"},{"TYPENAME":"促销模板","DMSPECID":"164","ANGLE":"0","DMNAME":"2.13促销模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"353","SPECNAME":"2.13(212*104)","ISACTIVE":"1","TYPE":"2","LIGHTCODE":"0"},{"TYPENAME":"价格模板","DMSPECID":"164","ANGLE":"0","DMNAME":"2.13寸价格","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"351","SPECNAME":"2.13(212*104)","ISACTIVE":"0","TYPE":"0","LIGHTCODE":"0"},{"TYPENAME":"价格模板","DMSPECID":"163","ANGLE":"0","DMNAME":"12151405","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"770","SPECNAME":"2.9(296*128)","ISACTIVE":"0","TYPE":"0","LIGHTCODE":""},{"TYPENAME":"价格模板","DMSPECID":"163","ANGLE":"0","DMNAME":"290世纪联华","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"573","SPECNAME":"2.9(296*128)","ISACTIVE":"1","TYPE":"0","LIGHTCODE":""},{"TYPENAME":"促异模板","DMSPECID":"163","ANGLE":"0","DMNAME":"2.9寸促异","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"360","SPECNAME":"2.9(296*128)","ISACTIVE":"1","TYPE":"8","LIGHTCODE":"0"},{"TYPENAME":"缺货模板","DMSPECID":"163","ANGLE":"0","DMNAME":"2.9寸缺货","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"358","SPECNAME":"2.9(296*128)","ISACTIVE":"1","TYPE":"7","LIGHTCODE":"0"},{"TYPENAME":"陈列模板","DMSPECID":"163","ANGLE":"0","DMNAME":"2.9寸陈列模板","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"1","ISSPLASHNAME":"无闪屏","DMCODE":"355","SPECNAME":"2.9(296*128)","ISACTIVE":"0","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"会员价模板","DMSPECID":"163","ANGLE":"0","DMNAME":"2.9会员模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"354","SPECNAME":"2.9(296*128)","ISACTIVE":"1","TYPE":"3","LIGHTCODE":"0"},{"TYPENAME":"陈列模板","DMSPECID":"165","ANGLE":"0","DMNAME":"2.13乐购陈列","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"黄灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"484","SPECNAME":"2.13(252*128)","ISACTIVE":"0","TYPE":"1","LIGHTCODE":"3"},{"TYPENAME":"价格模板","DMSPECID":"165","ANGLE":"0","DMNAME":"2.13寸支付宝模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"无灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"381","SPECNAME":"2.13(252*128)","ISACTIVE":"1","TYPE":"0","LIGHTCODE":"0"},{"TYPENAME":"会员价模板","DMSPECID":"166","ANGLE":"0","DMNAME":"POP8寸会员","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"红灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"407","SPECNAME":"8寸XianDi段码液晶屏","ISACTIVE":"1","TYPE":"3","LIGHTCODE":"1"},{"TYPENAME":"促销模板","DMSPECID":"166","ANGLE":"0","DMNAME":"POP8寸促销","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"红灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"406","SPECNAME":"8寸XianDi段码液晶屏","ISACTIVE":"0","TYPE":"2","LIGHTCODE":"1"},{"TYPENAME":"价格模板","DMSPECID":"166","ANGLE":"0","DMNAME":"POP8寸","ISSPLASH":"0","LIGHTPERIOD":"300","LIGHTCODENAME":"红灯","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"402","SPECNAME":"8寸XianDi段码液晶屏","ISACTIVE":"0","TYPE":"0","LIGHTCODE":"1"},{"TYPENAME":"价格模板","DMSPECID":"170","ANGLE":"0","DMNAME":"20161215","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"111","ISSPLASHNAME":"无闪屏","DMCODE":"767","SPECNAME":"1.54(152*152)","ISACTIVE":"1","TYPE":"0","LIGHTCODE":""},{"TYPENAME":"促销模板","DMSPECID":"168","ANGLE":"90","DMNAME":"4.2寸超市促销模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"457","SPECNAME":"4.2(400*300)","ISACTIVE":"1","TYPE":"2","LIGHTCODE":""},{"TYPENAME":"价格模板","DMSPECID":"169","ANGLE":"0","DMNAME":"1.54价格模板","ISSPLASH":"0","LIGHTPERIOD":"","LIGHTCODENAME":"","DESCRIBE":"","ISSPLASHNAME":"无闪屏","DMCODE":"446","SPECNAME":"1.54(200*200)","ISACTIVE":"0","TYPE":"0","LIGHTCODE":""}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    @Override
    public String toString() {
        return "ShopBoardBean{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBean implements Parcelable{
        /**
         * TYPENAME : 价格模板
         * DMSPECID : 121
         * ANGLE : 90
         * DMNAME : 2016121555
         * ISSPLASH : 0
         * LIGHTPERIOD :
         * LIGHTCODENAME :
         * DESCRIBE : uu
         * ISSPLASHNAME : 无闪屏
         * DMCODE : 769
         * SPECNAME : 11
         * ISACTIVE : 1
         * TYPE : 0
         * LIGHTCODE :
         */

        public String TYPENAME;
        public String DMSPECID;
        public String ANGLE;
        public String DMNAME;
        public String ISSPLASH;
        public String LIGHTPERIOD;
        public String LIGHTCODENAME;
        public String DESCRIBE;
        public String ISSPLASHNAME;
        public String DMCODE;
        public String SPECNAME;
        public String ISACTIVE;
        public String TYPE;
        public String LIGHTCODE;
        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.TYPENAME);
            dest.writeString(this.DMSPECID);
            dest.writeString(this.ANGLE);
            dest.writeString(this.DMNAME);
            dest.writeString(this.ISSPLASH);
            dest.writeString(this.LIGHTPERIOD);
            dest.writeString(this.LIGHTCODENAME);
            dest.writeString(this.DESCRIBE);
            dest.writeString(this.ISSPLASHNAME);
            dest.writeString(this.DMCODE);
            dest.writeString(this.SPECNAME);
            dest.writeString(this.ISACTIVE);
            dest.writeString(this.TYPE);
            dest.writeString(this.LIGHTCODE);
        }

        public RECORDBean() {
        }

        protected RECORDBean(Parcel in) {
            this.TYPENAME = in.readString();
            this.DMSPECID = in.readString();
            this.ANGLE = in.readString();
            this.DMNAME = in.readString();
            this.ISSPLASH = in.readString();
            this.LIGHTPERIOD = in.readString();
            this.LIGHTCODENAME = in.readString();
            this.DESCRIBE = in.readString();
            this.ISSPLASHNAME = in.readString();
            this.DMCODE = in.readString();
            this.SPECNAME = in.readString();
            this.ISACTIVE = in.readString();
            this.TYPE = in.readString();
            this.LIGHTCODE = in.readString();
        }

        public static final Creator<RECORDBean> CREATOR = new Creator<RECORDBean>() {
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
                    "TYPENAME='" + TYPENAME + '\'' +
                    ", DMSPECID='" + DMSPECID + '\'' +
                    ", ANGLE='" + ANGLE + '\'' +
                    ", DMNAME='" + DMNAME + '\'' +
                    ", ISSPLASH='" + ISSPLASH + '\'' +
                    ", LIGHTPERIOD='" + LIGHTPERIOD + '\'' +
                    ", LIGHTCODENAME='" + LIGHTCODENAME + '\'' +
                    ", DESCRIBE='" + DESCRIBE + '\'' +
                    ", ISSPLASHNAME='" + ISSPLASHNAME + '\'' +
                    ", DMCODE='" + DMCODE + '\'' +
                    ", SPECNAME='" + SPECNAME + '\'' +
                    ", ISACTIVE='" + ISACTIVE + '\'' +
                    ", TYPE='" + TYPE + '\'' +
                    ", LIGHTCODE='" + LIGHTCODE + '\'' +
                    '}';
        }


    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ROWS);
        dest.writeList(this.RECORD);
    }

    public ShopBoardBean() {
    }

    protected ShopBoardBean(Parcel in) {
        this.ROWS = in.readInt();
        this.RECORD = new ArrayList<RECORDBean>();
        in.readList(this.RECORD, RECORDBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ShopBoardBean> CREATOR = new Parcelable.Creator<ShopBoardBean>() {
        @Override
        public ShopBoardBean createFromParcel(Parcel source) {
            return new ShopBoardBean(source);
        }

        @Override
        public ShopBoardBean[] newArray(int size) {
            return new ShopBoardBean[size];
        }
    };
}
