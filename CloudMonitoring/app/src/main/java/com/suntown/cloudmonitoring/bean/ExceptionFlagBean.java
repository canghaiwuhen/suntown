package com.suntown.cloudmonitoring.bean;

/**
 * Created by Administrator on 2016/11/10.
 */

public class ExceptionFlagBean {
    /**
     * eslWarn : {"bindFlag":false,"loginFlag":false,"mapTypeFlag":false,"modelFlag":false,"oriMaptype":-2,"sendFlag":false,"successSend":false,"taskFlag":false}
     */

    public EslWarnBean eslWarn;


    public static class EslWarnBean {
        /**
         * bindFlag : false
         * loginFlag : false
         * mapTypeFlag : false
         * modelFlag : false
         * oriMaptype : -2
         * sendFlag : false
         * successSend : false
         * taskFlag : false
         */

        public boolean bindFlag;
        public boolean loginFlag;
        public boolean mapTypeFlag;
        public boolean modelFlag;
        public int oriMaptype;
        public boolean sendFlag;
        public boolean successSend;
        public boolean taskFlag;

        @Override
        public String toString() {
            return "EslWarnBean{" +
                    "bindFlag=" + bindFlag +
                    ", loginFlag=" + loginFlag +
                    ", mapTypeFlag=" + mapTypeFlag +
                    ", modelFlag=" + modelFlag +
                    ", oriMaptype=" + oriMaptype +
                    ", sendFlag=" + sendFlag +
                    ", successSend=" + successSend +
                    ", taskFlag=" + taskFlag +
                    '}';
        }
    }
}
