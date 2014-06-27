package com.gcj.excel;

import com.gcj.bean.InstructionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaochuanjun on 14-2-18.
 */
public class FunctionUrlTestExcel {

    private static final Log LOG = LogFactory.getLog(FunctionUrlTestExcel.class);

    public List<InstructionBean> getContentFromStep1(String excelPath) {
        List<InstructionBean> instructionBeans = new ArrayList<InstructionBean>();
        File file = new File(excelPath);
        ExcelUtil excelUtil = new ExcelUtil();
        String[][] returnArray = excelUtil.getData(file, 1);
        int rowLength = returnArray.length;
        for (int i = 0; i < rowLength; i++) {


            InstructionBean instructionBean = new InstructionBean();


            int productId = Integer.parseInt(returnArray[i][0]);//版本号
            String keyWords = returnArray[i][1];  //快捷键
            String name = returnArray[i][2]; //模块名称
            String manager = returnArray[i][3]; //产品人员
            String engineer = returnArray[i][4];//开发人员
            int clientType = Integer.parseInt(returnArray[i][5]);//客户端类型
            String cmd = returnArray[i][6]; //URL
            String noVariablePara = returnArray[i][7]; //不可变参数
            String variablePara = returnArray[i][8]; //可变参数
            String outputPara = returnArray[i][9]; //输出参数


            instructionBean.setProductId(productId);//版本号
            instructionBean.setKeyWords(keyWords);//快捷键
            instructionBean.setName(name);//模块名称
            instructionBean.setManager(manager);//产品人员
            instructionBean.setEngineer(engineer);//开发人员
            instructionBean.setClientType(clientType);//客户端类型
            instructionBean.setNoVariablePara(noVariablePara);//不可变参数
            instructionBean.setVariablePara(variablePara);//可变参数
            instructionBean.setOutputPara(outputPara);//输出参数

            if (clientType == 1 || clientType == 12 || clientType == 101) { //当客户端类型为1，12，101时，URL不变
                instructionBean.setCmd(cmd);//URL
            } else if (clientType == 2) {
                int index = cmd.indexOf("&");
                int len = cmd.length();
                String str = cmd.substring(index, len);
                instructionBean.setCmd(str);
            } else if (clientType == 9) {
                if (noVariablePara != null && noVariablePara.length() > 0) {
                    int index = noVariablePara.indexOf("=");
                    String temp = noVariablePara.substring(0, index);
                    int begin = cmd.indexOf(temp);
                    int end = cmd.indexOf("&obj");
                    instructionBean.setCmd(cmd.substring(begin, end));
                } else if (cmd.contains("file:")) {
                    int begin = cmd.indexOf("/ui");
                    if (cmd.contains("obj=")) {
                        int end = cmd.indexOf("obj");
                        instructionBean.setCmd(cmd.substring(begin, end));
                    } else {
                        int end = cmd.length();
                        instructionBean.setCmd(cmd.substring(begin, end));
                    }
                } else if (cmd.contains("dzhd:")) {
                    if (cmd.contains("obj=")) {
                        int end = cmd.indexOf("obj=");
                        instructionBean.setCmd(cmd.substring(0, end));
                    } else {
                        int end = cmd.length();
                        instructionBean.setCmd(cmd.substring(0, end));
                    }
                } else if (cmd.contains("root:")) {
                    instructionBean.setCmd(cmd);
                } else {
                    LOG.error("clientType: " + clientType);
                    LOG.error(productId + " " + keyWords + " " + name + " " + manager + " " + engineer + " " + clientType + " " + cmd + " " + noVariablePara + " " + variablePara + " " + outputPara);
                }
            } else if (clientType == 15) {
                if (cmd.contains("obj=")) {
                    int end = cmd.indexOf("obj=");
                    instructionBean.setCmd(cmd.substring(0, end));
                } else if (cmd.contains("co=")) {
                    int end = cmd.indexOf("co=");
                    instructionBean.setCmd(cmd.substring(0, end));
                } else {
                    instructionBean.setCmd(cmd);
                }
            } else {
                LOG.error("客户端类型错误:  " + productId + " " + keyWords + " " + name + " " + manager + " " + engineer + " " + clientType + " " + cmd + " " + noVariablePara + " " + variablePara + " " + outputPara);
            }
            instructionBeans.add(instructionBean);
        }
        return instructionBeans;
    }

    public static void main(String[] args) {
        FunctionUrlTestExcel functionUrlTestExcel = new FunctionUrlTestExcel();
        List<InstructionBean> instructionBeans = functionUrlTestExcel.getContentFromStep1("商品-step1.xls");
        for (InstructionBean instructionBean : instructionBeans) {
            System.out.print(instructionBean.getProductId() + " " + instructionBean.getKeyWords() + " " + instructionBean.getName() + " " + instructionBean.getManager() + " " + instructionBean.getEngineer() + " " + instructionBean.getClientType() + " " + instructionBean.getCmd() + " " + instructionBean.getNoVariablePara() + " " + instructionBean.getVariablePara() + " " + instructionBean.getOutputPara() + "\r\n");
        }
    }
}
