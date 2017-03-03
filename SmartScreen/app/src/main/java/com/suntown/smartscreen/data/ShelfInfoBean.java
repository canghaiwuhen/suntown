package com.suntown.smartscreen.data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ShelfInfoBean {
    /**
     * ROWS : 5
     * RECORD : [{"SID":"571002002","COLNUMBER":"1","ROWNUMBER":"1","BARCODE":"1234567890","AID":"K02G-1-14-1-019-01-01","FILENAME":"","TINYIP":"","GNAME":""},{"SID":"571002002","COLNUMBER":"2","ROWNUMBER":"1","BARCODE":"12345678900","AID":"K02G-1-14-1-019-01-02","FILENAME":"","TINYIP":"","GNAME":""},{"SID":"571002002","COLNUMBER":"3","ROWNUMBER":"1","BARCODE":"6914973600010","AID":"K02G-1-14-1-019-01-03","FILENAME":"http://www.suntowngis.com:8080/TempImages/TwoFloor/K02G-2-33-1-001/IMG_0507.JPG","TINYIP":"","GNAME":"德芙牛奶巧克力43g"},{"SID":"571002002","COLNUMBER":"4","ROWNUMBER":"1","BARCODE":"6917878030623","AID":"K02G-1-14-1-019-01-04","FILENAME":"http://www.suntowngis.com:8080/TempImages/OneFloor/K02G-1-14-1-011/IMG_0860.jpg","TINYIP":"0.0.111.163","GNAME":"雀巢咖啡丝滑拿铁即饮瓶装饮品268ml"},{"SID":"571002002","COLNUMBER":"5","ROWNUMBER":"1","BARCODE":"6925303722074","AID":"K02G-1-14-1-019-01-05","FILENAME":"http://www.suntowngis.com:8080/TempImages/OneFloor/K02G-1-14-1-023/IMG_1067.jpg","TINYIP":"","GNAME":"统一鲜橙多饮料250ml"}]
     * SHELF : [{"ROWNUMBERS":"1","COLNUMBERS":"5","SFID":"K02G-1-14-1-019"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;
    public List<SHELFBean> SHELF;



    public static class RECORDBean {
        /**
         * SID : 571002002
         * COLNUMBER : 1
         * ROWNUMBER : 1
         * BARCODE : 1234567890
         * AID : K02G-1-14-1-019-01-01
         * FILENAME :
         * TINYIP :
         * GNAME :
         */

        public String SID;
        public int COLNUMBER;
        public int ROWNUMBER;
        public String BARCODE;
        public String AID;
        public String FILENAME;
        public String TINYIP;
        public String GNAME;
        public boolean isToggleOn;


    }

    public static class SHELFBean {
        /**
         * ROWNUMBERS : 1
         * COLNUMBERS : 5
         * SFID : K02G-1-14-1-019
         */

        public int ROWNUMBERS;
        public int COLNUMBERS;
        public String SFID;


    }
}
