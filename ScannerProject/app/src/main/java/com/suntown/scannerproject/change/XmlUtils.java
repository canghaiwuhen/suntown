package com.suntown.scannerproject.change;

import android.content.Context;
import android.telephony.TelephonyManager;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2016/12/1.
 */

public class XmlUtils {
    public static String String2Xml( String oldTinyip, String tinyip, String sid, String userId,String deviceNum){
        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        String format = dateFormater.format(date);
        String xml = "<LabCHG><oldIP>"+oldTinyip+"</oldIP><NewIP>"+tinyip+"</NewIP> <ScanDate>"+format+"</ScanDate><UID>"+userId+"</UID><SID>"+sid+"</SID><SCANID>"+deviceNum+"</SCANID></LabCHG>";
        return xml;
    }
}
