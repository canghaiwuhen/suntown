package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */

public class FormListBean {
    public List<WorkformsBean> workforms;

    @Override
    public String toString() {
        return "FormListBean{" +
                "workforms=" + workforms +
                '}';
    }

    public static class WorkformsBean {
        /**
         * acceptDateStr :
         * acceptIdea :
         * acceptUser : {"email":"","ename":"","hobby":"","idcardNum":"","isSytem":0,"marriage":0,"mobile":"","nativName":"","nativePlace":"","orgCode":"","orgName":"","postalCode":"","pushid":"","pushnum":1,"sex":1,"tel":"","uflag":"","unSubscribe":0,"uname":"","upswd":"","userid":"admin"}
         * addCause : noCause
         * addDate : 1487123134000
         * addDateStr : 2017-02-15 09:45:34
         * addUser : {"email":"","ename":"","hobby":"","idcardNum":"","isSytem":0,"marriage":0,"mobile":"","nativName":"","nativePlace":"","orgCode":"","orgName":"","postalCode":"","pushid":"","pushnum":1,"sex":1,"tel":"","uflag":"","unSubscribe":0,"uname":"","upswd":"","userid":"ttt"}
         * asmsid :
         * id : 283
         * shop : {"id":"571002001","name":"濮家店"}
         * status : 0
         * wkind : 0
         * workNo : 1000308
         * workformInfos : [{"acceptIdea":"","status":0,"wid":"283"}]
         * workkind : 0
         */

        public String acceptDateStr;
        public String acceptIdea;
        public AcceptUserBean acceptUser;
        public String addCause;
        public long addDate;
        public String addDateStr;
        public AddUserBean addUser;
        public String asmsid;
        public String id;
        public ShopBean shop;
        public int status;
        public int wkind;
        public String workNo;
        public int workkind;
        public List<WorkformInfosBean> workformInfos;

        @Override
        public String toString() {
            return "WorkformsBean{" +
                    "acceptDateStr='" + acceptDateStr + '\'' +
                    ", acceptIdea='" + acceptIdea + '\'' +
                    ", acceptUser=" + acceptUser +
                    ", addCause='" + addCause + '\'' +
                    ", addDate=" + addDate +
                    ", addDateStr='" + addDateStr + '\'' +
                    ", addUser=" + addUser +
                    ", asmsid='" + asmsid + '\'' +
                    ", id='" + id + '\'' +
                    ", shop=" + shop +
                    ", status=" + status +
                    ", wkind=" + wkind +
                    ", workNo='" + workNo + '\'' +
                    ", workkind=" + workkind +
                    ", workformInfos=" + workformInfos +
                    '}';
        }


        public static class AcceptUserBean {
            /**
             * email :
             * ename :
             * hobby :
             * idcardNum :
             * isSytem : 0
             * marriage : 0
             * mobile :
             * nativName :
             * nativePlace :
             * orgCode :
             * orgName :
             * postalCode :
             * pushid :
             * pushnum : 1
             * sex : 1
             * tel :
             * uflag :
             * unSubscribe : 0
             * uname :
             * upswd :
             * userid : admin
             */

            public String email;
            public String ename;
            public String hobby;
            public String idcardNum;
            public int isSytem;
            public int marriage;
            public String mobile;
            public String nativName;
            public String nativePlace;
            public String orgCode;
            public String orgName;
            public String postalCode;
            public String pushid;
            public int pushnum;
            public int sex;
            public String tel;
            public String uflag;
            public int unSubscribe;
            public String uname;
            public String upswd;
            public String userid;

            @Override
            public String toString() {
                return "AcceptUserBean{" +
                        "email='" + email + '\'' +
                        ", ename='" + ename + '\'' +
                        ", hobby='" + hobby + '\'' +
                        ", idcardNum='" + idcardNum + '\'' +
                        ", isSytem=" + isSytem +
                        ", marriage=" + marriage +
                        ", mobile='" + mobile + '\'' +
                        ", nativName='" + nativName + '\'' +
                        ", nativePlace='" + nativePlace + '\'' +
                        ", orgCode='" + orgCode + '\'' +
                        ", orgName='" + orgName + '\'' +
                        ", postalCode='" + postalCode + '\'' +
                        ", pushid='" + pushid + '\'' +
                        ", pushnum=" + pushnum +
                        ", sex=" + sex +
                        ", tel='" + tel + '\'' +
                        ", uflag='" + uflag + '\'' +
                        ", unSubscribe=" + unSubscribe +
                        ", uname='" + uname + '\'' +
                        ", upswd='" + upswd + '\'' +
                        ", userid='" + userid + '\'' +
                        '}';
            }
        }

        public static class AddUserBean {
            /**
             * email :
             * ename :
             * hobby :
             * idcardNum :
             * isSytem : 0
             * marriage : 0
             * mobile :
             * nativName :
             * nativePlace :
             * orgCode :
             * orgName :
             * postalCode :
             * pushid :
             * pushnum : 1
             * sex : 1
             * tel :
             * uflag :
             * unSubscribe : 0
             * uname :
             * upswd :
             * userid : ttt
             */

            public String email;
            public String ename;
            public String hobby;
            public String idcardNum;
            public int isSytem;
            public int marriage;
            public String mobile;
            public String nativName;
            public String nativePlace;
            public String orgCode;
            public String orgName;
            public String postalCode;
            public String pushid;
            public int pushnum;
            public int sex;
            public String tel;
            public String uflag;
            public int unSubscribe;
            public String uname;
            public String upswd;
            public String userid;


            @Override
            public String toString() {
                return "AddUserBean{" +
                        "email='" + email + '\'' +
                        ", ename='" + ename + '\'' +
                        ", hobby='" + hobby + '\'' +
                        ", idcardNum='" + idcardNum + '\'' +
                        ", isSytem=" + isSytem +
                        ", marriage=" + marriage +
                        ", mobile='" + mobile + '\'' +
                        ", nativName='" + nativName + '\'' +
                        ", nativePlace='" + nativePlace + '\'' +
                        ", orgCode='" + orgCode + '\'' +
                        ", orgName='" + orgName + '\'' +
                        ", postalCode='" + postalCode + '\'' +
                        ", pushid='" + pushid + '\'' +
                        ", pushnum=" + pushnum +
                        ", sex=" + sex +
                        ", tel='" + tel + '\'' +
                        ", uflag='" + uflag + '\'' +
                        ", unSubscribe=" + unSubscribe +
                        ", uname='" + uname + '\'' +
                        ", upswd='" + upswd + '\'' +
                        ", userid='" + userid + '\'' +
                        '}';
            }
        }

        public static class ShopBean {
            /**
             * id : 571002001
             * name : 濮家店
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
        }

        public static class WorkformInfosBean {
            /**
             * acceptIdea :
             * status : 0
             * wid : 283
             */

            public String acceptIdea;
            public int status;
            public String wid;

            @Override
            public String toString() {
                return "WorkformInfosBean{" +
                        "acceptIdea='" + acceptIdea + '\'' +
                        ", status=" + status +
                        ", wid='" + wid + '\'' +
                        '}';
            }
        }
    }
}
