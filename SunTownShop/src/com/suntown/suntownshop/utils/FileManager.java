package com.suntown.suntownshop.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;
/**
 * �ļ�������
 * @author Administrator
 *
 */
public class FileManager {
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // �ж�sd���Ƿ����
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// ��ȡ��Ŀ¼
		} else {
			return null;
		}
		return sdDir.toString();

	}
	/**
	 * ��ȡ�ļ�����Ŀ¼�������SD�����浽SD���У�û��SD�����浽DATAĿ¼
	 * @return
	 */
	public static String getDataPath(Context context) {
		File dir = null;
		String path = getSDPath();
		if (path == null) {
			//dir = Environment.getDataDirectory();
			path = context.getApplicationInfo().dataDir;//dir.toString();
		}
		path = path + "/suntowndata";
		dir = new File(path);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return path;
	}
	/**
	 * ��ȡ�ı��ļ��е��ַ���
	 * @param fileName �ļ�·��
	 * @return ���ظ��ı��������ַ�������
	 * @throws IOException
	 */
	public static String readFile(String fileName) throws IOException {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;

	}
	
	/**
	 * ��ȡBASE64������ļ�������
	 * 
	 * @param filename
	 * @return
	 */
	public static String getFileBase64(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int count = 0;
			byte[] buffer = new byte[1024];
			while ((count = fis.read(buffer)) >= 0) {
				baos.write(buffer, 0, count);
			}
			String uploadBuffer = new String(Base64.encode(baos.toByteArray(),
					Base64.DEFAULT)); // ����Base64����
			return uploadBuffer;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
