package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/11/9.
 */

public class MessageBean {


    /**
     * addTimeStr : 2016-06-07 08:30:00
     * addtime : 1465259400000
     * content : 变价失败标签，消息来自公司服务:全部
     * detailUrl : http://www.iesl.com.cn/weChatTemplete/templete/2016/11/sms_15658039371620161103010121-1628806080.html
     * epkind : 0
     * ip : 
     * issuccess : 1
     * lookstatus : 1
     * msgid : 17009
     * resultCode : {"result":"ok","errcode":"0","msgid":"412452675","errmsg":"send by wechat","wx_send_times":"1","sms_send_times":"0"}

     * sendCount : 0
     * sendTimeStr : 2016-06-07 08:30:28
     * sendtime : 1465259428000
     * sid : 
     * smsType : 7
     * startDate : 1465259400000
     * startDateStr : 2016-06-07 08:30:00
     * tel : 18626192876
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
        return "MessageBean{" +
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
