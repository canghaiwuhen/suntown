package com.suntown.scannerproject.utils;


import android.util.Log;


import com.suntown.scannerproject.bean.Item2;
import com.suntown.scannerproject.bean.ShopXmlBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/1.
 */
public class Xml2Json {
    private XmlPullParser xmlpullParser;
    private ShopXmlBean shopXmlBean;
    private List<ShopXmlBean> list;
    private Item2 item2;

    public Xml2Json(String is) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        InputStream in = new ByteArrayInputStream(is.getBytes());
        xmlpullParser = factory.newPullParser();
        xmlpullParser.setInput(in,"UTF-8");
//        xmlpullParser.setInput(in,"GB2312");
        in.close();
    }

    public List<ShopXmlBean> PullXml() {
        List<ShopXmlBean> shopBeanList = null;
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
                        shopBeanList = new ArrayList<>();
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
//                        if ("LabData".equals(tagName)) {
//                            //  Log.i("Xml2Json", xmlpullParser.nextText());
////                            json = xmlpullParser.nextText();
//                            shopXmlBean = new ShopXmlBean();
//                        }else
                        if ("TinyIP".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            shopXmlBean = new ShopXmlBean();
                            if (!"".equals(s)){
                                shopXmlBean.setTinyIp(s);
                            }
                        }else if ("SFID".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setSFID(s);
                            }
                        }else if ("Barcode".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setBarcode(s);
                            }
                        }else if ("GName".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setGName(s);
                            }
                        }else if ("GCode".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setGCode(s);
                            }
                        }else if ("LastDate".equals(tagName)) {
                            shopBeanList.add(shopXmlBean);
                            shopXmlBean=null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("LabData".equals(tagName)){
//                            shopBeanList.add(shopXmlBean);
//                            shopXmlBean=null;
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
        return shopBeanList;
    }

    //<TinyIP>0.0.2.201</TinyIP>
//    <SFID>AL01-1-14-1-002</SFID>
//    <Barcode>6920458814353</Barcode>
//    <GName>午后奶茶</GName>
//    <GCode>6920458814353</GCode>
//    <Unit>个</Unit>
//    <Spec/>
//    <Kind>140903</Kind>
//    <Origin/>
//    <Oriprice>0</Oriprice>
//    <uptPrice>3.5</uptPrice>
//    <MemPrice>0</MemPrice>
//    <DispStr>层数x排面x纵深</DispStr>
//    <CurStore>0</CurStore>
//    <VBatchNO/>
//    <GStatus>0</GStatus>
//    <Battery>3040</Battery>
//    <LastDate>2016-11-07 15:17</LastDate>




    public List<ShopXmlBean> Xml2Bean() {
        List<ShopXmlBean> shopBeanList = null;
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
                        shopBeanList = new ArrayList<>();
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
//                        if ("LabData".equals(tagName)) {
//                            //  Log.i("Xml2Json", xmlpullParser.nextText());
////                            json = xmlpullParser.nextText();
//                            shopXmlBean = new ShopXmlBean();
//                        }else
                        if ("TinyIP".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            shopXmlBean = new ShopXmlBean();
                            if (!"".equals(s)){
                                shopXmlBean.setTinyIp(s);
                            }
                        }else if ("SFID".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setSFID(s);
                            }
                        }else if ("Barcode".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setBarcode(s);
                            }
                        }else if ("GName".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setGName(s);
                            }
                        }else if ("GCode".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setGCode(s);
                            }
                        }else if ("Kind".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setKind(s);
                            }
                        }else if ("Oriprice".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setOriprice(s);
                            }
                        }else if ("uptPrice".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setUptPrice(s);
                            }
                        }else if ("GStatus".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setGStatus(s);
                            }
                        }else if ("Battery".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setBattery(s);
                            }
                        }else if ("Battery".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setBattery(s);
                            }
                        }else if ("LastDate".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setLastDate(s);
                            }
                            shopBeanList.add(shopXmlBean);
                        }else if ("PowerOff".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                shopXmlBean.setPowerOff(s);
                            }
                            shopBeanList.add(shopXmlBean);
                            shopXmlBean=null;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if ("LabData".equals(tagName)){
//                            shopBeanList.add(shopXmlBean);
//                            shopXmlBean=null;
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
        return shopBeanList;
    }
    public ShopXmlBean pullXml2Bean(){
            ShopXmlBean  shopXmlBean = new ShopXmlBean();
            int eventType;
            try {
                eventType = xmlpullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String tagName  = xmlpullParser.getName();
                    switch (eventType) {
                        // 判断当前事件是否为文档开始事件
//                        case XmlPullParser.START_DOCUMENT:
//                            break;
                        // 判断当前事件是否为标签元素开始事件
                        case XmlPullParser.START_TAG:
                            if ("TinyIP".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)){
                                    shopXmlBean.setTinyIp(s);
                                }
                            }else if ("SFID".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setSFID(s);
                                }
                            }else if ("Barcode".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setBarcode(s);
                                }
                            }else if ("GName".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setGName(s);
                                }
                            }else if ("GCode".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setGCode(s);
                                }
                            }else if ("Unit".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setUnit(s);
                                }
                            }else if ("Spec".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setSpec(s);
                                }
                            }else if ("Kind".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setKind(s);
                                }
                            }else if ("Origin".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setOrigin(s);
                                }
                            }else if ("Oriprice".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setOriprice(s);
                                }
                            }else if ("uptPrice".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setUptPrice(s);
                                }
                            }else if ("MemPrice".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setMemPrice(s);
                                }
                            }else if ("DispStr".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setDispStr(s);
                                }
                            }else if ("CurStore".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setCurStore(s);
                                }
                            }else if ("GStatus".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setGStatus(s);
                                }
                            } else if ("VBatchNO".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setVBatchNO(s);
                                }
                            }else if ("Battery".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setBattery(s);
                                }
                            }else if ("LastDate".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setLastDate(s);
                                }

                            }else if ("PowerOff".equals(tagName)) {
                                String s = xmlpullParser.nextText();
                                if (!"".equals(s)) {
                                    shopXmlBean.setPowerOff(s);
                                }
                            }
                            break;
                        case XmlPullParser.END_TAG:
                            if ("LabData".equals(tagName)){
//                            shopBeanList.add(shopXmlBean);
//                            shopXmlBean=null;
                            }
                            break;
                    }
                    eventType=xmlpullParser.next();
                }
            } catch (XmlPullParserException e) {
                e.printStackTrace();
                return shopXmlBean;
            } catch (IOException e) {
                e.printStackTrace();
                return shopXmlBean;
            }
            return shopXmlBean;
    }


    public Item2 PullGoodsXml() {
        int eventType = 0;
        try {
            eventType = xmlpullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName  = xmlpullParser.getName();
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        item2 = new Item2();
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if ("Barcode".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                item2.Barcode = s;
                            }
                        } else if ("GName".equals(tagName)) {
                            String s = xmlpullParser.nextText();
                            if (!"".equals(s)) {
                                item2.GName=s;
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
        return item2;
    }
}

