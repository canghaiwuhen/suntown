package com.suntown.suntownshop.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;
/**
 * XML������
 * @author Administrator
 *
 */
public class XmlParser {
	public static String parse(InputStream is, String code, String node)
			throws Exception {
		String result = "";
		XmlPullParser parser = Xml.newPullParser();// ��android.util.Xml����һ��XmlPullParserʵ��
		parser.setInput(is, code); // ���������� ��ָ�����뷽ʽ
		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().endsWith(node)) {
					eventType = parser.next();
					result = parser.getText();
					return result;
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		return result;
	}

	public static String parse(String str, String code, String node)
			throws Exception {
		String result = "";
		ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes());
		XmlPullParser parser = Xml.newPullParser();// ��android.util.Xml����һ��XmlPullParserʵ��
		parser.setInput(is, code); // ���������� ��ָ�����뷽ʽ

		int eventType = parser.getEventType();
		while (eventType != XmlPullParser.END_DOCUMENT) {
			switch (eventType) {
			case XmlPullParser.START_DOCUMENT:
				break;
			case XmlPullParser.START_TAG:
				if (parser.getName().endsWith(node)) {
					eventType = parser.next();
					result = parser.getText();
					return result;
				}
				break;
			case XmlPullParser.END_TAG:
				break;
			}
			eventType = parser.next();
		}
		return result;
	}
}
