package com.suntown.suntownshop.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 各种数据规则验证类
 * 
 * @author 钱凯
 *
 */
public class FormatValidation {

	/**
	 * 由于Java是基于Unicode编码的，因此，一个汉字的长度为1，而不是2。
	 * 但有时需要以字节单位获得字符串的长度。例如，“123abc长城”按字节长度计算是10，而按Unicode计算长度是8。
	 * 为了获得10，需要从头扫描根据字符的Ascii来获得具体的长度
	 * 。如果是标准的字符，Ascii的范围是0至255，如果是汉字或其他全角字符，Ascii会大于255。
	 * 因此，可以编写如下的方法来获得以字节为单位的字符串长度。
	 * */
	public static int getWordCount(String s) {
		if (s == null || "".equals(s)) {
			return 0;
		}
		int length = 0;
		for (int i = 0; i < s.length(); i++) {
			int ascii = Character.codePointAt(s, i);
			if (ascii >= 0 && ascii <= 255)
				length++;
			else
				length += 2;

		}
		return length;

	}

	/*
	 * 基本原理是将字符串中所有的非标准字符（双字节字符）替换成两个标准字符（**，或其他的也可以）。这样就可以直接例用length方法获得字符串的字节长度了
	 */
	public static int getWordCountRegex(String s) {

		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}

	/* 按特定的编码格式获取长度 */
	public static int getWordCountCode(String str, String code)
			throws UnsupportedEncodingException {
		return str.getBytes(code).length;
	}

	/**
	 * 判断是否为18位身份证号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isIdCard(String str) {
		if (str == null || "".equals(str))
			return false;
		Pattern pattern = Pattern.compile("\\d{17}[0-9a-zA-Z]");
		Matcher isIdCard = pattern.matcher(str);
		if (isIdCard.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 判断是否全为数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		if (str == null || "".equals(str))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 判断是否为字符或数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isCharacterOrNumber(String str) {
		if (str == null || "".equals(str))
			return false;
		String charRegex = "[A-Za-z0-9]*";
		return str.matches(charRegex);
	}

	/**
	 * 判断是否全为字符
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isCharacter(String str) {
		if (str == null || "".equals(str))
			return false;
		String charRegex = "[a-zA-Z]*";
		return str.matches(charRegex);
	}

	/**
	 * 判断是否全为符号
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isSymbol(String str) {
		if (str == null || "".equals(str))
			return false;
		String regex = "[^a-zA-Z_0-9]*";
		return str.matches(regex);
	}

	/**
	 * 验证邮箱格式
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if (email == null || "".equals(email))
			return false;
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);

		return m.matches();
	}

	/**
	 * 验证手机格式
	 */
	public static boolean isMobileNO(String mobiles) {
		String telRegex = "[1][34578]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * 验证电话号码格式
	 */
	public static boolean isPhoneNo(String phone) {
		String telRegex = "[0-9]+\\-?[0-9]+\\-?[0-9]+"; //
		if (TextUtils.isEmpty(phone))
			return false;
		else
			return phone.matches(telRegex);
	}

	/**
	 * 将字符串转化为数组
	 */
	public static String[] StringToArray(String source, String split) {
		if (source == null || "".equals(source)) {
			return null;
		} else {
			return source.split(split);
		}
	}

	/**
	 * 将数组转化为字符串
	 */
	public static String ArrayToString(String[] source, String split) {
		String str = "";
		for (String value : source) {
			str += value;
			str += split;
		}
		return str;
	}
}
