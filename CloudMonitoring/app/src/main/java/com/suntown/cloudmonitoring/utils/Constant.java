package com.suntown.cloudmonitoring.utils;

/**
 * Created by Administrator on 2016/9/13.
 */
public class Constant {
    public static final String USER_NAME = "username";
    public static final String PASS_WORD = "password";
    public static final String GUIDED = "guided";
    public static final String USER_ID = "userid";
    public static final String SERVER_IP = "server_ip";
    public static final String MODNAME = "modname";
    public static final String SNAME = "sname";
    public static final String STRING_DATA = "Strig_data";
    public static final String SID = "sid";
    public static final String INFO_BEAN = "info_bean";
    public static final String USER_ID_MAIN = "uer_id_main";
    public static final String TINYIP = "tinyip";
    public static final String DATERANGE = "dateRange";
    public static final String SUBSERVER_IP = "subServerIp";
    public static final String SUB_USER_ID = "sub_user_id";
    public static final String BARCODE = "Barcode";
    public static final String SHOP_BEAN = "shop_bean";
    public static final String LOOKSTATUS = "lookstatus";
    public static final String XML = "xml";
    public static final String DBNAME = "CloudMon.db";
    public static final String SHOP_NUM = "shop_num";
    public static final String SHOP_NAME = "shop_name";
    public static final String SHELFGOODS = "shelfGoods";
    public static final String REGISTER = "register";
    public static final String ORIMATYPE = "oriMaptype";
    public static final String GOODS = "goods";
    public static final String SFID = "sfid";
    public static final String ESL = "sel";
    public static final String AP = "ap";
    public static final String ESLUPTASKS = "eslUptTasks";
    public static final String ESLMODEL = "eslModel";
    public static final String RESULT_CODE = "result";
    public static final String IS_ON_SCANN = "isOneScanner";
    public static final String SCANN_DATAS = "scanner_datas";
    public static final int RESULT_OK =1;
    public static final String MESSAGE = "message";
    public static final String INOUTBEAN = "inOutBean";
    public static final String INOUTBEAN_LIST = "inOutBeanList";
    public static final String NUM = "num";
    public static final String STRING = "string";
    public static final String PDNO = "pdno";
    public static final String PERSON = "person";
    public static final String SMSTYPE = "smstype";
    public static final String PAGE_NUM = "pageNum";
    public static final String NUM_PER_PAGE = "numPerPage";
    public static final String WEB_URL = "web_url";
    public static final String ID = "id";
    public static final String SENDBEAN = "sendBean";
    public static final String NAME = "name";
    public static final String SHOPNAME = "shopName";
    public static final String RECORD = "record";
    public static final String REGISTRATION_ID = "regid";
    public static final String SCANN_TYPE = "scanning_type";
    public static final String IP = "ip";
    public static final String IN_NUM = "in_num";
    public static final String OUT_NUM = "out_num";
    public static final String CHECK_NUM = "check_num";
    public static final String ARG1 = "arg1";
    public static final String ARG0 = "arg0";
    public static final String PIC_PATH = "path";
    public static String All_RECORD = "record";
    public static final String JMPSW = "suntown";


    public static String formatBASE_HOST(String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return "http://" + url;
        }
    }

}
