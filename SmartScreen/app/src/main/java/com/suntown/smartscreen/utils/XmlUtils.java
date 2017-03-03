package com.suntown.smartscreen.utils;

import android.util.Log;

import com.suntown.smartscreen.data.AllTaskBean;
import com.suntown.smartscreen.data.DmListBean;
import com.suntown.smartscreen.data.GoodsInfo;
import com.suntown.smartscreen.maintain.allocation.SubBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class XmlUtils {

//    <DATALIST>
//    <APPINFO>
//    <USERID>suntown</USERID>
//    <PRICETYPE>0</PRICETYPE>
//    <BEGTIME>2017-02-10 14:39:08</BEGTIME>
//    <SID>571002002</SID>
//    <ENDTIME>2017-02-10 14:39:08</ENDTIME>
//    </APPINFO>
//    <DATA>
//    <MEMPRICE>10</MEMPRICE> 会员价
//    <BARCODE>026102251959</BARCODE>
//    <TINYIP>0.0.136.178</TINYIP>
//    <NEWPRICE>5.7</NEWPRICE> 现价
//    <ORIPRICE>0</ORIPRICE> 原价
//    </DATA>
//    <DATA>
//    <MEMPRICE>18</MEMPRICE>
//    <BARCODE>026102251904</BARCODE>
//    <TINYIP>0.0.56.73</TINYIP>
//    <NEWPRICE>7.1</NEWPRICE>
//    <ORIPRICE>0</ORIPRICE>
//    </DATA>
//    </DATALIST>
    public static String List2Xml(ArrayList<GoodsInfo.RECORDBean> recordBeanList,String userid,String sid) {
        GoodsInfo.RECORDBean recordBean = recordBeanList.get(0);
        String type = recordBean.TYPE;
        String xml = "<DATALIST><APPINFO><USERID>";
        xml+=userid+"</USERID><PRICETYPE>";
        xml+=type+"</PRICETYPE><BEGTIME>";
        xml+=recordBean.STARTTIME+"</BEGTIME><SID>";
        xml+=sid+"</SID><ENDTIME>";
        xml+=recordBean.ENDTIME+"</ENDTIME></APPINFO>";
        for (GoodsInfo.RECORDBean bean : recordBeanList) {
                //调整会员价
            if (type.equals("0")) {
                xml+="<DATA><MEMPRICE>"+bean.VIP+"</MEMPRICE>";
                xml+="<BARCODE>"+bean.BARCODE+"</BARCODE>";
                xml+="<TINYIP>"+bean.TINYIP+"</TINYIP>";
                xml+="<NEWPRICE>"+bean.CURPRICE+"</NEWPRICE>";
                xml+="<ORIPRICE>"+bean.COSTPRICE+"</ORIPRICE></DATA>";
                //调整促销 会员价传null NEWPRICE改变
            }else {
                xml+="<DATA><MEMPRICE></MEMPRICE>";
                xml+="<BARCODE>"+bean.BARCODE+"</BARCODE>";
                xml+="<TINYIP>"+bean.TINYIP+"</TINYIP>";
                xml+="<NEWPRICE>"+bean.VIP+"</NEWPRICE>";
                xml+="<ORIPRICE>"+bean.COSTPRICE+"</ORIPRICE></DATA>";
                //调整现价  会员价传null NEWPRICE改变
            }
        }
        xml+="</DATALIST>";
       return xml;
    }
//    <DATA>
//        <DMCODE>142</DMCODE>
//        <GOODSDISPM>
//          <VERB>INSERT</VERB>
//          <SID>571002002</SID>
//        </GOODSDISPM>
//        <GOODSDISPM>
//          <VERB>DELETE</VERB>
//          <SID>571003001</SID>
//        </GOODSDISPM>
//    </DATA>
    public static String SubBean2Xml(String dmCode, ArrayList<SubBean> sidList){
        String xml ="<DATA><DMCODE>"+dmCode+"</DMCODE>";
        for (SubBean subBean : sidList) {
            String sid = subBean.sid;
            if (!"".equals(sid)&&null!=sid){
                xml+="<GOODSDISPM><VERB>";
//            1 删除  0 插入
                int delete = subBean.delete;
                xml+=delete==0?"INSERT":"DELETE";
                xml+="</VERB><SID>"+ sid +"</SID></GOODSDISPM>";
            }
        }
        xml+="</DATA>";
        return xml;
    }


    //<DATA>
    //<SID>142</SID>
    //<GOODSDISPM><DMCODE>142</DMCODE></GOODSDISPM>
    //<GOODSDISPM><DMCODE>143</DMCODE></GOODSDISPM>
    //</DATA>
    public static String allTaskBean2Xml(String sid, List<AllTaskBean.RECORDBean> list, HashMap<String, List<DmListBean.RECORDBean>> listHashMap) {
        String xml = "<DATA><SID>"+sid+"</SID>";
        for (AllTaskBean.RECORDBean recordBean : list) {
            for (DmListBean.RECORDBean bean : listHashMap.get(recordBean.TYPENAME)) {
                if (bean.ISCHECHED) {
                    String dmcode = bean.DMCODE;
                    xml+="<GOODSDISPM><DMCODE>"+dmcode+"</DMCODE></GOODSDISPM>";
                }
            }
        }
        xml+="</DATA>";
        return xml;
    }
}
