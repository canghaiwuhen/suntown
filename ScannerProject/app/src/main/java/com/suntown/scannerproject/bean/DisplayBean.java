package com.suntown.scannerproject.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Administrator on 2016/12/1.
 */
@XStreamAlias("DispData")
public class DisplayBean {
//                <DispData>
//                  <Tip>0.0.91.215</Tip>
//                <Barcode>6925303721404</Barcode>
//                <DispRow>1</DispRow>
//                  <DispCol>1</DispCol>
//                <DispDepths>9</DispDepths>
//                <AlterCnt />
//                <GNAME>统一冰绿茶饮料500ml</GNAME>
//                </DispData>
    @XStreamAlias("Tip")
    public String Tip;
    @XStreamAlias("Barcode")
    public String Barcode;
    @XStreamAlias("DispRow")
    public String DispRow;
    @XStreamAlias("DispCol")
    public String DispCol;
    @XStreamAlias("DispDepths")
    public String DispDepths;
    @XStreamAlias("AlterCnt")
    public String AlterCnt;
    @XStreamAlias("GNAME")
    public String GNAME;

    public DisplayBean() {
    }

    public DisplayBean(String tip, String barcode, String dispRow, String dispCol, String dispDepths, String alterCnt, String GNAME) {
        Tip = tip;
        Barcode = barcode;
        DispRow = dispRow;
        DispCol = dispCol;
        DispDepths = dispDepths;
        AlterCnt = alterCnt;
        this.GNAME = GNAME;
    }

    @Override
    public String toString() {
        return "DisplayBean{" +
                "Tip='" + Tip + '\'' +
                ", Barcode='" + Barcode + '\'' +
                ", DispRow='" + DispRow + '\'' +
                ", DispCol='" + DispCol + '\'' +
                ", DispDepths='" + DispDepths + '\'' +
                ", AlterCnt='" + AlterCnt + '\'' +
                ", GNAME='" + GNAME + '\'' +
                '}';
    }
}
