package com.suntowns.labeltest.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/4.
 */
public class HexStr {

    //16进制 2 String
    public static String hexStr2Str(String hexStr) {
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            n = hexStr.indexOf(hexs[2 * i]) * 16;
            n += hexStr.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }

    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            baKeyword[i] = (byte)(0xff & Integer.parseInt(s.substring(i*2, i*2+2),16));
            try {
                s = new String(baKeyword, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return s;
    }



    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    //byte[]-->hexString
    public static String bytes2Hex(byte[] src){
        char[] res = new char[src.length*2];
        final char hexDigits[]={'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
        for(int i=0,j=0; i<src.length; i++){
            res[j++] = hexDigits[src[i] >>>4 & 0x0f];
            res[j++] = hexDigits[src[i] & 0x0f];
        }

        return new String(res);
    }
    /**
     * 把16进制字符串转换成字节数组
     * @param
     * @return byte[]
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    private static int toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }

    public static byte[] ResolveIP(String portno, String serverIp) {
        ArrayList<Byte> bytes = new ArrayList<>();
        String s1 = Integer.toHexString(Integer.parseInt(portno));
        Log.i("test","s1:"+s1);
        if (s1.length()<4){
            s1 = "0"+s1;
        }
        int i2 = Integer.parseInt(s1.substring(0, 2),16);
        bytes.add((byte) i2);
        int i3 = Integer.parseInt(s1.substring(2, 4),16);
        bytes.add((byte) i3);
        int addressMode;
        if (serverIp.startsWith("www")){
            addressMode = 0x0B;
            bytes.add((byte) addressMode);
            for (int i = 0; i <3 ; i++) {
                bytes.add((byte) 0x5A);
            }
            bytes.add((byte)0x2E);
            String[] split = serverIp.substring(3, serverIp.length()).split("\\.");
            for (int i = 0; i < split.length; i++) {
                String s = split[i];
                int split1 = Integer.parseInt(s);
                int split2 = split1 & 0xff;
                int split3= Integer.valueOf(split2);
                bytes.add((byte) split3);
            }
        }else{
            addressMode = 0x00;
            bytes.add((byte) addressMode);
            String[] split = serverIp.split("\\.");
            for (int i = 0; i < split.length; i++) {
            String s = split[i];
            int split1 = Integer.parseInt(s);
            int split2 = split1 & 0xff;
            int split3= Integer.valueOf(split2);
            bytes.add((byte) split3);
            }
        }
        byte[] buff = new byte[bytes.size()];
        for (int i = 0; i < bytes.size(); i++) {
            Byte aByte = bytes.get(i);
            buff[i]= aByte;
        }
        return buff;
    }
    public static String toHex(byte b) {
        String result = Integer.toHexString(b & 0xFF);
        if (result.length() == 1) {
            result = '0' + result;
        }
        return result;
    }

}
