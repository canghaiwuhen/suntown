package com.suntown.scannerproject.utils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Administrator on 2016/12/2.
 */

public class FormatString {
    public static String fromTinyip(String message) {
        String string = message.replace(".", "");
        //0000BCC4
        String s1 = string.substring(0,2);
        String s2 = string.substring(2,4);
        String s3 = string.substring(4,6);
        String s4 = string.substring(6,8);
        int i1 = Integer.parseInt(s1, 16);
        int i2 = Integer.parseInt(s2, 16);
        int i3 = Integer.parseInt(s3, 16);
        int i4 = Integer.parseInt(s4, 16);
        return i1+"."+i2+"."+i3+"."+i4;
    }

    public static String toXml(String tinyip, String barcode, String dispRow, String dispCol, String dispDepth, String rAmount, String alterAmount, String sid, String deviceNum) {
        String xml = "<DispData><TinyIP>"+tinyip+"</TinyIP><Barcode>"+barcode+"</Barcode>" +
                "<DispRow>"+dispRow+"</DispRow><DispCol>"+dispCol+"</DispCol><DispDepth>"+dispDepth+
                "</DispDepth><RAmount>"+rAmount+"</RAmount><AlterAmount>"+alterAmount+"</AlterAmount><IsAdd>"+1+
                "</IsAdd><SID>"+sid+"</SID><SCANID>"+deviceNum+"</SCANID></DispData>";
        return xml;
    }

    /**
     * 动态设置ListView的高度
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if(listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
