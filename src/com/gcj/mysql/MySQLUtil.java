package com.gcj.mysql;

import com.gcj.bean.InstructionBean;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySQLUtil {

    private static final Log LOG = LogFactory.getLog(MySQLUtil.class);

    private String url;
    private String user;
    private String password;

    private Connection conn;

    private Statement stmt;

    private ResultSet rs;

    public MySQLUtil() {
    }

    public MySQLUtil(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
        registerClass();
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

    private void getConn() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void getStmt() {
        try {
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void closeConn() {
        try {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }


    public Map<String, String> selectFuncitonKeyMap() {
        Map<String, String> funcitonKeyMap = new HashMap<String, String>();
        String sSql = "select * from funciton_key_map";
        getConn();
        getStmt();
        try {
            rs = stmt.executeQuery(sSql);
            conn.commit();
            while (rs.next()) {
                String funKey = rs.getString("fun_key");
                String funUrl = rs.getString("fun_url");
                funcitonKeyMap.put(funKey, funUrl);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            closeConn();
        }
        return funcitonKeyMap;
    }

    public void insertFunctionUrlTest(List<InstructionBean> instructionBeanList) {
        PreparedStatement pstmt = null;
        String iSql = "insert into function_url_test(productId,Url,FunctionKey,FunctionName) values(?,?,?,?)";
        getConn();
        try {
            pstmt = conn.prepareStatement(iSql);
            int index;
            for (InstructionBean instructionBean : instructionBeanList) {
                pstmt.setInt(1, instructionBean.getProductId());
                pstmt.setString(2, instructionBean.getCmd());
                pstmt.setString(3, instructionBean.getKeyWords());
                pstmt.setString(4, instructionBean.getName());
                index = pstmt.executeUpdate();
                conn.commit();
                if (index != 0) {
                    LOG.info("更新成功-->" + index);
                } else {
                    StringBuffer sb = new StringBuffer();
                    sb.append("insert into function_url_test(productId,Url,FunctionKey,FunctionName) values(")
                            .append(instructionBean.getProductId()).append(",'").append(instructionBean.getCmd())
                            .append("','").append(instructionBean.getKeyWords()).append("','")
                            .append(instructionBean.getName()).append("')");
                    LOG.error("更新失败-->" + index);
                    LOG.error("ERROR SQL: " + sb.toString());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (pstmt != null)
                    pstmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public Map<String, String> selectFunctionUrlTest(int productId) {
        Map<String, String> funcitonKeyMap = new HashMap<String, String>();
        String sSql = "select Url,FunctionName from function_url_test where productId = " + productId;
        getConn();
        getStmt();
        try {
            rs = stmt.executeQuery(sSql);
            conn.commit();
            while (rs.next()) {
                String url = rs.getString(1);
                String functionName = rs.getString(2);
                funcitonKeyMap.put(url, functionName);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            closeConn();
        }
        return funcitonKeyMap;
    }

    public Map<String, String> getKeyWordsName(int productId) {
        Map<String, String> funcitonKeyMap = new HashMap<String, String>();
        String sSql = "select FunctionKey,FunctionName from function_url_test where productId = " + productId;
        getConn();
        getStmt();
        try {
            rs = stmt.executeQuery(sSql);
            conn.commit();
            while (rs.next()) {
                String functionKey = rs.getString(1);
                String functionName = rs.getString(2);
                funcitonKeyMap.put(functionKey, functionName);
            }
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            closeConn();
        }
        return funcitonKeyMap;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }
}
