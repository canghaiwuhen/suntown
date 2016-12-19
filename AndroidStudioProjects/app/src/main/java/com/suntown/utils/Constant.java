package com.suntown.utils;


/**
 * Created by Administrator on 2016/7/29.
 */
public class Constant {
    public static final String HOST="http://www.smartesl.com.cn/axis2/services/sunteslwebservice/";
    public static final String BASE_HOST="http://www.smartesl.com.cn/axis2/services/SUNTOKWEBSERVICE/";
    public static final String QUERY_HOST="http://www.suntowngis.com:8080/axis2/services/sunteslwebservice/";
    //获取alipay信息
    public static final String TEST_HOST="http://www.iesl.com.cn/axis2/services/sunteslwebservice/";
    public static final String USER_NAME="userNum";
    public static final String LOGIN_USER_NAME = "login_user_name";
    public static final String LOGIN_TOKEN = "logintoken";
    public static final String SAX = "sax";
    public static final String WIFI_PASSWORD = "wifi_password";
    public static final String PASSWORD="password";
    public static final String MEMID ="memid";
    public static final String LOGIN_PWD="login_pwd";
    public static final String NICKNAME="nick_name";
    public static final String WIFI_SSID="wifi_ssid";
    public static final String MAC="mac";
    public static final String MODULE_IP = "moduleIP";
    public static final String TAG_IP = "tag_ip";
    public static final String SERVER_IP = "server_ip";
    public static final String SERVER_PORTNO = "server_portno";
    public static final String TAG_NAME = "tag_name";
    public static final String BSSID = "bssid";
    public static final String ADDRESS = "address";
    public static final String ALIPAYSFID = "Alipaysfid";
    public static final String ALIPAYSFZH = "Alipaysfzh";
    public static final String ALIPAYSFRSA = "Alipaysfrsa";
    public static final String REGISTRATION_ID = "registrationID";
    public static final String PASSWORD_MD5 = "password_md5";
    public static final String SEX = "sex";
    public static final String IS_OFF = "is_off";
    public static final String GUIDED = "guided";
    public static String SERIAL_NUM ="serial_num";

    //type
    public static final int SETUP_SVR_INFOR=0x00;
    public static final int SETUP_SVR_INFOR_ACK=0x01;
    public static final int SETUP_TAGID=0x02;
    public static final int SETUP_TAGID_ACK=0x03;
    public static final int UPLOAD_CLICK_INFOR=0x05;
    public static final int UPLOAD_CLICK_INFOR_ACK=0x06;
    public static final int GET_TAGINFO=0x08;
    public static final int GET_TAGINFO_ACK=0x09;
    public static final int HIBERNATE_TAG=0x0A;
    public static final int HIBERNATE_TAG_ACK=0x0B;
    public static final int PORT = 8899;
    public static String PUSH_NUM = "JpushNum";
    public static String NAME = "name";
    public static String IS_DEVICE_LIST = "device_list";
    public static String GOODS_NAME = "goods_name";


    public static String format(String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return HOST + url;
        }
    }
    public static String formatBASE_HOST(String url){
        if(url.startsWith("http")){
                return url;
            }else{
                return BASE_HOST + url;
            }
        }
    public static String formatTEST_HOST(String url){
        if(url.startsWith("http")){
                return url;
            }else{
                return TEST_HOST + url;
            }
        }
    public static String formatImage(String url){
        if(url.startsWith("http")){
                return url;
            }else{
                return "http://" + url;
            }
        }


}
