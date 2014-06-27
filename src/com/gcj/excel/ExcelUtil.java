package com.gcj.excel;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import com.gcj.mysql.MySQLUtil;
import com.gcj.utils.ConfigUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.gcj.bean.InstructionBean;

public class ExcelUtil {

    private static final Log LOG = LogFactory.getLog(ExcelUtil.class);

    public String[][] getData(File file, int ignoreRows) {

        List<String[]> result = new ArrayList<String[]>();
        int rowSize = 0;
        BufferedInputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            POIFSFileSystem fs = new POIFSFileSystem(in);
            HSSFWorkbook wb = new HSSFWorkbook(fs);
            HSSFCell cell;
            for (int sheetIndex = 0; sheetIndex < wb.getNumberOfSheets(); sheetIndex++) {
                HSSFSheet st = wb.getSheetAt(sheetIndex);
                for (int rowIndex = ignoreRows; rowIndex <= st.getLastRowNum(); rowIndex++) {
                    HSSFRow row = st.getRow(rowIndex);
                    if (row == null) {
                        continue;
                    }
                    int tempRowSize = row.getLastCellNum() + 1;
                    if (tempRowSize > rowSize) {
                        rowSize = tempRowSize;
                    }
                    String[] values = new String[rowSize];
                    Arrays.fill(values, "");
                    boolean hasValue = false;
                    for (short columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                        String value = "";
                        cell = row.getCell(columnIndex);
                        if (cell != null) {
                            cell.setEncoding(HSSFCell.ENCODING_UTF_16);
                            switch (cell.getCellType()) {
                                case HSSFCell.CELL_TYPE_STRING:
                                    value = cell.getStringCellValue();
                                    break;
                                case HSSFCell.CELL_TYPE_NUMERIC:
                                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                                        Date date = cell.getDateCellValue();
                                        if (date != null) {
                                            value = new SimpleDateFormat("yyyy-MM-dd").format(date);
                                        } else {
                                            value = "";
                                        }
                                    } else {
                                        value = new DecimalFormat("0").format(cell.getNumericCellValue());
                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_FORMULA:
                                    if (!cell.getStringCellValue().equals("")) {
                                        value = cell.getStringCellValue();
                                    } else {
                                        value = cell.getNumericCellValue() + "";

                                    }
                                    break;
                                case HSSFCell.CELL_TYPE_BLANK:
                                    break;
                                case HSSFCell.CELL_TYPE_ERROR:
                                    value = "";
                                    break;
                                case HSSFCell.CELL_TYPE_BOOLEAN:
                                    value = (cell.getBooleanCellValue() == true ? "Y" : "N");
                                    break;
                                default:
                                    value = "";
                            }
                        }
                        if (columnIndex == 0 && value.trim().equals("")) {
                            break;
                        }
                        values[columnIndex] = rightTrim(value);
                        hasValue = true;
                    }
                    if (hasValue) {
                        result.add(values);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
        String[][] returnArray = new String[result.size()][rowSize];
        for (int i = 0; i < returnArray.length; i++) {
            returnArray[i] = result.get(i);
        }
        return returnArray;
    }


    /**
     * 去掉字符串右边的空格
     *
     * @param str 要处理的字符串
     * @return 处理后的字符串
     */

    public String rightTrim(String str) {
        if (str == null) {
            return "";
        }
        int length = str.length();
        for (int i = length - 1; i >= 0; i--) {
            if (str.charAt(i) != 0x20) {
                break;
            }
            length--;
        }
        return str.substring(0, length);
    }

    public List<InstructionBean> getInfoFromExcel(String filePath) {
        List<InstructionBean> instructionBeans = new ArrayList<InstructionBean>();
        File file = new File(filePath);
        String[][] returnArray = getData(file, 1);
        int rowLength = returnArray.length;
        for (int i = 0; i < rowLength; i++) {
            InstructionBean instructionBean = new InstructionBean();
            int productId = Integer.parseInt(returnArray[i][0]);
            String keyWords = returnArray[i][1];
            String name = returnArray[i][2];
            String manager = returnArray[i][3];
            String engineer = returnArray[i][4];
            int clientType = Integer.parseInt(returnArray[i][5]);
            String cmd = returnArray[i][6];
            String noVariablePara = returnArray[i][7];
            String variablePara = returnArray[i][8];
            String outputPara = returnArray[i][9];
            instructionBean.setProductId(productId);
            instructionBean.setKeyWords(keyWords);
            instructionBean.setName(name);
            instructionBean.setManager(manager);
            instructionBean.setEngineer(engineer);
            instructionBean.setClientType(clientType);
            instructionBean.setNoVariablePara(noVariablePara);
            instructionBean.setVariablePara(variablePara);
            instructionBean.setOutputPara(outputPara);
            instructionBeans.add(instructionBean);
            if (clientType == 1 || clientType == 12 || clientType == 101) {
                instructionBean.setCmd(cmd);
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
        }
        return instructionBeans;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        ExcelUtil excelUtil = new ExcelUtil();
        List<InstructionBean> instructionBeans = excelUtil.getInfoFromExcel("三个环境指令整理20131206-外网.xls");
        ConfigUtils conf = new ConfigUtils("infoconfig.properties");
        String mySQLIPAddress = conf.getString("mysql_IPAddress");
        String mySQLUser = conf.getString("mysql_user");
        String mySQLPassword = conf.getString("mysql_password");
        String mySQLDatabaseName = conf.getString("mysql_databaseName");
        String url = "jdbc:mysql://" + mySQLIPAddress + "/" + mySQLDatabaseName + "?useUnicode=true&characterEncoding=utf8";
        LOG.info("MySQL数据库url：" + url);
        LOG.info("MySQL数据库用户名：" + mySQLUser);
        LOG.info("MySQL数据库密码：" + mySQLPassword);

        Set<String> cmdSet = new HashSet<String>();

        MySQLUtil mySQLUtil = new MySQLUtil(url, mySQLUser, mySQLPassword);

        mySQLUtil.insertFunctionUrlTest(instructionBeans);

        for (InstructionBean instructionBean : instructionBeans) {
            int productId = instructionBean.getProductId();
            String keyWords = instructionBean.getKeyWords();
            String name = instructionBean.getName();
            String manager = instructionBean.getManager();
            String engineer = instructionBean.getEngineer();
            int clientType = instructionBean.getClientType();
            String cmd = instructionBean.getCmd();
            String noVariablePara = instructionBean.getNoVariablePara();
            String variablePara = instructionBean.getVariablePara();
            String outputPara = instructionBean.getOutputPara();
            if (productId == 112) {

            } else if (productId == 113) {

            } else if (productId == 116) {

            } else {
                LOG.error("版本号错误！");
            }
            if (cmdSet.contains(cmd)) {
                LOG.info(productId + " " + keyWords + " " + name + " " + manager + " " + engineer + " " + clientType + " " + cmd + " " + noVariablePara + " " + variablePara + " " + outputPara);
            } else {
                cmdSet.add(productId + ":" + cmd);
            }
        }
    }
}
