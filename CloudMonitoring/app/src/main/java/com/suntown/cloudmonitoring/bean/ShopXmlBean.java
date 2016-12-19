package com.suntown.cloudmonitoring.bean;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * reated by Administrator on 2016/10/13.
 */
public class ShopXmlBean {
//    <TinyIP>7.0.0.42</TinyIP>
//    <SFID>571002002000002</SFID>
//    <Barcode>6920458824369</Barcode>
//    <GName>麒麟午后红茶原味500ml</GName>
//    <GCode>561140</GCode>
//    <Unit>瓶</Unit>
//    <Spec>500ml</Spec>
//    <Kind>140901</Kind>
//    <Origin>上海</Origin>
    //    <Oriprice>3</Oriprice>
    //    <uptPrice>3</uptPrice>
//    <MemPrice/>
//    <DispStr>层数x排面x纵深</DispStr>
//    <VBatchNO/>
//    <GStatus>0</GStatus>
//    <Battery>3040</Battery>
//    <LastDate>2016-06-13 10:38</LastDate>
//    <PowerOff>0</PowerOff>

    public String TinyIp;
    public String SFID;
    public String Barcode;
    public String GName;
    public String GCode;
    public String Unit;
    public String Spec;
    public String Kind;
    public String Origin;
    public String Oriprice;
    public String uptPrice;
    public String MemPrice;
    public String DispStr;
    public String CurStore;
    public String VBatchNO;
    public String GStatus;
    public String Battery;
    public String LastDate;
    public String PowerOff;


    public String getTinyIp() {

        return TinyIp;
    }

    public void setTinyIp(String tinyIp) {
        TinyIp = tinyIp;
    }

    public String getSFID() {
        return SFID;
    }

    public void setSFID(String SFID) {
        this.SFID = SFID;
    }

    public String getBarcode() {
        return Barcode;
    }

    public void setBarcode(String barcode) {
        Barcode = barcode;
    }

    public String getGName() {
        return GName;
    }

    public void setGName(String GName) {
        this.GName = GName;
    }

    public String getGCode() {
        return GCode;
    }

    public void setGCode(String GCode) {
        this.GCode = GCode;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getSpec() {
        return Spec;
    }

    public void setSpec(String spec) {
        Spec = spec;
    }

    public String getKind() {
        return Kind;
    }

    public void setKind(String kind) {
        Kind = kind;
    }

    public String getOrigin() {
        return Origin;
    }

    public void setOrigin(String origin) {
        Origin = origin;
    }

    public String getOriprice() {
        return Oriprice;
    }

    public void setOriprice(String oriprice) {
        Oriprice = oriprice;
    }

    public String getUptPrice() {
        return uptPrice;
    }

    public void setUptPrice(String uptPrice) {
        this.uptPrice = uptPrice;
    }

    public String getMemPrice() {
        return MemPrice;
    }

    public void setMemPrice(String memPrice) {
        MemPrice = memPrice;
    }

    public String getDispStr() {
        return DispStr;
    }

    public void setDispStr(String dispStr) {
        DispStr = dispStr;
    }

    public String getCurStore() {
        return CurStore;
    }

    public void setCurStore(String curStore) {
        CurStore = curStore;
    }

    public String getVBatchNO() {
        return VBatchNO;
    }

    public void setVBatchNO(String VBatchNO) {
        this.VBatchNO = VBatchNO;
    }

    public String getGStatus() {
        return GStatus;
    }

    public void setGStatus(String GStatus) {
        this.GStatus = GStatus;
    }

    public String getBattery() {
        return Battery;
    }

    public void setBattery(String battery) {
        Battery = battery;
    }

    public String getLastDate() {
        return LastDate;
    }

    public void setLastDate(String lastDate) {
        LastDate = lastDate;
    }

    public String getPowerOff() {
        return PowerOff;
    }

    public void setPowerOff(String powerOff) {
        PowerOff = powerOff;
    }

    @Override
    public String toString() {
        return "ShopXmlBean{" +
                "TinyIp='" + TinyIp + '\'' +
                ", SFID='" + SFID + '\'' +
                ", Barcode='" + Barcode + '\'' +
                ", GName='" + GName + '\'' +
                ", GCode='" + GCode + '\'' +
                ", Unit='" + Unit + '\'' +
                ", Spec='" + Spec + '\'' +
                ", Kind='" + Kind + '\'' +
                ", Origin='" + Origin + '\'' +
                ", Oriprice='" + Oriprice + '\'' +
                ", uptPrice='" + uptPrice + '\'' +
                ", MemPrice='" + MemPrice + '\'' +
                ", DispStr='" + DispStr + '\'' +
                ", CurStore='" + CurStore + '\'' +
                ", VBatchNO='" + VBatchNO + '\'' +
                ", GStatus='" + GStatus + '\'' +
                ", Battery='" + Battery + '\'' +
                ", LastDate='" + LastDate + '\'' +
                ", PowerOff='" + PowerOff + '\'' +
                '}';
    }
}
