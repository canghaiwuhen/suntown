package com.suntown.scannerproject.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
@XStreamAlias("ShopList")
public class ShopResult {
    @XStreamImplicit(itemFieldName="ShopLists")
    public List<ShopListBean> shopListBean;

    public ShopResult() {
    }

    @Override
    public String toString() {
        return "ShopResult{" +
                "shopListBean=" + shopListBean +
                '}';
    }
}
