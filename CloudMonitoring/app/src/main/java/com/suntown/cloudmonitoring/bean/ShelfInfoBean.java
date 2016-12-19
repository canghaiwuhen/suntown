package com.suntown.cloudmonitoring.bean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */

public class ShelfInfoBean {

    /**
     * RowCount 层
     * ACount 列
     */
//    <Data>
//      <Shelf>
//          <SFID>K02G-1-14-1-012</SFID>
//          <RowCount>1</RowCount>
//          <ACount>13</ACount>
//      </Shelf>
//      <Shelf_Allocation>
//          <Shelf_Allocations>
//              <AID>K02G-1-14-1-012-1-1</AID>
//              <Tinyip/>
//              <Barcode>0000000513760</Barcode>
//              <GName>M&amp;M'Sm&amp;ms迷你筒装巧克力30.6g30.6g</GName>
//               <RowNumber>1</RowNumber>
//              <ColNumber>1</ColNumber>
//          </Shelf_Allocations>
//          <Shelf_Allocations>
//              <AID>K02G-1-14-1-012-1-10</AID>
//              <Tinyip>127.0.2.1</Tinyip>
//              <Barcode/>
//              <GName/>
//              <RowNumber>1</RowNumber>
//              <ColNumber>10</ColNumber>
//          </Shelf_Allocations>
//        </Shelf_Allocation>
//      </Data>
    public ShelfBean shelfBean;
    public List<Shelf_Allocation> Shelf_Allocations;

    public void setShelfBean(ShelfBean shelfBean) {
        this.shelfBean = shelfBean;
    }

    public void setShelf_Allocations(List<Shelf_Allocation> shelf_Allocations) {
        Shelf_Allocations = shelf_Allocations;
    }

    @Override
    public String toString() {
        return "ShelfInfoBean{" +
                "shelfBean=" + shelfBean +
                ", Shelf_Allocations=" + Shelf_Allocations +
                '}';
    }

    public static class ShelfBean {
        public String SFID;
        public String RowCount;
        public String ACount;
        public String Exist;


        public void setSFID(String SFID) {
            this.SFID = SFID;
        }

        public void setRowCount(String rowCount) {
            RowCount = rowCount;
        }

        public void setACount(String ACount) {
            this.ACount = ACount;
        }

        public void setExist(String exist) {
            Exist = exist;
        }

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
        public String AID;
        public String Tinyip;
        public String Barcode;
        public String GName;
        public String RowNumber;
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

        public void setAID(String AID) {
            this.AID = AID;
        }

        public void setTinyip(String tinyip) {
            Tinyip = tinyip;
        }

        public void setBarcode(String barcode) {
            Barcode = barcode;
        }

        public void setGName(String GName) {
            this.GName = GName;
        }

        public void setRowNumber(String rowNumber) {
            RowNumber = rowNumber;
        }

        public void setColNumber(String colNumber) {
            ColNumber = colNumber;
        }


    }
}

//    <?xml version="1.0" encoding="gb2312"?>
//<Data>
//<Shelf>
//<SFID>K02G-1-14-1-001</SFID>
//<RowCount>4</RowCount>
//<ACount>21</ACount>
//</Shelf>
//<Shelf_Allocation>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-01-02</AID>
//<Tinyip>6.0.56.56</Tinyip>
//<Barcode>6921168532049</Barcode>
//<GName>农夫番茄/山楂/草莓饮料500ml</GName>
//<RowNumber>1</RowNumber>
//<ColNumber>2</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-01-03</AID>
//<Tinyip>6.0.72.72</Tinyip>
//<Barcode>6921168532049</Barcode>
//<GName>农夫番茄/山楂/草莓饮料500ml</GName>
//<RowNumber>1</RowNumber>
//<ColNumber>3</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-02-01</AID>
//<Tinyip>6.0.232.232</Tinyip>
//<Barcode>6920459998823</Barcode>
//<GName>康师傅冰红茶2升</GName>
//<RowNumber>2</RowNumber>
//<ColNumber>1</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-02-02</AID>
//<Tinyip>7.0.0.33</Tinyip>
//<Barcode>6921168500970</Barcode>
//<GName>农夫水溶C100西柚汁饮料445ml</GName>
//<RowNumber>2</RowNumber>
//<ColNumber>2</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-02-03</AID>
//<Tinyip>6.0.105.105</Tinyip>
//<Barcode>6921168502066</Barcode>
//<GName>农夫山泉饮用天然水量贩装550ml*12</GName>
//<RowNumber>2</RowNumber>
//<ColNumber>3</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-02-04</AID>
//<Tinyip>6.0.70.70</Tinyip>
//<Barcode>6920459993798</Barcode>
//<GName>康师傅茉莉蜜茶500ml</GName>
//<RowNumber>2</RowNumber>
//<ColNumber>4</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-02-05</AID>
//<Tinyip>6.0.136.136</Tinyip>
//<Barcode>6920459999295</Barcode>
//<GName>康师傅绿茶1.25升</GName>
//<RowNumber>2</RowNumber>
//<ColNumber>5</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-02-06</AID>
//<Tinyip>7.0.0.24</Tinyip>
//<Barcode>6921168550098</Barcode>
//<GName>农夫山泉力量帝维他命水饮品(蓝莓-树莓)500ml</GName>
//<RowNumber>2</RowNumber>
//<ColNumber>6</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-03-01</AID>
//<Tinyip>7.0.0.78</Tinyip>
//<Barcode>6902083882099</Barcode>
//<GName>娃哈哈纯净水1.25升</GName>
//<RowNumber>3</RowNumber>
//<ColNumber>1</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-03-02</AID>
//<Tinyip>7.0.0.79</Tinyip>
//<Barcode>6902083896591</Barcode>
//<GName>娃哈哈启力维生素饮料250毫升</GName>
//<RowNumber>3</RowNumber>
//<ColNumber>2</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-03-03</AID>
//<Tinyip>7.0.0.77</Tinyip>
//<Barcode>6901424334228</Barcode>
//<GName>王老吉凉茶六连包饮料250ml*6</GName>
//<RowNumber>3</RowNumber>
//<ColNumber>3</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-03-04</AID>
//<Tinyip>7.0.0.64</Tinyip>
//<Barcode>6902083880675</Barcode>
//<GName>娃哈哈纯净水350毫升</GName>
//<RowNumber>3</RowNumber>
//<ColNumber>4</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-03-05</AID>
//<Tinyip>7.0.0.65</Tinyip>
//<Barcode>6901285991240</Barcode>
//<GName>怡宝纯净水350ml</GName>
//<RowNumber>3</RowNumber>
//<ColNumber>5</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-01</AID>
//<Tinyip>7.0.0.59</Tinyip>
//<Barcode>6902083893163</Barcode>
//<GName>娃哈哈柚饮料450毫升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>1</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-02</AID>
//<Tinyip>7.0.0.71</Tinyip>
//<Barcode>6902083882099</Barcode>
//<GName>娃哈哈纯净水1.25升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>2</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-03</AID>
//<Tinyip>7.0.0.68</Tinyip>
//<Barcode>6902083896591</Barcode>
//<GName>娃哈哈启力维生素饮料250毫升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>3</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-04</AID>
//<Tinyip>7.0.0.62</Tinyip>
//<Barcode>6902083896591</Barcode>
//<GName>娃哈哈启力维生素饮料250毫升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>4</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-05</AID>
//<Tinyip>7.0.0.56</Tinyip>
//<Barcode>6902083896591</Barcode>
//<GName>娃哈哈启力维生素饮料250毫升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>5</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-06</AID>
//<Tinyip>7.0.0.46</Tinyip>
//<Barcode>6902083896591</Barcode>
//<GName>娃哈哈启力维生素饮料250毫升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>6</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-07</AID>
//<Tinyip>7.0.0.41</Tinyip>
//<Barcode>6902083880675</Barcode>
//<GName>娃哈哈纯净水350毫升</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>7</ColNumber>
//</Shelf_Allocations>
//<Shelf_Allocations>
//<AID>K02G-1-14-1-001-04-08</AID>
//<Tinyip>7.0.0.37</Tinyip>
//<Barcode>6901424334228</Barcode>
//<GName>王老吉凉茶六连包饮料250ml*6</GName>
//<RowNumber>4</RowNumber>
//<ColNumber>8</ColNumber>
//</Shelf_Allocations>
//</Shelf_Allocation>
//</Data>
