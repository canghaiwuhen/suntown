package com.suntown.smartscreen.data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/16.
 */

public class AllTaskBean {
    /**
     * ROWS : 12
     * RECORD : [{"TYPENAME":"陈列模板","CT":"19","TYPE":"1"},{"TYPENAME":"会员价模板","CT":"22","TYPE":"3"},{"TYPENAME":"促异模板2","CT":"2","TYPE":"28"},{"TYPENAME":"陈列模板2","CT":"2","TYPE":"21"},{"TYPENAME":"价格模板2","CT":"3","TYPE":"20"},{"TYPENAME":"促销模板2","CT":"2","TYPE":"22"},{"TYPENAME":"价格模板","CT":"78","TYPE":"0"},{"TYPENAME":"促异模板","CT":"18","TYPE":"8"},{"TYPENAME":"缺货模板","CT":"18","TYPE":"7"},{"TYPENAME":"缺货模板2","CT":"2","TYPE":"27"},{"TYPENAME":"促销模板","CT":"41","TYPE":"2"},{"TYPENAME":"会员价模板2","CT":"2","TYPE":"23"}]
     */

    public int ROWS;
    public List<RECORDBean> RECORD;

    public static class RECORDBean {
        /**
         * TYPENAME : 陈列模板
         * CT : 19
         * TYPE : 1
         */

        public String TYPENAME;
        public String CT;
        public String TYPE;

    }
}
