package com.suntown.suntownshop.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * �������ݹ�����֤��
 * 
 * @author Ǯ��
 *
 */
public class FormatValidation {

	/**
	 * ����Java�ǻ���Unicode����ģ���ˣ�һ�����ֵĳ���Ϊ1��������2��
	 * ����ʱ��Ҫ���ֽڵ�λ����ַ����ĳ��ȡ����磬��123abc���ǡ����ֽڳ��ȼ�����10������Unicode���㳤����8��
	 * Ϊ�˻��10����Ҫ��ͷɨ������ַ���Ascii����þ���ĳ���
	 * ������Ǳ�׼���ַ���Ascii�ķ�Χ��0��255������Ǻ��ֻ�����ȫ���ַ���Ascii�����255��
	 * ��ˣ����Ա�д���µķ�����������ֽ�Ϊ��λ���ַ������ȡ�
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
	 * ����ԭ���ǽ��ַ��������еķǱ�׼�ַ���˫�ֽ��ַ����滻��������׼�ַ���**����������Ҳ���ԣ��������Ϳ���ֱ������length��������ַ������ֽڳ�����
	 */
	public static int getWordCountRegex(String s) {

		s = s.replaceAll("[^\\x00-\\xff]", "**");
		int length = s.length();
		return length;
	}

	/* ���ض��ı����ʽ��ȡ���� */
	public static int getWordCountCode(String str, String code)
			throws UnsupportedEncodingException {
		return str.getBytes(code).length;
	}

	/**
	 * �ж��Ƿ�Ϊ18λ���֤��
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
	 * �ж��Ƿ�ȫΪ����
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
	 * �ж��Ƿ�Ϊ�ַ�������
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
	 * �ж��Ƿ�ȫΪ�ַ�
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
	 * �ж��Ƿ�ȫΪ����
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
	 * ��֤�����ʽ
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
	 * ��֤�ֻ���ʽ
	 */
	public static boolean isMobileNO(String mobiles) {
		String telRegex = "[1][34578]\\d{9}";// "[1]"�����1λΪ����1��"[358]"����ڶ�λ����Ϊ3��5��8�е�һ����"\\d{9}"��������ǿ�����0��9�����֣���9λ��
		if (TextUtils.isEmpty(mobiles))
			return false;
		else
			return mobiles.matches(telRegex);
	}

	/**
	 * ��֤�绰�����ʽ
	 */
	public static boolean isPhoneNo(String phone) {
		String telRegex = "[0-9]+\\-?[0-9]+\\-?[0-9]+"; //
		if (TextUtils.isEmpty(phone))
			return false;
		else
			return phone.matches(telRegex);
	}

	/**
	 * ���ַ���ת��Ϊ����
	 */
	public static String[] StringToArray(String source, String split) {
		if (source == null || "".equals(source)) {
			return null;
		} else {
			return source.split(split);
		}
	}

	/**
	 * ������ת��Ϊ�ַ���
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
