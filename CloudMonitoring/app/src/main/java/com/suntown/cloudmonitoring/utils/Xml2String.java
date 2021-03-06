package com.suntown.cloudmonitoring.utils;

import android.util.Log;

import com.suntown.cloudmonitoring.bean.PDDetailBean;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/10/18.
 */

public class Xml2String {

    //    "<ns:checkCodeSendResponse><ns:return>{\"RESULT\":\"0\"}</ns:return></ns:checkCodeSendResponse>"
    private XmlPullParser xmlpullParser;

    public Xml2String(InputStream is) throws IOException, XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        xmlpullParser = factory.newPullParser();
        xmlpullParser.setInput(is, "UTF-8");
    }


    //解析XML  返回数据
    public String Pull2String() {
        int eventType = 0;
        try {
            eventType = xmlpullParser.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        String json=null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    Log.i("Xml2Json", xmlpullParser.getName());
                    if ("ns:return".equals(xmlpullParser.getName())) {
                        try {
                            //  Log.i("Xml2Json", xmlpullParser.nextText());
                            json = xmlpullParser.nextText();
                            Log.i("Xml2Json",json);
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
            try {
                eventType = xmlpullParser.next();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return json;
    }

}
