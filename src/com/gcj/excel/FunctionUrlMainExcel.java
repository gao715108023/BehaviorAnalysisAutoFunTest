package com.gcj.excel;

import com.gcj.bean.FunctionUrlMainBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaochuanjun on 14-2-18.
 */
public class FunctionUrlMainExcel {

    private static final Log LOG = LogFactory.getLog(FunctionUrlMainExcel.class);

    public List<FunctionUrlMainBean> getContentFromStep2(String excelPath) {
        List<FunctionUrlMainBean> functionUrlMainBeans = new ArrayList<FunctionUrlMainBean>();
        File file = new File(excelPath);
        ExcelUtil excelUtil = new ExcelUtil();
        String[][] returnArray = excelUtil.getData(file, 1);
        int rowLength = returnArray.length;
        for (int i = 0; i < rowLength; i++) {
            FunctionUrlMainBean functionUrlMainBean = new FunctionUrlMainBean();
            String shortcutKey = returnArray[i][0];
            int clientType = Integer.parseInt(returnArray[i][1]);
            String baseurl = returnArray[i][2];
            String parser = returnArray[i][3];
            String parameter = returnArray[i][4];
            System.out.print(shortcutKey + " " + clientType + " " + baseurl + " " + parser + " " + parameter + "\r\n");
            functionUrlMainBean.setFunType(shortcutKey);
            functionUrlMainBean.setClientType(clientType);
            functionUrlMainBean.setBaseUrl(baseurl);
            functionUrlMainBean.setParserName(parser);
            //给的excel与MySQL不匹配

        }
        return functionUrlMainBeans;
    }

    public static void main(String[] args) {
        FunctionUrlMainExcel functionUrlMainExcel = new FunctionUrlMainExcel();
        functionUrlMainExcel.getContentFromStep2("商品-step2.xls");
    }
}