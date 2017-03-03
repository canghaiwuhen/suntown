package com.suntown.scannerproject.utils;


import com.suntown.scannerproject.bean.UpdateBean;
import com.suntown.scannerproject.input.bean.PDDetailBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/3.
 */

public class Xml2Bean {
    private XmlPullParser xmlpullParser;

    public Xml2Bean(String is) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        InputStream in = new ByteArrayInputStream(is.getBytes());
        xmlpullParser = factory.newPullParser();
        xmlpullParser.setInput(in,"UTF-8");
//        xmlpullParser.setInput(in,"GB2312");
        in.close();
    }
    /**
     * 盘点单号xml解析
     * @return
     */
//    <SHOPGOODSPDLIST>
    //    <PDID>21061</PDID>
//    <SPDID>200668724</SPDID>
//    <PDNO>20160815174038</PDNO>
//    <BARCODE>6918976333388</BARCODE>
//    <GOODSNAME>雀巢优活饮用水330毫升</GOODSNAME>
//    <D4>25</D4>
//    <D7>110</D7>
//    </SHOPGOODSPDLIST>
    public PDDetailBean PullPDXML() {
        PDDetailBean pdDetailBean = null;
        int eventType = 0;
        Long id = null;
        String json = null;
        try {
            eventType = xmlpullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName  = xmlpullParser.getName();
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        pdDetailBean = new PDDetailBean();
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if ("PDID".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.PDID = s;
                            }
                        }else if ("SPDID".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.SPDID=s;
                            }
                        }else if ("PDNO".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.PDNO=s;
                            }
                        }else if ("BARCODE".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.BARCODE=s;
                            }
                        }else if ("GOODSNAME".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.GOODSNAME = s;
                            }
                        }else if ("D4".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.D4 = s;
                            }
                        }else if ("D7".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                pdDetailBean.D7 = s;
                            }
                        }
                        break;
                }
                eventType=xmlpullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return pdDetailBean;
    }


//    <pdaUpdateFile>
//    <fswid>836</fswid>
//    <swid>184</swid>
//    <swversion>1.0.0.3</swversion>
//    <filename>SalesPoint.Device.dll</filename>
//    <version>2.0.7.6</version>
//    <ext>dll</ext>
//    </pdaUpdateFile>
    public UpdateBean PullUpdateXML() {
        UpdateBean updateBean = null;
        int eventType ;
        try {
            eventType = xmlpullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName  = xmlpullParser.getName();
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        updateBean = new UpdateBean();
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if ("fswid".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                updateBean.fswid = s;
                            }
                        }else if ("swid".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                updateBean.swid=s;
                            }
                        }else if ("swversion".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                updateBean.swversion=s;
                            }
                        }else if ("filename".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                updateBean.filename=s;
                            }
                        }else if ("version".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                updateBean.version = s;
                            }
                        }else if ("ext".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                updateBean.ext = s;
                            }
                        }
                        break;
                }
                eventType=xmlpullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return updateBean;
    }

}
