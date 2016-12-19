package com.suntown.scannerproject.utils;

/**
 * Created by Administrator on 2016/11/28.
 */
public class Constant {
    public static final String USER_ID = "userId";
    public static final String PDNO = "pdno";
    public static final String SID = "sid";
    public static final String XML = "xml";
    public static final String SERVER_IP = "serverIp";
    public static final String SUBSERVER_IP = "subServerIp";
    public static final String INFO_BEAN = "info_bean";
    public static final String PERSON = "person";
    public static final String SHOP_NAME = "shop_name";
    public static final String BARCODE = "barcode";
    public static final String PASS_WORD = "pass_word";
    public static final String IN_NUM = "in_num";
    public static final String OUT_NUM = "out_num";
    public static final String USER_CODE = "usercode";
    public static final String PSWD = "pswd";
    public static final String ITEM = "item";
    public static final String TINYIP = "tinyip";
    public static final String SHELFGOODS = "shelf_goods";
    public static final String NUM = "num";
    public static final String INOUTBEAN_LIST = "in_out_bean_list";
    public static final String STRING = "string";
    public static final String DEVICE_NUM = "device_num";
    public static final String ARG1 = "arg1";
    public static final String ARG0 = "arg0";


    public static String formatBASE_HOST(String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return "http://" + url;
        }
    }
}
