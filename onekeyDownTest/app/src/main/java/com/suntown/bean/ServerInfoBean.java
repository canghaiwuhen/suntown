package com.suntown.bean;

/**
 * Created by Administrator on 2016/8/4.
 */
public class ServerInfoBean {

    /**
     * SERVER_IP : 120.55.180.224
     * SERVER_PORTNO : 1005
     * RESULT : 0
     */
    private String SERVER_IP;
    private String SERVER_PORTNO;
    private String RESULT;

    public String getSERVER_IP() {
        return SERVER_IP;
    }

    public void setSERVER_IP(String SERVER_IP) {
        this.SERVER_IP = SERVER_IP;
    }

    public String getSERVER_PORTNO() {
        return SERVER_PORTNO;
    }

    public void setSERVER_PORTNO(String SERVER_PORTNO) {
        this.SERVER_PORTNO = SERVER_PORTNO;
    }

    public String getRESULT() {
        return RESULT;
    }

    public void setRESULT(String RESULT) {
        this.RESULT = RESULT;
    }
}
