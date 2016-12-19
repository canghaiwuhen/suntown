package com.suntown.cloudmonitoring.utils;

import com.suntown.cloudmonitoring.bean.PDDetailBean;

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

}
