package com.suntown.cloudmonitoring.utils;

import com.suntown.cloudmonitoring.bean.PDBean;
import com.suntown.cloudmonitoring.bean.inputBean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class XmlUtils {
//    <?xml version="1.0" encoding="utf-8"?>
//    <GOODS_IN>
//    <GOODS_IN_LIST>
//    <PKG>1</PKG>
//    <BARCODE>6901424334228</BARCODE>
//    <PKGNUM>0</PKGNUM>
//    <INNUM>1</INNUM>
//    <PDate>2016-11-02 14:07</PDate>
//    </GOODS_IN_LIST>
//    <GOODS_IN_LIST>
//    <PKG>1</PKG>
//    <BARCODE>6902083882099</BARCODE>
//    <PKGNUM>0</PKGNUM>
//    <INNUM>1</INNUM>
//    <PDate>2016-11-02 14:07</PDate>
//    </GOODS_IN_LIST>
//    <GOODS_INS>
//    <INDATE>2016-11-02 14:07</INDATE>
//    <INUSER>suntown</INUSER>
//    <SHEETNO>0000275</SHEETNO>
//    <SID>571002002</SID>
//    <PASSDATE>2016-11-02 14:07</PASSDATE>
//    <PASSKIND>3</PASSKIND>
//    <PASSUSER>suntown</PASSUSER>
//    </GOODS_INS>
//    <GOODS_INS>
//    <INDATE>2016-11-02 14:07</INDATE>
//    <INUSER>suntown</INUSER>
//    <SHEETNO>0000275</SHEETNO>
//    <SID>571002002</SID>
//    <PASSDATE>2016-11-02 14:07</PASSDATE>
//    <PASSKIND>3</PASSKIND>
//    <PASSUSER>suntown</PASSUSER>
//    </GOODS_INS>
//    </GOODS_IN>
    public static String List2Xml(List<inputBean> inputBeanList,String userId,String oddNum,String sid) {
        String time = Utils.Time();
        String xml = "<GOODS_IN>";
        for (inputBean inputBean : inputBeanList) {
            String barcode = inputBean.Barcode;
            String num = inputBean.num;
            String date = inputBean.Date;
            String boxNum = inputBean.boxNum;
            String gname = inputBean.Gname;
            xml+="<GOODS_IN_LIST>";
            xml+=" <PKG>"+boxNum+"</PKG>";
            xml+="<BARCODE>"+barcode+"</BARCODE>";
            xml+="<PKGNUM>"+0+"</PKGNUM>";
            xml+="<INNUM>"+num+"</INNUM>";
            xml+="<PDate>"+date+"</PDate></GOODS_IN_LIST>";
        }
        for (inputBean inputBean : inputBeanList) {
            String date = inputBean.Date;
            xml+="<GOODS_INS>";
            xml+="<INUSER>"+userId+"</INUSER>";
            xml+="<SHEETNO>"+oddNum+"</SHEETNO>";
            xml+="<SID>"+sid+"</SID>";
            xml+="<PASSDATE>"+time+"</PASSDATE>";
            xml+="<PASSKIND>"+3+"</PASSKIND>";
            xml+="<PASSUSER>"+userId+"</PASSUSER></GOODS_INS>";
        }
        xml+="</GOODS_IN>";
        return xml;
    }
    public static String ListXml(List<inputBean> inputBeanList,String userId,String oddNum,String sid) {
        String time = Utils.Time();
        String xml = "<GOODS_IN>";
        for (inputBean inputBean : inputBeanList) {
            String barcode = inputBean.Barcode;
            String num = inputBean.num;
            String date = inputBean.Date;
            String boxNum = inputBean.boxNum;
            String gname = inputBean.Gname;
            xml+="<GOODS_OUT_LIST>";
            xml+=" <PKG>"+boxNum+"</PKG>";
            xml+="<BARCODE>"+barcode+"</BARCODE>";
            xml+="<PKGNUM>"+0+"</PKGNUM>";
            xml+="<OUTNUM>"+num+"</OUTNUM>";
            xml+="<PDate>"+date+"</PDate></GOODS_OUT_LIST>";
        }
        for (inputBean inputBean : inputBeanList) {
            String date = inputBean.Date;
            xml+="<GOODS_OUTS>";
            xml+="<OUTUSER>"+userId+"</OUTUSER>";
            xml+="<SHEETNO>"+oddNum+"</SHEETNO>";
            xml+="<SID>"+sid+"</SID>";
            xml+="<PASSDATE>"+time+"</PASSDATE>";
            xml+="<PASSKIND>"+3+"</PASSKIND>";
            xml+="<PASSUSER>"+userId+"</PASSUSER></GOODS_OUTS>";
        }
        xml+="</GOODS_OUT>";
        return xml;
    }


//        <?xml version="1.0" encoding="utf-8"?>
//    <GOODS_OUT>
//    <GOODS_OUT_LIST>
//    <PKG>1</PKG>
//    <BARCODE>6901424334228</BARCODE>
//    <PKGNUM>0</PKGNUM>
//    <OUTNUM>1</OUTNUM>
//    <PDate>2016-11-02 14:07</PDate>
//    </GOODS_OUT_LIST>
//    <GOODS_OUT_LIST>
//    <PKG>1</PKG>
//    <BARCODE>6902083882099</BARCODE>
//    <PKGNUM>0</PKGNUM>
//    <OUTNUM>1</OUTNUM>
//    <PDate>2016-11-02 14:07</PDate>
//    </GOODS_OUT_LIST>
//    <GOODS_OUTS>
//    <OUTDATE>2016-11-02 14:07</OUTDATE>
//    <OUTUSER>suntown</OUTUSER>
//    <SHEETNO>0000275</SHEETNO>
//    <SID>571002002</SID>
//    <PASSDATE>2016-11-02 14:07</PASSDATE>
//    <PASSKIND>3</PASSKIND>
//    <PASSUSER>suntown</PASSUSER>
//    </GOODS_OUTS>
//    <GOODS_OUTS>
//    <OUTDATE>2016-11-02 14:07</OUTDATE>
//    <OUTUSER>suntown</OUTUSER>
//    <SHEETNO>0000275</SHEETNO>
//    <SID>571002002</SID>
//    <PASSDATE>2016-11-02 14:07</PASSDATE>
//    <PASSKIND>3</PASSKIND>
//    <PASSUSER>suntown</PASSUSER>
//    </GOODS_OUTS>
//    </GOODS_OUT>
    public static String List2Xml2(List<inputBean> inputBeanList, String userId, String oddNum, String sid) {
        String time = Utils.Time();
        String xml ="<GOODS_OUT>";
        for (inputBean inputBean : inputBeanList) {
            String barcode = inputBean.Barcode;
            String num = inputBean.num;
            String date = inputBean.Date;
            String boxNum = inputBean.boxNum;
            String gname = inputBean.Gname;
            xml+="<GOODS_OUT_LIST>";
            xml+=" <PKG>"+boxNum+"</PKG>";
            xml+="<BARCODE>"+barcode+"</BARCODE>";
            xml+="<PKGNUM>"+0+"</PKGNUM>";
            xml+="<OUTNUM>"+num+"</OUTNUM>";
            xml+="<PDate>"+date+"</PDate></GOODS_OUT_LIST>";
        }
        for (inputBean inputBean : inputBeanList) {
            String date = inputBean.Date;
            xml+="<GOODS_OUTS>";
            xml+="<OUTUSER>"+userId+"</OUTUSER>";
            xml+="<SHEETNO>"+oddNum+"</SHEETNO>";
            xml+="<SID>"+sid+"</SID>";
            xml+="<PASSDATE>"+time+"</PASSDATE>";
            xml+="<PASSKIND>"+3+"</PASSKIND>";
            xml+="<PASSUSER>"+userId+"</PASSUSER></GOODS_OUTS>";
        }
        xml+="</GOODS_OUT>";
        return xml;
    }
//    <SHOP_GOODS_PD_LIST>
//        <SHOP_GOODS_PD_LISTS>
//            <PDNUM_CK>20</PDNUM_CK>
//            <BARNAME>雀巢优活饮用水330毫升</BARNAME>
//            <PDNUM>10</PDNUM>
//            <D4>1</D4>
//            <SID>571002002</SID>
//            <BARCODE>6918976333388</BARCODE>
//            <SPDID>133264</SPDID>
//            <D7>0</D7>
//            <PDID>466</PDID>
//            <PDNO>20161103093321</PDNO>
//        </SHOP_GOODS_PD_LISTS>
//    </SHOP_GOODS_PD_LIST>
    //PDNUM_CK 仓库数量  PDNUM 卖场数量
    public static String PDList2XML(List<PDBean> pdBeanList, String sid) {
        String xml = "<SHOP_GOODS_PD_LIST>";
        for (PDBean pdBean : pdBeanList) {
            String storeNum = pdBean.storeNum;
            String martNum = pdBean.martNum;
            String goodsname = pdBean.GOODSNAME;
            String d4 = pdBean.D4;
            String d7 = pdBean.D7;
            String barcode = pdBean.BARCODE;
            String spdid = pdBean.SPDID;
            String pdid = pdBean.PDID;
            String pdno = pdBean.PDNO;
            xml+="<SHOP_GOODS_PD_LISTS><PDNUM_CK>"+storeNum+"</PDNUM_CK>";
            xml+="<BARNAME>"+goodsname+"</BARNAME>";
            xml+="<PDNUM>"+martNum+"</PDNUM>";
            xml+="<D4>"+d4+"</D4>";
            xml+="<SID>"+sid+"</SID>";
            xml+="<BARCODE>"+barcode+"</BARCODE>";
            xml+="<SPDID>"+spdid+"</SPDID>";
            xml+="<D7>"+d7+"</D7>";
            xml+="<PDID>"+pdid+"</PDID>";
            xml+="<PDNO>"+pdno+"</PDNO></SHOP_GOODS_PD_LISTS>";
        }
        xml+="</SHOP_GOODS_PD_LIST>";
        return xml;
    }
}
