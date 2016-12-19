package com.suntown.cloudmonitoring.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2016/9/28.
 */

public class SortList {
    public static List<String> sort(List<String> list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (String cd : list) {
            if (set.add(cd)) {
                newList.add(cd);
            }
        }
        return list;
    }
}
