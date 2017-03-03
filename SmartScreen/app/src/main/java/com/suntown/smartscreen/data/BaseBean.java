package com.suntown.smartscreen.data;

/**
 * Created by Administrator on 2017/2/13.
 */

public class BaseBean {
    /**
     * REUSLT : 1
     */

    public String REUSLT;

    public BaseBean(String REUSLT) {
        this.REUSLT = REUSLT;
    }

    @Override
    public String toString() {
        return "BaseBean{" +
                "REUSLT='" + REUSLT + '\'' +
                '}';
    }
}
