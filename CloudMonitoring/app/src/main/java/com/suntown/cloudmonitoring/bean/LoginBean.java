package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/9/12.
 */
public class LoginBean extends BaseBean{

    /**
     * resultCode : 1
     * user : {"email":"","ename":"","hobby":"","idcardNum":"356666661626666633","isSytem":1,"marriage":0,"mobile":"18626192876","nativName":"","nativePlace":"","orgCode":"*1","orgName":"","postalCode":"","pushid":"18171adc0305c44bf0f","pushnum":2,"sex":1,"tel":"18626192876","uflag":"","unSubscribe":0,"uname":"admin","upswd":"0x21232f297a57a5a743894a0e4a801fc3","userid":"admin"}
     */

//    private int resultCode;
    /**
     * email :
     * ename :
     * hobby :
     * idcardNum : 356666661626666633
     * isSytem : 1
     * marriage : 0
     * mobile : 18626192876
     * nativName :
     * nativePlace :
     * orgCode : *1
     * orgName :
     * postalCode :
     * pushid : 18171adc0305c44bf0f
     * pushnum : 2
     * sex : 1
     * tel : 18626192876
     * uflag :
     * unSubscribe : 0
     * uname : admin
     * upswd : 0x21232f297a57a5a743894a0e4a801fc3
     * userid : admin
     */


//    public int getResultCode() {
//        return resultCode;
//    }
//
//    public void setResultCode(int resultCode) {
//        this.resultCode = resultCode;
//    }
//
    public UserBean getUser() {
        return user;
    }
    private UserBean user;

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean {
        private String email;
        private String ename;
        private String hobby;
        private String idcardNum;
        private int isSytem;
        private int marriage;
        private String mobile;
        private String nativName;
        private String nativePlace;
        private String orgCode;
        private String orgName;
        private String postalCode;
        private String pushid;
        private int pushnum;
        private int sex;
        private String tel;
        private String uflag;
        private int unSubscribe;
        private String uname;
        private String upswd;
        private String userid;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getEname() {
            return ename;
        }

        public void setEname(String ename) {
            this.ename = ename;
        }

        public String getHobby() {
            return hobby;
        }

        public void setHobby(String hobby) {
            this.hobby = hobby;
        }

        public String getIdcardNum() {
            return idcardNum;
        }

        public void setIdcardNum(String idcardNum) {
            this.idcardNum = idcardNum;
        }

        public int getIsSytem() {
            return isSytem;
        }

        public void setIsSytem(int isSytem) {
            this.isSytem = isSytem;
        }

        public int getMarriage() {
            return marriage;
        }

        public void setMarriage(int marriage) {
            this.marriage = marriage;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNativName() {
            return nativName;
        }

        public void setNativName(String nativName) {
            this.nativName = nativName;
        }

        public String getNativePlace() {
            return nativePlace;
        }

        public void setNativePlace(String nativePlace) {
            this.nativePlace = nativePlace;
        }

        public String getOrgCode() {
            return orgCode;
        }

        public void setOrgCode(String orgCode) {
            this.orgCode = orgCode;
        }

        public String getOrgName() {
            return orgName;
        }

        public void setOrgName(String orgName) {
            this.orgName = orgName;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
        }

        public String getPushid() {
            return pushid;
        }

        public void setPushid(String pushid) {
            this.pushid = pushid;
        }

        public int getPushnum() {
            return pushnum;
        }

        public void setPushnum(int pushnum) {
            this.pushnum = pushnum;
        }

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public String getTel() {
            return tel;
        }

        public void setTel(String tel) {
            this.tel = tel;
        }

        public String getUflag() {
            return uflag;
        }

        public void setUflag(String uflag) {
            this.uflag = uflag;
        }

        public int getUnSubscribe() {
            return unSubscribe;
        }

        public void setUnSubscribe(int unSubscribe) {
            this.unSubscribe = unSubscribe;
        }

        public String getUname() {
            return uname;
        }

        public void setUname(String uname) {
            this.uname = uname;
        }

        public String getUpswd() {
            return upswd;
        }

        public void setUpswd(String upswd) {
            this.upswd = upswd;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        @Override
        public String toString() {
            return "UserBean{" +
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
