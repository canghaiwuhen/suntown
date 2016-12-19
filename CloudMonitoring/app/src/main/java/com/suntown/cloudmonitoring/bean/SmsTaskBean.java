package com.suntown.cloudmonitoring.bean;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2016/10/14.
 */
public class SmsTaskBean implements Parcelable {

    /**
     * apRecord : 12179
     * apSmstasks : {"addTimeStr":"2016-12-06 07:51:14","addtime":1480981874000,"content":"AP和SERVER链接中断[APADDR:69],故障已经排除，消息来自126云服务:乐木几温州南站店","detailUrl":"http://www.iesl.com.cn/weChatTemplete/templete/2016/12/sms_13355789329120161206075117293977168.html","epkind":1,"ip":"69","issuccess":1,"lookstatus":0,"msgid":103422,"resultCode":"{\"result\":\"ok\",\"errcode\":\"0\",\"msgid\":\"422322156\",\"errmsg\":\"send by wechat\",\"wx_send_times\":\"1\",\"sms_send_times\":\"0\"}\r\n","sendCount":0,"sendTimeStr":"2016-12-06 07:51:17","sendtime":1480981877000,"sid":"577000001","smsType":1,"startDate":1480981874000,"startDateStr":"2016-12-06 07:51:14","tel":"13355789329","wxSendCount":1}
     * lowBatteryRecord : 348
     * lowSmstasks : {"addTimeStr":"2016-12-06 01:02:49","addtime":1480957369000,"content":"电子标签电量报警，消息来自126云服务:全部","detailUrl":"http://www.iesl.com.cn/weChatTemplete/templete/2016/12/sms_13355789329620161206010254-811727824.html","epkind":0,"ip":"","issuccess":1,"lookstatus":0,"msgid":103396,"resultCode":"{\"result\":\"ok\",\"errcode\":\"0\",\"msgid\":\"422321202\",\"errmsg\":\"send by wechat\",\"wx_send_times\":\"1\",\"sms_send_times\":\"0\"}\r\n","sendCount":0,"sendTimeStr":"2016-12-06 01:02:54","sendtime":1480957374000,"sid":"","smsType":6,"startDate":1480957369000,"startDateStr":"2016-12-06 01:02:49","tel":"13355789329","wxSendCount":1}
     * noLoginSmstaskRecord : 1384
     * noLoginSmstasks : {"addTimeStr":"2015-11-16 04:31:52","addtime":1447619512000,"content":"累计超时未注册标签共:2个，消息来自126云端:ewj南京苏果张府园","detailUrl":"","epkind":0,"ip":"270001011","issuccess":1,"lookstatus":0,"msgid":12442,"resultCode":"{\"result\":\"ok\",\"errcode\":\"0\",\"msgid\":\"400651956\",\"errmsg\":\"send by wechat\",\"wx_send_times\":\"1\",\"sms_send_times\":\"0\"}\r\n","sendCount":0,"sendTimeStr":"2015-11-16 04:31:55","sendtime":1447619515000,"sid":"270001011","smsType":10,"startDate":1447619512000,"startDateStr":"2015-11-16 04:31:52","tel":"13355789329","wxSendCount":1}
     * noUptPriceRecord : 996
     * noUptSmstasks : {"addTimeStr":"2016-02-17 09:33:30","addtime":1455672810000,"content":"电子标签变价失败[ED:0.0.16.90],故障已经排除，消息来自126云端:ewj西安V+","detailUrl":"","epkind":1,"ip":"0.0.16.90","issuccess":1,"lookstatus":0,"msgid":30011,"resultCode":"{\"result\":\"ok\",\"errcode\":\"0\",\"msgid\":\"402943904\",\"errmsg\":\"send by wechat\",\"wx_send_times\":\"1\",\"sms_send_times\":\"0\"}\r\n","sendCount":0,"sendTimeStr":"2016-02-17 09:33:30","sendtime":1455672810000,"sid":"290000001","smsType":7,"startDate":1455672810000,"startDateStr":"2016-02-17 09:33:30","tel":"13355789329","wxSendCount":1}
     * otherSmstasks : {"addTimeStr":"2016-12-06 07:51:14","addtime":1480981874000,"content":"AP和SERVER链接中断[APADDR:69],故障已经排除，消息来自126云服务:乐木几温州南站店","detailUrl":"http://www.iesl.com.cn/weChatTemplete/templete/2016/12/sms_13355789329120161206075117293977168.html","epkind":1,"ip":"69","issuccess":1,"lookstatus":0,"msgid":103422,"resultCode":"{\"result\":\"ok\",\"errcode\":\"0\",\"msgid\":\"422322156\",\"errmsg\":\"send by wechat\",\"wx_send_times\":\"1\",\"sms_send_times\":\"0\"}\r\n","sendCount":0,"sendTimeStr":"2016-12-06 07:51:17","sendtime":1480981877000,"sid":"577000001","smsType":1,"startDate":1480981874000,"startDateStr":"2016-12-06 07:51:14","tel":"13355789329","wxSendCount":1}
     * otherSmstasksRecord : 21732
     */

    public int apRecord;
    public ApSmstasksBean apSmstasks;
    public int lowBatteryRecord;
    public LowSmstasksBean lowSmstasks;
    public int noLoginSmstaskRecord;
    public NoLoginSmstasksBean noLoginSmstasks;
    public int noUptPriceRecord;
    public NoUptSmstasksBean noUptSmstasks;
    public OtherSmstasksBean otherSmstasks;
    public int otherSmstasksRecord;

    @Override
    public String toString() {
        return "SmsTaskBean{" +
                "apRecord=" + apRecord +
                ", apSmstasks=" + apSmstasks +
                ", lowBatteryRecord=" + lowBatteryRecord +
                ", lowSmstasks=" + lowSmstasks +
                ", noLoginSmstaskRecord=" + noLoginSmstaskRecord +
                ", noLoginSmstasks=" + noLoginSmstasks +
                ", noUptPriceRecord=" + noUptPriceRecord +
                ", noUptSmstasks=" + noUptSmstasks +
                ", otherSmstasks=" + otherSmstasks +
                ", otherSmstasksRecord=" + otherSmstasksRecord +
                '}';
    }

    public static class ApSmstasksBean implements Parcelable {
        /**
         * addTimeStr : 2016-12-06 07:51:14
         * addtime : 1480981874000
         * content : AP和SERVER链接中断[APADDR:69],故障已经排除，消息来自126云服务:乐木几温州南站店
         * detailUrl : http://www.iesl.com.cn/weChatTemplete/templete/2016/12/sms_13355789329120161206075117293977168.html
         * epkind : 1
         * ip : 69
         * issuccess : 1
         * lookstatus : 0
         * msgid : 103422
         * resultCode : {"result":"ok","errcode":"0","msgid":"422322156","errmsg":"send by wechat","wx_send_times":"1","sms_send_times":"0"}

         * sendCount : 0
         * sendTimeStr : 2016-12-06 07:51:17
         * sendtime : 1480981877000
         * sid : 577000001
         * smsType : 1
         * startDate : 1480981874000
         * startDateStr : 2016-12-06 07:51:14
         * tel : 13355789329
         * wxSendCount : 1
         */

        public String addTimeStr;
        public long addtime;
        public String content;
        public String detailUrl;
        public int epkind;
        public String ip;
        public int issuccess;
        public int lookstatus;
        public int msgid;
        public String resultCode;
        public int sendCount;
        public String sendTimeStr;
        public long sendtime;
        public String sid;
        public int smsType;
        public long startDate;
        public String startDateStr;
        public String tel;
        public int wxSendCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addTimeStr);
            dest.writeLong(this.addtime);
            dest.writeString(this.content);
            dest.writeString(this.detailUrl);
            dest.writeInt(this.epkind);
            dest.writeString(this.ip);
            dest.writeInt(this.issuccess);
            dest.writeInt(this.lookstatus);
            dest.writeInt(this.msgid);
            dest.writeString(this.resultCode);
            dest.writeInt(this.sendCount);
            dest.writeString(this.sendTimeStr);
            dest.writeLong(this.sendtime);
            dest.writeString(this.sid);
            dest.writeInt(this.smsType);
            dest.writeLong(this.startDate);
            dest.writeString(this.startDateStr);
            dest.writeString(this.tel);
            dest.writeInt(this.wxSendCount);
        }

        public ApSmstasksBean() {
        }

        protected ApSmstasksBean(Parcel in) {
            this.addTimeStr = in.readString();
            this.addtime = in.readLong();
            this.content = in.readString();
            this.detailUrl = in.readString();
            this.epkind = in.readInt();
            this.ip = in.readString();
            this.issuccess = in.readInt();
            this.lookstatus = in.readInt();
            this.msgid = in.readInt();
            this.resultCode = in.readString();
            this.sendCount = in.readInt();
            this.sendTimeStr = in.readString();
            this.sendtime = in.readLong();
            this.sid = in.readString();
            this.smsType = in.readInt();
            this.startDate = in.readLong();
            this.startDateStr = in.readString();
            this.tel = in.readString();
            this.wxSendCount = in.readInt();
        }

        public static final Parcelable.Creator<ApSmstasksBean> CREATOR = new Parcelable.Creator<ApSmstasksBean>() {
            @Override
            public ApSmstasksBean createFromParcel(Parcel source) {
                return new ApSmstasksBean(source);
            }

            @Override
            public ApSmstasksBean[] newArray(int size) {
                return new ApSmstasksBean[size];
            }
        };

        @Override
        public String toString() {
            return "ApSmstasksBean{" +
                    "addTimeStr='" + addTimeStr + '\'' +
                    ", addtime=" + addtime +
                    ", content='" + content + '\'' +
                    ", detailUrl='" + detailUrl + '\'' +
                    ", epkind=" + epkind +
                    ", ip='" + ip + '\'' +
                    ", issuccess=" + issuccess +
                    ", lookstatus=" + lookstatus +
                    ", msgid=" + msgid +
                    ", resultCode='" + resultCode + '\'' +
                    ", sendCount=" + sendCount +
                    ", sendTimeStr='" + sendTimeStr + '\'' +
                    ", sendtime=" + sendtime +
                    ", sid='" + sid + '\'' +
                    ", smsType=" + smsType +
                    ", startDate=" + startDate +
                    ", startDateStr='" + startDateStr + '\'' +
                    ", tel='" + tel + '\'' +
                    ", wxSendCount=" + wxSendCount +
                    '}';
        }
    }

    public static class LowSmstasksBean implements Parcelable {
        /**
         * addTimeStr : 2016-12-06 01:02:49
         * addtime : 1480957369000
         * content : 电子标签电量报警，消息来自126云服务:全部
         * detailUrl : http://www.iesl.com.cn/weChatTemplete/templete/2016/12/sms_13355789329620161206010254-811727824.html
         * epkind : 0
         * ip : 
         * issuccess : 1
         * lookstatus : 0
         * msgid : 103396
         * resultCode : {"result":"ok","errcode":"0","msgid":"422321202","errmsg":"send by wechat","wx_send_times":"1","sms_send_times":"0"}

         * sendCount : 0
         * sendTimeStr : 2016-12-06 01:02:54
         * sendtime : 1480957374000
         * sid : 
         * smsType : 6
         * startDate : 1480957369000
         * startDateStr : 2016-12-06 01:02:49
         * tel : 13355789329
         * wxSendCount : 1
         */

        public String addTimeStr;
        public long addtime;
        public String content;
        public String detailUrl;
        public int epkind;
        public String ip;
        public int issuccess;
        public int lookstatus;
        public int msgid;
        public String resultCode;
        public int sendCount;
        public String sendTimeStr;
        public long sendtime;
        public String sid;
        public int smsType;
        public long startDate;
        public String startDateStr;
        public String tel;
        public int wxSendCount;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addTimeStr);
            dest.writeLong(this.addtime);
            dest.writeString(this.content);
            dest.writeString(this.detailUrl);
            dest.writeInt(this.epkind);
            dest.writeString(this.ip);
            dest.writeInt(this.issuccess);
            dest.writeInt(this.lookstatus);
            dest.writeInt(this.msgid);
            dest.writeString(this.resultCode);
            dest.writeInt(this.sendCount);
            dest.writeString(this.sendTimeStr);
            dest.writeLong(this.sendtime);
            dest.writeString(this.sid);
            dest.writeInt(this.smsType);
            dest.writeLong(this.startDate);
            dest.writeString(this.startDateStr);
            dest.writeString(this.tel);
            dest.writeInt(this.wxSendCount);
        }

        public LowSmstasksBean() {
        }

        protected LowSmstasksBean(Parcel in) {
            this.addTimeStr = in.readString();
            this.addtime = in.readLong();
            this.content = in.readString();
            this.detailUrl = in.readString();
            this.epkind = in.readInt();
            this.ip = in.readString();
            this.issuccess = in.readInt();
            this.lookstatus = in.readInt();
            this.msgid = in.readInt();
            this.resultCode = in.readString();
            this.sendCount = in.readInt();
            this.sendTimeStr = in.readString();
            this.sendtime = in.readLong();
            this.sid = in.readString();
            this.smsType = in.readInt();
            this.startDate = in.readLong();
            this.startDateStr = in.readString();
            this.tel = in.readString();
            this.wxSendCount = in.readInt();
        }

        public static final Parcelable.Creator<LowSmstasksBean> CREATOR = new Parcelable.Creator<LowSmstasksBean>() {
            @Override
            public LowSmstasksBean createFromParcel(Parcel source) {
                return new LowSmstasksBean(source);
            }

            @Override
            public LowSmstasksBean[] newArray(int size) {
                return new LowSmstasksBean[size];
            }
        };

        @Override
        public String toString() {
            return "LowSmstasksBean{" +
                    "addTimeStr='" + addTimeStr + '\'' +
                    ", addtime=" + addtime +
                    ", content='" + content + '\'' +
                    ", detailUrl='" + detailUrl + '\'' +
                    ", epkind=" + epkind +
                    ", ip='" + ip + '\'' +
                    ", issuccess=" + issuccess +
                    ", lookstatus=" + lookstatus +
                    ", msgid=" + msgid +
                    ", resultCode='" + resultCode + '\'' +
                    ", sendCount=" + sendCount +
                    ", sendTimeStr='" + sendTimeStr + '\'' +
                    ", sendtime=" + sendtime +
                    ", sid='" + sid + '\'' +
                    ", smsType=" + smsType +
                    ", startDate=" + startDate +
                    ", startDateStr='" + startDateStr + '\'' +
                    ", tel='" + tel + '\'' +
                    ", wxSendCount=" + wxSendCount +
                    '}';
        }
    }

    public static class NoLoginSmstasksBean implements Parcelable {
        /**
         * addTimeStr : 2015-11-16 04:31:52
         * addtime : 1447619512000
         * content : 累计超时未注册标签共:2个，消息来自126云端:ewj南京苏果张府园
         * detailUrl : 
         * epkind : 0
         * ip : 270001011
         * issuccess : 1
         * lookstatus : 0
         * msgid : 12442
         * resultCode : {"result":"ok","errcode":"0","msgid":"400651956","errmsg":"send by wechat","wx_send_times":"1","sms_send_times":"0"}

         * sendCount : 0
         * sendTimeStr : 2015-11-16 04:31:55
         * sendtime : 1447619515000
         * sid : 270001011
         * smsType : 10
         * startDate : 1447619512000
         * startDateStr : 2015-11-16 04:31:52
         * tel : 13355789329
         * wxSendCount : 1
         */

        public String addTimeStr;
        public long addtime;
        public String content;
        public String detailUrl;
        public int epkind;
        public String ip;
        public int issuccess;
        public int lookstatus;
        public int msgid;
        public String resultCode;
        public int sendCount;
        public String sendTimeStr;
        public long sendtime;
        public String sid;
        public int smsType;
        public long startDate;
        public String startDateStr;
        public String tel;
        public int wxSendCount;

        @Override
        public String toString() {
            return "NoLoginSmstasksBean{" +
                    "addTimeStr='" + addTimeStr + '\'' +
                    ", addtime=" + addtime +
                    ", content='" + content + '\'' +
                    ", detailUrl='" + detailUrl + '\'' +
                    ", epkind=" + epkind +
                    ", ip='" + ip + '\'' +
                    ", issuccess=" + issuccess +
                    ", lookstatus=" + lookstatus +
                    ", msgid=" + msgid +
                    ", resultCode='" + resultCode + '\'' +
                    ", sendCount=" + sendCount +
                    ", sendTimeStr='" + sendTimeStr + '\'' +
                    ", sendtime=" + sendtime +
                    ", sid='" + sid + '\'' +
                    ", smsType=" + smsType +
                    ", startDate=" + startDate +
                    ", startDateStr='" + startDateStr + '\'' +
                    ", tel='" + tel + '\'' +
                    ", wxSendCount=" + wxSendCount +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addTimeStr);
            dest.writeLong(this.addtime);
            dest.writeString(this.content);
            dest.writeString(this.detailUrl);
            dest.writeInt(this.epkind);
            dest.writeString(this.ip);
            dest.writeInt(this.issuccess);
            dest.writeInt(this.lookstatus);
            dest.writeInt(this.msgid);
            dest.writeString(this.resultCode);
            dest.writeInt(this.sendCount);
            dest.writeString(this.sendTimeStr);
            dest.writeLong(this.sendtime);
            dest.writeString(this.sid);
            dest.writeInt(this.smsType);
            dest.writeLong(this.startDate);
            dest.writeString(this.startDateStr);
            dest.writeString(this.tel);
            dest.writeInt(this.wxSendCount);
        }

        public NoLoginSmstasksBean() {
        }

        protected NoLoginSmstasksBean(Parcel in) {
            this.addTimeStr = in.readString();
            this.addtime = in.readLong();
            this.content = in.readString();
            this.detailUrl = in.readString();
            this.epkind = in.readInt();
            this.ip = in.readString();
            this.issuccess = in.readInt();
            this.lookstatus = in.readInt();
            this.msgid = in.readInt();
            this.resultCode = in.readString();
            this.sendCount = in.readInt();
            this.sendTimeStr = in.readString();
            this.sendtime = in.readLong();
            this.sid = in.readString();
            this.smsType = in.readInt();
            this.startDate = in.readLong();
            this.startDateStr = in.readString();
            this.tel = in.readString();
            this.wxSendCount = in.readInt();
        }

        public static final Parcelable.Creator<NoLoginSmstasksBean> CREATOR = new Parcelable.Creator<NoLoginSmstasksBean>() {
            @Override
            public NoLoginSmstasksBean createFromParcel(Parcel source) {
                return new NoLoginSmstasksBean(source);
            }

            @Override
            public NoLoginSmstasksBean[] newArray(int size) {
                return new NoLoginSmstasksBean[size];
            }
        };
    }

    public static class NoUptSmstasksBean implements Parcelable {
        /**
         * addTimeStr : 2016-02-17 09:33:30
         * addtime : 1455672810000
         * content : 电子标签变价失败[ED:0.0.16.90],故障已经排除，消息来自126云端:ewj西安V+
         * detailUrl : 
         * epkind : 1
         * ip : 0.0.16.90
         * issuccess : 1
         * lookstatus : 0
         * msgid : 30011
         * resultCode : {"result":"ok","errcode":"0","msgid":"402943904","errmsg":"send by wechat","wx_send_times":"1","sms_send_times":"0"}

         * sendCount : 0
         * sendTimeStr : 2016-02-17 09:33:30
         * sendtime : 1455672810000
         * sid : 290000001
         * smsType : 7
         * startDate : 1455672810000
         * startDateStr : 2016-02-17 09:33:30
         * tel : 13355789329
         * wxSendCount : 1
         */

        public String addTimeStr;
        public long addtime;
        public String content;
        public String detailUrl;
        public int epkind;
        public String ip;
        public int issuccess;
        public int lookstatus;
        public int msgid;
        public String resultCode;
        public int sendCount;
        public String sendTimeStr;
        public long sendtime;
        public String sid;
        public int smsType;
        public long startDate;
        public String startDateStr;
        public String tel;
        public int wxSendCount;


        @Override
        public String toString() {
            return "NoUptSmstasksBean{" +
                    "addTimeStr='" + addTimeStr + '\'' +
                    ", addtime=" + addtime +
                    ", content='" + content + '\'' +
                    ", detailUrl='" + detailUrl + '\'' +
                    ", epkind=" + epkind +
                    ", ip='" + ip + '\'' +
                    ", issuccess=" + issuccess +
                    ", lookstatus=" + lookstatus +
                    ", msgid=" + msgid +
                    ", resultCode='" + resultCode + '\'' +
                    ", sendCount=" + sendCount +
                    ", sendTimeStr='" + sendTimeStr + '\'' +
                    ", sendtime=" + sendtime +
                    ", sid='" + sid + '\'' +
                    ", smsType=" + smsType +
                    ", startDate=" + startDate +
                    ", startDateStr='" + startDateStr + '\'' +
                    ", tel='" + tel + '\'' +
                    ", wxSendCount=" + wxSendCount +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addTimeStr);
            dest.writeLong(this.addtime);
            dest.writeString(this.content);
            dest.writeString(this.detailUrl);
            dest.writeInt(this.epkind);
            dest.writeString(this.ip);
            dest.writeInt(this.issuccess);
            dest.writeInt(this.lookstatus);
            dest.writeInt(this.msgid);
            dest.writeString(this.resultCode);
            dest.writeInt(this.sendCount);
            dest.writeString(this.sendTimeStr);
            dest.writeLong(this.sendtime);
            dest.writeString(this.sid);
            dest.writeInt(this.smsType);
            dest.writeLong(this.startDate);
            dest.writeString(this.startDateStr);
            dest.writeString(this.tel);
            dest.writeInt(this.wxSendCount);
        }

        public NoUptSmstasksBean() {
        }

        protected NoUptSmstasksBean(Parcel in) {
            this.addTimeStr = in.readString();
            this.addtime = in.readLong();
            this.content = in.readString();
            this.detailUrl = in.readString();
            this.epkind = in.readInt();
            this.ip = in.readString();
            this.issuccess = in.readInt();
            this.lookstatus = in.readInt();
            this.msgid = in.readInt();
            this.resultCode = in.readString();
            this.sendCount = in.readInt();
            this.sendTimeStr = in.readString();
            this.sendtime = in.readLong();
            this.sid = in.readString();
            this.smsType = in.readInt();
            this.startDate = in.readLong();
            this.startDateStr = in.readString();
            this.tel = in.readString();
            this.wxSendCount = in.readInt();
        }

        public static final Parcelable.Creator<NoUptSmstasksBean> CREATOR = new Parcelable.Creator<NoUptSmstasksBean>() {
            @Override
            public NoUptSmstasksBean createFromParcel(Parcel source) {
                return new NoUptSmstasksBean(source);
            }

            @Override
            public NoUptSmstasksBean[] newArray(int size) {
                return new NoUptSmstasksBean[size];
            }
        };
    }

    public static class OtherSmstasksBean implements Parcelable {
        /**
         * addTimeStr : 2016-12-06 07:51:14
         * addtime : 1480981874000
         * content : AP和SERVER链接中断[APADDR:69],故障已经排除，消息来自126云服务:乐木几温州南站店
         * detailUrl : http://www.iesl.com.cn/weChatTemplete/templete/2016/12/sms_13355789329120161206075117293977168.html
         * epkind : 1
         * ip : 69
         * issuccess : 1
         * lookstatus : 0
         * msgid : 103422
         * resultCode : {"result":"ok","errcode":"0","msgid":"422322156","errmsg":"send by wechat","wx_send_times":"1","sms_send_times":"0"}

         * sendCount : 0
         * sendTimeStr : 2016-12-06 07:51:17
         * sendtime : 1480981877000
         * sid : 577000001
         * smsType : 1
         * startDate : 1480981874000
         * startDateStr : 2016-12-06 07:51:14
         * tel : 13355789329
         * wxSendCount : 1
         */

        public String addTimeStr;
        public long addtime;
        public String content;
        public String detailUrl;
        public int epkind;
        public String ip;
        public int issuccess;
        public int lookstatus;
        public int msgid;
        public String resultCode;
        public int sendCount;
        public String sendTimeStr;
        public long sendtime;
        public String sid;
        public int smsType;
        public long startDate;
        public String startDateStr;
        public String tel;
        public int wxSendCount;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.addTimeStr);
            dest.writeLong(this.addtime);
            dest.writeString(this.content);
            dest.writeString(this.detailUrl);
            dest.writeInt(this.epkind);
            dest.writeString(this.ip);
            dest.writeInt(this.issuccess);
            dest.writeInt(this.lookstatus);
            dest.writeInt(this.msgid);
            dest.writeString(this.resultCode);
            dest.writeInt(this.sendCount);
            dest.writeString(this.sendTimeStr);
            dest.writeLong(this.sendtime);
            dest.writeString(this.sid);
            dest.writeInt(this.smsType);
            dest.writeLong(this.startDate);
            dest.writeString(this.startDateStr);
            dest.writeString(this.tel);
            dest.writeInt(this.wxSendCount);
        }

        public OtherSmstasksBean() {
        }

        protected OtherSmstasksBean(Parcel in) {
            this.addTimeStr = in.readString();
            this.addtime = in.readLong();
            this.content = in.readString();
            this.detailUrl = in.readString();
            this.epkind = in.readInt();
            this.ip = in.readString();
            this.issuccess = in.readInt();
            this.lookstatus = in.readInt();
            this.msgid = in.readInt();
            this.resultCode = in.readString();
            this.sendCount = in.readInt();
            this.sendTimeStr = in.readString();
            this.sendtime = in.readLong();
            this.sid = in.readString();
            this.smsType = in.readInt();
            this.startDate = in.readLong();
            this.startDateStr = in.readString();
            this.tel = in.readString();
            this.wxSendCount = in.readInt();
        }

        public static final Parcelable.Creator<OtherSmstasksBean> CREATOR = new Parcelable.Creator<OtherSmstasksBean>() {
            @Override
            public OtherSmstasksBean createFromParcel(Parcel source) {
                return new OtherSmstasksBean(source);
            }

            @Override
            public OtherSmstasksBean[] newArray(int size) {
                return new OtherSmstasksBean[size];
            }
        };

        @Override
        public String toString() {
            return "OtherSmstasksBean{" +
                    "addTimeStr='" + addTimeStr + '\'' +
                    ", addtime=" + addtime +
                    ", content='" + content + '\'' +
                    ", detailUrl='" + detailUrl + '\'' +
                    ", epkind=" + epkind +
                    ", ip='" + ip + '\'' +
                    ", issuccess=" + issuccess +
                    ", lookstatus=" + lookstatus +
                    ", msgid=" + msgid +
                    ", resultCode='" + resultCode + '\'' +
                    ", sendCount=" + sendCount +
                    ", sendTimeStr='" + sendTimeStr + '\'' +
                    ", sendtime=" + sendtime +
                    ", sid='" + sid + '\'' +
                    ", smsType=" + smsType +
                    ", startDate=" + startDate +
                    ", startDateStr='" + startDateStr + '\'' +
                    ", tel='" + tel + '\'' +
                    ", wxSendCount=" + wxSendCount +
                    '}';
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.apRecord);
        dest.writeParcelable(this.apSmstasks, flags);
        dest.writeInt(this.lowBatteryRecord);
        dest.writeParcelable(this.lowSmstasks, flags);
        dest.writeInt(this.noLoginSmstaskRecord);
        dest.writeParcelable(this.noLoginSmstasks, flags);
        dest.writeInt(this.noUptPriceRecord);
        dest.writeParcelable(this.noUptSmstasks, flags);
        dest.writeParcelable(this.otherSmstasks, flags);
        dest.writeInt(this.otherSmstasksRecord);
    }

    public SmsTaskBean() {
    }

    protected SmsTaskBean(Parcel in) {
        this.apRecord = in.readInt();
        this.apSmstasks = in.readParcelable(ApSmstasksBean.class.getClassLoader());
        this.lowBatteryRecord = in.readInt();
        this.lowSmstasks = in.readParcelable(LowSmstasksBean.class.getClassLoader());
        this.noLoginSmstaskRecord = in.readInt();
        this.noLoginSmstasks = in.readParcelable(NoLoginSmstasksBean.class.getClassLoader());
        this.noUptPriceRecord = in.readInt();
        this.noUptSmstasks = in.readParcelable(NoUptSmstasksBean.class.getClassLoader());
        this.otherSmstasks = in.readParcelable(OtherSmstasksBean.class.getClassLoader());
        this.otherSmstasksRecord = in.readInt();
    }

    public static final Parcelable.Creator<SmsTaskBean> CREATOR = new Parcelable.Creator<SmsTaskBean>() {
        @Override
        public SmsTaskBean createFromParcel(Parcel source) {
            return new SmsTaskBean(source);
        }

        @Override
        public SmsTaskBean[] newArray(int size) {
            return new SmsTaskBean[size];
        }
    };
}
