package com.suntowns.labeltest.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;


import com.suntowns.labeltest.PhoneInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/8/2.
 */
public class Contacts {
    private static final String TAG = "Contacts";
    public static List<PhoneInfo> getNumber(Context context) {
        List<PhoneInfo> lists = new ArrayList<>();
        Cursor cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null, null, null, null);
        String phoneNumber;
        String phoneName;
        while (cursor.moveToNext()) {
            phoneNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));//电话号码
            phoneName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));//姓名
            PhoneInfo  info = new PhoneInfo(phoneName, phoneNumber);
            lists.add(info);
        }
        return lists;
    }
}
