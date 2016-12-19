package com.suntown.scannerproject.scanner.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
@XStreamAlias("Data")
public class ShelfInfoBean {

    /**
     * RowCount 层
     * ACount 列
     */
    @XStreamImplicit(itemFieldName="Shelf")
    public ShelfBean shelfBean;
    @XStreamImplicit(itemFieldName="Shelf_Allocation")
    public List<Shelf_Allocation> Shelf_Allocations;

    @Override
    public String toString() {
        return "ShelfInfoBean{" +
                "shelfBean=" + shelfBean +
                ", Shelf_Allocations=" + Shelf_Allocations +
                '}';
    }

    public static class ShelfBean {
        @XStreamAlias("SFID")
        public String SFID;
        @XStreamAlias("RowCount")
        public String RowCount;
        @XStreamAlias("ACount")
        public String ACount;
        @XStreamAlias("Exist")
        public String Exist;

        @Override
        public String toString() {
            return "ShelfBean{" +
                    "SFID='" + SFID + '\'' +
                    ", RowCount='" + RowCount + '\'' +
                    ", ACount='" + ACount + '\'' +
                    ", Exist='" + Exist + '\'' +
                    '}';
        }
    }

    public static class Shelf_Allocation {
        @XStreamAlias("AID")
        public String AID;
        @XStreamAlias("Tinyip")
        public String Tinyip;
        @XStreamAlias("Barcode")
        public String Barcode;
        @XStreamAlias("GName")
        public String GName;
        @XStreamAlias("RowNumber")
        public String RowNumber;
        @XStreamAlias("ColNumber")
        public String ColNumber;


        @Override
        public String toString() {
            return "Shelf_Allocation{" +
                    "AID='" + AID + '\'' +
                    ", Tinyip='" + Tinyip + '\'' +
                    ", Barcode='" + Barcode + '\'' +
                    ", GName='" + GName + '\'' +
                    ", RowNumber='" + RowNumber + '\'' +
                    ", ColNumber='" + ColNumber + '\'' +
                    '}';
        }

    }
}

