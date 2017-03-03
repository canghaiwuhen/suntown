package com.suntown.cloudmonitoring.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by Administrator on 2017/1/16.
 */

public class Sort {
    //demo排序需要文本消息类型.意思是发送方创建文本消息,接收方进行排序
    public static List<Message> orderMessage(StringBuilder sb, List<Message> allMessage) {
        for (Message msgList : allMessage) {
            long time = msgList.getCreateTime();
            Date date = new Date(time);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            String createTime = simpleDateFormat.format(date);
            sb.append("消息ID = " + msgList.getId());
            sb.append("~~~消息类型 = " + msgList.getContentType());
            sb.append("~~~创建时间 = " + createTime);
            sb.append("\n");
        }
        return allMessage;
    }
}
