package com.suntowns.labeltest.utils;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/5.
 */
public class SmartSocketUtils {
    static Context context;
    public static byte[] makeBytes(int hexType, byte[] loads,Context ctx) {
        context=ctx;
        ArrayList<Byte> bytes = new ArrayList<>();
        bytes.add((byte) 0x5A);
        bytes.add((byte) 0x5A);
        bytes.add((byte) loads.length);
        bytes.add((byte) hexType);
        bytes.addAll(CreateSerialNum());
            for (byte load : loads) {
                bytes.add(load);
            }
        byte b = 0;
        for (int i = 0; i < bytes.size(); i++) {
            b ^= bytes.get(i);
        }
        bytes.add(b);
        byte[] arrayBytes = new byte[bytes.size()];
        for (int i1 = 0; i1 < bytes.size(); i1++) {
            arrayBytes[i1] = bytes.get(i1);
        }
        return arrayBytes;
    }
//    5a5a07010002010000000714a1b7
    //暂时放这里，不清楚此静态变量会不会在程序未退出时销毁
    public static List<Byte> CreateSerialNum() {
        ArrayList<Byte> bytes = new ArrayList<>();
        int i = SPUtils.getInt(context, Constant.SERIAL_NUM);
        int serialNum = 10000 +i++;
        String serialNumber = serialNum + "";
        String hex = serialNumber.substring(1, serialNumber.length());
        String one = hex.substring(0, 2);
        String two = hex.substring(2);
        bytes.add((byte) Integer.parseInt(one, 16));
        bytes.add((byte) Integer.parseInt(two, 16));
        SPUtils.put(context,Constant.SERIAL_NUM,i);
        return bytes;
    }
    //16string -->byte
    public static byte hexStringToByte(String hex) {
        byte result ;
        if (hex.length()==1) {
            hex = "0" + hex;
        }
        char[] achar = hex.toCharArray();
        result = (byte) (toByte(achar[0]) << 4 | toByte(achar[1]));
        return result;
    }
    private static byte toByte(char c) {
        byte b = (byte) "0123456789ABCDEF".indexOf(c);
        return b;
    }
    //    拆分数据
    public static List<Byte> int2bytes(int port) {
        ArrayList<Byte> bytes = new ArrayList<>();
        String hex = Integer.toHexString(port);
        String one = hex.substring(0, 2);
        String two = hex.substring(2);
        bytes.add((byte) Integer.parseInt(one, 16));
        bytes.add((byte) Integer.parseInt(two, 16));
        return bytes;
    }
    //  拆分标签string 拼接成字符串
    public static String append(String str){
        String s="";
            Log.i("tets",str.getBytes()+"");
            String one = str.substring(0, 2);
            byte[] byte1 = HexStr.toStringHex(one).getBytes();
            s+= byte1[0]+".";

            String two = str.substring(2, 4);
            byte[] byte2 = HexStr.toStringHex(two).getBytes();
            s+=byte2[0]+".";

            String three = str.substring(4, 6);
            byte[] byte3 = HexStr.toStringHex(three).getBytes();
            s+=byte3[0]+".";

            String four = str.substring(6, 8);
            byte[] byte4 = HexStr.toStringHex(four).getBytes();
            s+=byte4[0]+"";
        return s;
    }


}
