package com.suntown.smartscreen.api;

import android.os.Environment;


public class  ApiConstant {
    // 文件位置
    public static String SAVE_FILE_PATH = Environment.getExternalStorageDirectory()
            .toString() + "/suntown/merchant";
    public static String BASE_URL = "http://www.smartesl.com.cn/axis2/services/STEslMobileService2/";
//    http://www.iesl.com.cn/axis2/services/STEslMobileService2/
    public static String formatBASE_HOST(String url){
        if(url.startsWith("http")){
            return url;
        }else{
            return "http://" + url;
        }
    }
}
