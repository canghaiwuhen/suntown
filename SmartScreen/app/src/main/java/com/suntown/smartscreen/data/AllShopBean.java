package com.suntown.smartscreen.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class AllShopBean {
    /**
     * city : [{"shop":[{"id":"797000001","name":"ewj江西赣州V+店"}],"rownum":1,"id":"797","name":"赣州"},{"shop":[{"id":"210002001","name":"ewj上海长宁店"},{"id":"210002008","name":"寺岗测试"}],"rownum":2,"id":"210","name":"上海"},{"shop":[{"id":"571002001","name":"濮家店"},{"id":"571002002","name":"施家桥店"},{"id":"571002003","name":"朝晖店"},{"id":"571566668","name":"仁盈西湖科技园"},{"id":"571566666","name":"仁盈科技数娱大厦"},{"id":"571003001","name":"支付宝"},{"id":"571566667","name":"仁盈天亿大厦"},{"id":"571002000","name":"升腾支付宝"},{"id":"750001002","name":"ewj武汉展会"},{"id":"571002004","name":"ewj杭州龙湖店"},{"id":"571002005","name":"乐购新塘景芳店"},{"id":"571002007","name":"乐木几杭州武林广场店"},{"id":"571501235","name":"仁盈科技互联网金融大厦"},{"id":"571002008","name":"世纪联华西湖银泰店"},{"id":"571002006","name":"乐购大家钱塘府"},{"id":"571003073","name":"实施部内部测试_wx"},{"id":"571003074","name":"杭州酒庄"},{"id":"571003056","name":"友商科技2"},{"id":"571003010","name":"C.Smart德国展会"},{"id":"003032","name":"杭州图特"},{"id":"571003021","name":"邮乐桐庐许帯娣副食品商店"},{"id":"571003025","name":"美思特"},{"id":"571003033","name":"华东大药房"},{"id":"571003038","name":"testcs"},{"id":"571003039","name":"et"},{"id":"571003046","name":"驿商科技高速公路"},{"id":"571003047","name":"乐木几广州展会"},{"id":"571003048","name":"理财测试门店"},{"id":"571003049","name":"实施部测试1"},{"id":"571003050","name":"绿城领鲜超市"},{"id":"571003051","name":"绿城领鲜超市2"},{"id":"571003052","name":"浙江省邮电工程有限公司"},{"id":"571003053","name":"测试门店"},{"id":"571003054","name":"友商科技"},{"id":"571003055","name":"众马"},{"id":"571003057","name":"内部测试门店8s"},{"id":"571003058","name":"内部测试门店16s"},{"id":"571003059","name":"内部测试门店32S"},{"id":"571003060","name":"嘉兴瀚腾测试"},{"id":"571003062","name":"爱客仕"},{"id":"571003063","name":"爱客仕-杭州店"},{"id":"571003068","name":"测试1"},{"id":"571003066","name":"测试"},{"id":"571003069","name":"爱客仕安全测试"},{"id":"571003070","name":"西狗国际-杭州"},{"id":"571003071","name":"实施部内部测试"},{"id":"571003072","name":"xjc实施部测试"}],"rownum":3,"id":"571","name":"杭州"},{"shop":[{"id":"270001001","name":"ewj南京碑亭巷"},{"id":"270001002","name":"ewj南京三元巷"},{"id":"270001003","name":"ewj南京湖北路好的便利店"},{"id":"270001004","name":"ewj南京好的嵩山路"},{"id":"270001005","name":"ewj南京好的黄山路"},{"id":"270001006","name":"ewj南京好的仙林东路东城汇"},{"id":"270001007","name":"ewj南京好的汉口西路"},{"id":"270001008","name":"ewj南京好的中山东路"},{"id":"270001009","name":"ewj南京公交一村"},{"id":"270001010","name":"ewj南京仙境路生活超市"},{"id":"270001011","name":"ewj南京苏果张府园"},{"id":"270001012","name":"ewj南京苏果溧水万辰购物广场"},{"id":"270001013","name":"ewj南京中山北路购物广场"},{"id":"270001014","name":"ewj南京解放路标超"},{"id":"270001015","name":"ewj南京解放路便利店"},{"id":"270001016","name":"ewj南京新街口店"},{"id":"270001017","name":"ewj南京锁金东路"},{"id":"270001018","name":"ewj南京亚东店"},{"id":"270001019","name":"ewj南京天鹅湖社区店"},{"id":"270001020","name":"ewj南京殷巷购物广场店"}],"rownum":4,"id":"270","name":"南京"},{"shop":[{"id":"512001001","name":"ewj苏州园区店"},{"id":"512001002","name":"乐购苏州测试SRD"}],"rownum":5,"id":"512","name":"苏州"},{"shop":[{"id":"510000001","name":"宜兴电力物资仓库"},{"id":"510000002","name":"电力测试门店"}],"rownum":6,"id":"510","name":"无锡"},{"shop":[{"id":"577000001","name":"乐木几温州南站店"},{"id":"577000002","name":"乐木几测试门店"},{"id":"577000003","name":"温州由你秀信用超市"}],"rownum":7,"id":"577","name":"温州"}]
     * id : 1
     * name : 华东区
     */

    public String id;
    public String name;
    public List<CityBean> city;

    @Override
    public String toString() {
        return "AllShopBean{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", city=" + city +
                '}';
    }


    public static class CityBean {
        /**
         * shop : [{"id":"797000001","name":"ewj江西赣州V+店"}]
         * rownum : 1
         * id : 797
         * name : 赣州
         */

        public int rownum;
        public String id;
        public String name;
        public boolean checked  = false;
        public List<ShopBean> shop;

        @Override
        public String toString() {
            return "CityBean{" +
                    "rownum=" + rownum +
                    ", id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", shop=" + shop +
                    '}';
        }


        public static class ShopBean implements Parcelable {
            /**
             * id : 797000001
             * name : ewj江西赣州V+店
             */

            public String id;
            public String name;

            @Override
            public String toString() {
                return "ShopBean{" +
                        "id='" + id + '\'' +
                        ", name='" + name + '\'' +
                        '}';
            }

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
        }
    }
}
