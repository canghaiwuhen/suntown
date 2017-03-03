package com.suntown.smartscreen.data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/20.
 */

public class ShelfListBean {

    public int ROWS;
    public List<RECORDBeanX> RECORD;

    @Override
    public String toString() {
        return "ShelfListBean{" +
                "ROWS=" + ROWS +
                ", RECORD=" + RECORD +
                '}';
    }

    public static class RECORDBeanX {


        public String ORD;
        public String RID;
        public String RNAME;
        public int ROWS;
        public String SID;
        public List<RECORDBean> RECORD;

        public RECORDBeanX(String ORD, String RID, String RNAME, int ROWS, String SID, List<RECORDBean> RECORD) {
            this.ORD = ORD;
            this.RID = RID;
            this.RNAME = RNAME;
            this.ROWS = ROWS;
            this.SID = SID;
            this.RECORD = RECORD;
        }

        @Override
        public String toString() {
            return "RECORDBeanX{" +
                    "ORD='" + ORD + '\'' +
                    ", RID='" + RID + '\'' +
                    ", RNAME='" + RNAME + '\'' +
                    ", ROWS=" + ROWS +
                    ", SID='" + SID + '\'' +
                    ", RECORD=" + RECORD +
                    '}';
        }


        public static class RECORDBean {
            /**
             * FLOORNO :
             * ISSTOP : 0
             * LASTMODIFY :
             * LASTOP : Joy
             * REMARKS :
             * RID : 27
             * SFID : K02G-1-14-1-019
             * SFNAME : K02G-1-14-1-019
             * SNO : K02G-1-14-1-019
             */

            public String FLOORNO;
            public String ISSTOP;
            public String LASTMODIFY;
            public String LASTOP;
            public String REMARKS;
            public String RID;
            public String SFID;
            public String SFNAME;
            public String SNO;

            public RECORDBean(String FLOORNO, String ISSTOP, String LASTMODIFY, String LASTOP, String REMARKS, String RID, String SFID, String SFNAME, String SNO) {
                this.FLOORNO = FLOORNO;
                this.ISSTOP = ISSTOP;
                this.LASTMODIFY = LASTMODIFY;
                this.LASTOP = LASTOP;
                this.REMARKS = REMARKS;
                this.RID = RID;
                this.SFID = SFID;
                this.SFNAME = SFNAME;
                this.SNO = SNO;
            }

            @Override
            public String toString() {
                return "RECORDBean{" +
                        "FLOORNO='" + FLOORNO + '\'' +
                        ", ISSTOP='" + ISSTOP + '\'' +
                        ", LASTMODIFY='" + LASTMODIFY + '\'' +
                        ", LASTOP='" + LASTOP + '\'' +
                        ", REMARKS='" + REMARKS + '\'' +
                        ", RID='" + RID + '\'' +
                        ", SFID='" + SFID + '\'' +
                        ", SFNAME='" + SFNAME + '\'' +
                        ", SNO='" + SNO + '\'' +
                        '}';
            }
        }
    }
}
