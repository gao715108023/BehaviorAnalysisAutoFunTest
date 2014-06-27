package com.gcj.main;

import com.gcj.bean.FunctionUrlMainBean;
import com.gcj.utils.ConfigUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gaochuanjun on 14-2-17.
 */
public class FunctionUrlMain {

    private String mySQLIPAddress;

    private String mySQLUser;

    private String mySQLPasswd;

    private String mySQLDBName;

    private String url;

    private Connection conn;

    public FunctionUrlMain() {
        super();
        registerClass();
    }

    private List<FunctionUrlMainBean> selectFunctionUrlMain() {
        List<FunctionUrlMainBean> functionUrlMainBeans = new ArrayList<FunctionUrlMainBean>();
        String sSql = "select * from function_url_main";
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sSql);
            while (rs.next()) {
                FunctionUrlMainBean functionUrlMainBean = new FunctionUrlMainBean();
                String funType = rs.getString(1);
                int clientType = rs.getInt(2);
                String baseUrl = rs.getString(3);
                String parserName = rs.getString(4);
                String params = rs.getString(5);
                String paramsGet = rs.getString(6);
                functionUrlMainBean.setFunType(funType);
                functionUrlMainBean.setClientType(clientType);
                functionUrlMainBean.setBaseUrl(baseUrl);
                functionUrlMainBean.setParserName(parserName);
                functionUrlMainBean.setParams(params);
                functionUrlMainBean.setParamsGet(paramsGet);
                functionUrlMainBeans.add(functionUrlMainBean);
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
        return functionUrlMainBeans;
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


    private void getConf(String confPath) {
        ConfigUtils conf = new ConfigUtils(confPath);
        mySQLIPAddress = conf.getString("mysql_IPAddress");
        mySQLUser = conf.getString("mysql_user");
        mySQLPasswd = conf.getString("mysql_password");
        mySQLDBName = conf.getString("mysql_databaseName");
        url = "jdbc:mysql://" + mySQLIPAddress + "/" + mySQLDBName + "?useUnicode=true&characterEncoding=utf8";
    }

    public void startJob(String confPath) {
        getConf(confPath);
        connToMySQL();
        List<FunctionUrlMainBean> functionUrlMainBeans = selectFunctionUrlMain();
        for (FunctionUrlMainBean functionUrlMainBean : functionUrlMainBeans) {
            System.out.print(functionUrlMainBean.getFunType() + " " + functionUrlMainBean.getClientType() + " " + functionUrlMainBean.getBaseUrl() + " " + functionUrlMainBean.getParserName() + " " + functionUrlMainBean.getParams() + " " + functionUrlMainBean.getParamsGet() + "\r\n");
        }
    }

    public static void main(String[] args) {
        FunctionUrlMain functionUrlMain = new FunctionUrlMain();
        functionUrlMain.startJob("infoconfig.properties");
    }
}