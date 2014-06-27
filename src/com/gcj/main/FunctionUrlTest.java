package com.gcj.main;

import com.gcj.bean.InstructionBean;
import com.gcj.excel.FunctionUrlTestExcel;
import com.gcj.utils.ConfigUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by gaochuanjun on 14-2-19.
 */
public class FunctionUrlTest {

    private static final Log LOG = LogFactory.getLog(FunctionUrlTest.class);

    private String mySQLIPAddress;

    private String mySQLUser;

    private String mySQLPasswd;

    private String mySQLDBName;

    private String url;

    private Connection conn;

    public FunctionUrlTest() {
        super();
        registerClass();
    }

    private List<InstructionBean> selectFunctionUrlTest() {

        List<InstructionBean> instructionBeans = new ArrayList<InstructionBean>();

        String sSql = "select * from function_url_test";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sSql);
            while (rs.next()) {
                InstructionBean instructionBean = new InstructionBean();
                int productId = rs.getInt(1);
                String url = rs.getString(2);
                String functionKey = rs.getString(3);
                String functionName = rs.getString(4);
                instructionBean.setProductId(productId);
                instructionBean.setCmd(url);
                instructionBean.setKeyWords(functionKey);
                instructionBean.setName(functionName);
                instructionBeans.add(instructionBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null)
                    rs.close();
                if (stmt != null)
                    stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return instructionBeans;
    }

    private void registerClass() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getConf(String confPath) {
        ConfigUtils conf = new ConfigUtils(confPath);
        mySQLIPAddress = conf.getString("mysql_IPAddress");
        mySQLUser = conf.getString("mysql_user");
        mySQLPasswd = conf.getString("mysql_password");
        mySQLDBName = conf.getString("mysql_databaseName");
        url = "jdbc:mysql://" + mySQLIPAddress + "/" + mySQLDBName + "?useUnicode=true&characterEncoding=utf8";
    }

    public Set<String> convertMap(List<InstructionBean> instructionBeans) {
        Set<String> functionUrlTestSet = new HashSet<String>();
        for (InstructionBean instructionBean : instructionBeans) {
            functionUrlTestSet.add(instructionBean.getProductId() + ":" + instructionBean.getCmd());
        }
        return functionUrlTestSet;
    }

    public void startJob(String confPath, List<String> excelPaths) {
        getConf(confPath);
        connToMySQL();
        List<InstructionBean> instructionBeans = selectFunctionUrlTest();
        Set<String> functionUrlTestSet = convertMap(instructionBeans);
        FunctionUrlTestExcel functionUrlTestExcel = new FunctionUrlTestExcel();
        for (String excelPath : excelPaths) {
            List<InstructionBean> instructionBeanList = functionUrlTestExcel.getContentFromStep1(excelPath);
            for (InstructionBean instructionBean : instructionBeanList) {
                String key = instructionBean.getProductId() + ":" + instructionBean.getCmd();
                if (functionUrlTestSet.contains(key)) {
                    LOG.error(key + " has exists!");
                } else {
                    String iSql = "insert into function_url_test(productId,Url,FunctionKey,FunctionName) values(" + instructionBean.getProductId() + ",'" + instructionBean.getCmd() + "','" + instructionBean.getKeyWords() + "','" + instructionBean.getName() + "')";
                    LOG.info(iSql);
                }
            }
        }
        for (InstructionBean instructionBean : instructionBeans) {
            System.out.print(instructionBean.getProductId() + " " + instructionBean.getCmd() + " " + instructionBean.getKeyWords() + " " + instructionBean.getName() + "\r\n");
        }
    }

    private void connToMySQL() {
        try {
            conn = DriverManager.getConnection(url, mySQLUser, mySQLPasswd);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConn() {
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        FunctionUrlTest functionUrlTest = new FunctionUrlTest();
        List<String> excelPaths = new ArrayList<String>();
        excelPaths.add("人物-step1.xls");
        excelPaths.add("商品-step1.xls");
        excelPaths.add("宏观-step1.xls");
        excelPaths.add("新闻-step1.xls");
        excelPaths.add("行业-step1.xls");
        functionUrlTest.startJob("infoconfig.properties", excelPaths);
    }
}
