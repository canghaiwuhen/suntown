package com.suntown.smartscreen.data;

/**
 * Created by Administrator on 2017/2/7.
 */

public class User {
    /**
     * UNAME : suntown
     * RESULT : 1
     * USERID : suntown
     */

    public String UNAME;
    public String RESULT;
    public String USERID;

    @Override
    public String toString() {
        return "User{" +
                "UNAME='" + UNAME + '\'' +
                ", RESULT='" + RESULT + '\'' +
                ", USERID='" + USERID + '\'' +
                '}';
    }
}
