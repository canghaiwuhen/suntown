package com.suntown.zxing.encoding;

import java.util.Hashtable;

import android.graphics.Bitmap;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * @author Ryan Tang
 *
 */
public final class EncodingHandler {
	private static final int BLACK = 0xff000000;

	
	
	
	private BitMatrix updateBit(BitMatrix matrix, int margin) {

		int tempM = margin * 2;

		int[] rec = matrix.getEnclosingRectangle(); // ��ȡ��ά��ͼ��������

		int resWidth = rec[2] + tempM;

		int resHeight = rec[3] + tempM;

		BitMatrix resMatrix = new BitMatrix(resWidth, resHeight); // �����Զ���߿������µ�BitMatrix

		resMatrix.clear();

		for (int i = margin; i < resWidth - margin; i++) { // ѭ��������ά��ͼ�����Ƶ��µ�bitMatrix��

			for (int j = margin; j < resHeight - margin; j++) {

				if (matrix.get(i - margin + rec[0], j - margin + rec[1])) {

					resMatrix.set(i, j);

				}

			}

		}

		return resMatrix;

	}

	public static Bitmap createQRCode(String str, int widthAndHeight)
			throws WriterException {
		Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		BitMatrix matrix = new MultiFormatWriter().encode(str,
				BarcodeFormat.QR_CODE, widthAndHeight, widthAndHeight);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = BLACK;
				}
			}
		}
		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}

	/**
	 * ���ڽ��������������ɳ�һά�� ע��Ŀǰ��������Ϊ���ĵĻ���ֱ�ӱ���Ҫ�޸ĵײ�jar��������
	 *
	 * @param content
	 *            ��Ҫ����һά�������
	 * @return �������ɺõ�һά��bitmap
	 * @throws WriterException
	 *             WriterException�쳣
	 */
	public static Bitmap CreateBarDCode(String content) throws WriterException {
		// ����һά����,����ʱָ����С,��Ҫ������ͼƬ�Ժ��ٽ�������,������ģ������ʶ��ʧ��
		BitMatrix matrix = new MultiFormatWriter().encode(content,
				BarcodeFormat.CODE_128, 500, 150);
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		int[] pixels = new int[width * height];
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (matrix.get(x, y)) {
					pixels[y * width + x] = 0xff000000;
				}
			}
		}

		Bitmap bitmap = Bitmap.createBitmap(width, height,
				Bitmap.Config.ARGB_8888);
		// ͨ��������������bitmap,����ο�api
		bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
		return bitmap;
	}
}
