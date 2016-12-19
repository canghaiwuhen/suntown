package com.suntown.scannerproject.scanner.util;


import com.suntown.scannerproject.scanner.bean.ShelfItemBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/10/28.
 */
public class CommitShelfData {
    /**
     * 将listMap集合转换成xml
     * @param indexList
     * @param listMap
     * @return
     * 列数，货架，标签，SCANID 唯一标识，aid具体位置,sid,barcode,scandate,行数
     */
    public static String productData(List<Integer> indexList, Map<Integer, List<ShelfItemBean>> listMap, String sfid, String sid, String scanid) {

        String xml="<ScanData>";
       String RowNumber ;
        for (int i = 0; i < indexList.size(); i++) {
                int row = indexList.size()-i;
            List<ShelfItemBean> shelfItemBeanList = listMap.get(indexList.get(i));
                for (int y = 0; y < shelfItemBeanList.size(); y++) {
                    int colum = y+1;
                    RowNumber=row<10?"0"+row:row+"";
                    String ColNumber = colum<10?"0"+colum:colum+"";
                    String aid = sfid+"-"+RowNumber+"-"+ColNumber;
                    ShelfItemBean shelfItemBean = shelfItemBeanList.get(y);
                    String barcode = shelfItemBean.barcode;
                    String gname = shelfItemBean.gname;
                    String tag = shelfItemBean.tag;
                    if (!"".equals(tag)||!"".equals(barcode)){
                        SimpleDateFormat dateFormater = new SimpleDateFormat("yyyy-MM-dd");
                        Date date=new Date();
                        String currentDate = dateFormater.format(date);
                        xml+="<ScanDatas>";
                        xml+="<ColNumber>"+ColNumber+"</ColNumber>";
                        xml+="<SfID>"+sfid+"</SfID>";
                        xml+="<TIP>"+tag+"</TIP>";
                        xml+="<SCANID>"+scanid+"</SCANID>";
                        xml+="<AID>"+aid+"</AID>";
                        xml+="<SID>"+sid+"</SID>";
                        xml+="<Barcode>"+barcode+"</Barcode>";
                        xml+="<ScanDate>"+currentDate+"</ScanDate>";
                        xml+="<RowNumber>"+RowNumber+"</RowNumber>";
                        xml+="</ScanDatas>";
                    }
                }
            }
        xml+="</ScanData>";
        return xml;
    }
}
