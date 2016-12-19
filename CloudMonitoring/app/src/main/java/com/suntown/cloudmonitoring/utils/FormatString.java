package com.suntown.cloudmonitoring.utils;

/**
 * Created by Administrator on 2016/12/2.
 */

public class FormatString {
    public static String fromTinyip(String message) {
        String string = message.replace(".", "");
        //0000BCC4
        String s1 = string.substring(0,2);
        String s2 = string.substring(2,4);
        String s3 = string.substring(4,6);
        String s4 = string.substring(6,8);
        int i1 = Integer.parseInt(s1, 16);
        int i2 = Integer.parseInt(s2, 16);
        int i3 = Integer.parseInt(s3, 16);
        int i4 = Integer.parseInt(s4, 16);
        return i1+"."+i2+"."+i3+"."+i4;
    }
}
