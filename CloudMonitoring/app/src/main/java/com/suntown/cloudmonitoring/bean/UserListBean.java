package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/24.
 */

public class UserListBean {
    public List<UsersBean> users;

    @Override
    public String toString() {
        return "UserListBean{" +
                "users=" + users +
                '}';
    }


    public static class UsersBean {
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
            return "UsersBean{" +
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
}
