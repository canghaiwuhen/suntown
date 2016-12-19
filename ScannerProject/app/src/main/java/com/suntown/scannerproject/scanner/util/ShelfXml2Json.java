package com.suntown.scannerproject.scanner.util;



import com.suntown.scannerproject.scanner.bean.ShelfInfoBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/1.
 */
public class ShelfXml2Json {
    private XmlPullParser xmlpullParser;
    private ShelfInfoBean.ShelfBean shelfBean;
    private ArrayList<ShelfInfoBean.Shelf_Allocation> shelf_allocations;
    private ShelfInfoBean.Shelf_Allocation shelf_allocation;

    public ShelfXml2Json(String is) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        InputStream in = new ByteArrayInputStream(is.getBytes());
        xmlpullParser = factory.newPullParser();
        xmlpullParser.setInput(in, "UTF-8");
//        xmlpullParser.setInput(in,"GB2312");
        in.close();
    }

    public ShelfInfoBean PullXml() {
        ShelfInfoBean shelfInfoBean = null;
        int eventType = 0;
        Long id = null;
        String json = null;
        try {
            eventType = xmlpullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xmlpullParser.getName();
                switch (eventType) {
                    // 判断当前事件是否为文档开始事件
                    case XmlPullParser.START_DOCUMENT:
                        shelfInfoBean = new ShelfInfoBean();
                        break;
                    // 判断当前事件是否为标签元素开始事件
                    case XmlPullParser.START_TAG:
                        if ("Shelf".equals(tagName)) {
                            shelfBean = new ShelfInfoBean.ShelfBean();
                        } else if ("SFID".equals(tagName)) {
                            //  Log.i("Xml2Json", xmlpullParser.nextText());
                            json = xmlpullParser.nextText();
                            shelfBean.SFID = json;
                        } else if ("RowCount".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelfBean.RowCount=json;
                        } else if ("ACount".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelfBean.ACount = json;
                        } else if ("Exist".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelfBean.Exist = json;
                            shelfInfoBean.shelfBean=shelfBean;
                        }
                        //Shelf_Allocation集合
                        else if ("Shelf_Allocation".equals(tagName)) {
                            shelf_allocations = new ArrayList<>();
                        } else if ("Shelf_Allocations".equals(tagName)) {
                            shelf_allocation = new ShelfInfoBean.Shelf_Allocation();
                        } else if ("AID".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelf_allocation.AID = json;
                        } else if ("Tinyip".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelf_allocation.Tinyip=json;
                        } else if ("Barcode".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelf_allocation.Barcode = json;
                        } else if ("GName".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelf_allocation.GName = json;
                        } else if ("RowNumber".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelf_allocation.RowNumber = json;
                        } else if ("ColNumber".equals(tagName)) {
                            json = xmlpullParser.nextText();
                            shelf_allocation.ColNumber = json;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        //添加到对象
                        if ("Shelf_Allocation".equals(tagName)) {
                            shelfInfoBean.shelfBean = shelfBean;
                            shelfInfoBean.Shelf_Allocations = shelf_allocations;
                            //添加到集合
                        } else if ("Shelf_Allocations".equals(tagName)) {
                            shelf_allocations.add(shelf_allocation);
                        }
                        break;
                }
                eventType = xmlpullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return shelfInfoBean;
    }

}

