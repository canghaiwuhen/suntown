package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/9/30.
 */
public class BattVoltageBean {
    /**
     * {"label":["2016-10-05","2016-10-06","2016-10-07","2016-10-08","2016-10-09","2016-10-10","2016-10-11"],"value":[2955.33,2955.33,2656,2304,2304,2304,2304]}
     */
    public List<String> label;
    public List<Float> value;


    @Override
    public String toString() {
        return "BattVoltageBean{" +
                "label=" + label +
                ", value=" + value +
                '}';
    }
}

