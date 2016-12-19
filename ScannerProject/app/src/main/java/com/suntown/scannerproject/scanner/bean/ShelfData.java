package com.suntown.scannerproject.scanner.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/11/30.
 */

public class ShelfData {
    public ShelfBean shelfBean;
    public List<Shelf_Allocation> Shelf_Allocations;

    public static class ShelfBean {
        public String SFID;
        public String RowCount;
        public String ACount;
        public String Exist;
    }

    public static class Shelf_Allocation {
        public String AID;
        public String Tinyip;
        public String Barcode;
        public String GName;
        public String RowNumber;
        public String ColNumber;
    }
}
