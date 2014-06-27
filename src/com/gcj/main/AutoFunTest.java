package com.gcj.main;

import com.gcj.bean.FunctionDayBean;
import com.gcj.bean.InfcltBean;
import com.gcj.bean.UserOperationLogBean;
import com.gcj.file.CSVUtils;
import com.gcj.file.FileUtil;
import com.gcj.mysql.MySQLUtil;
import com.gcj.utils.ConfigUtils;
import com.gcj.utils.DoubleUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;

public class AutoFunTest {

    private static final Log LOG = LogFactory.getLog(MySQLUtil.class);

    private MySQLUtil mySQLUtil;

    private FileUtil fileUtil;

    public AutoFunTest(MySQLUtil mySQLUtil, FileUtil fileUtil) {
        this.mySQLUtil = mySQLUtil;
        this.fileUtil = fileUtil;
    }

    public void testFunctionDay(String srcFile, int productId, String userId, String destFile) {

        LOG.info("从" + srcFile + "文件中读取信息，请等待......");
        List<FunctionDayBean> functionDayBeans = fileUtil.getFunctionDay(srcFile);
        if (functionDayBeans.size() == 0) {
            LOG.error(srcFile + "中的内容为空！请检查文件中是否有内容！");
        } else {
            LOG.info("读取完成！共有" + functionDayBeans.size() + "行数据！");
        }

        LOG.info("从《三个环境指令整理.xls》中读取每个url每个对应的功能名称，请等待......");
        Map<String, String> funcitonKeyMap = mySQLUtil.selectFunctionUrlTest(productId);
        LOG.info("读取完成！具体的对应关系如下：");
        LOG.info(funcitonKeyMap);

        Set<String> urlKey = funcitonKeyMap.keySet();
        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        LOG.info("查询表function_key_map，获取每个MD5码对应的url，请等待......");
        Map<String, String> oldFuncitonKeyMap = mySQLUtil.selectFuncitonKeyMap();
        LOG.info("读取完成！具体的MD5码与url的对应关系如下：");
        LOG.info(oldFuncitonKeyMap);

        for (FunctionDayBean functionDayBean : functionDayBeans) {

            if (!functionDayBean.getUserid().equals(userId)) {
                continue;
            }

            //如果csv中的MD5码在表function_key_map中存在，映射每个MD5与url的关系
            if (oldFuncitonKeyMap.containsKey(functionDayBean.getMd5())) {
                functionDayBean.setUrl(oldFuncitonKeyMap.get(functionDayBean.getMd5()));
            }
        }

        List<String> errorURList = new ArrayList<String>();
        for (FunctionDayBean functionDayBean : functionDayBeans) {
            if (!functionDayBean.getUserid().equals(userId)) {
                continue;
            }

            if (functionDayBean.getUrl() == null) {
                LOG.warn("在数据库中未发现与之对应的MD5码！");
                LOG.warn("userid: " + functionDayBean.getUserid() + " MD5：" + functionDayBean.getMd5());
                continue;
            }

            boolean b = false;
            for (String key : urlKey) {

                if (functionDayBean.getUrl().contains(key)) {
                    resultMap.put(funcitonKeyMap.get(key), Integer.parseInt(functionDayBean.getCount()));
                    b = true;
                }
            }

            if (!b) {
                errorURList.add(functionDayBean.getUrl());
            }

            if (b) {
            } else {
                System.err.println(functionDayBean.getMd5());
            }

        }

        LOG.info("正在将结果数据存储至" + destFile + ",请等待......");
        CSVUtils.writeCSV(destFile, resultMap);
        LOG.info("存储完成！");

        if (errorURList.size() == 0) {
            LOG.info("所有的url均已匹配完成，未发现异常的url！");
        } else {
            LOG.warn("在《三个环境指令整理.xls》中未发现与之匹配的url，请人工检查是否正确！");
            for (String url : errorURList) {
                LOG.warn(url);
            }
        }
    }
    

    private void testInfclt(String infcltPath, int productId, String infcltDestPath) {

        LOG.info("查询表function_url_test，得到每个url对应的具体的功能名称。");
        Map<String, String> funUrlTestMap = mySQLUtil.selectFunctionUrlTest(productId);
        LOG.info(funUrlTestMap);

        LOG.info("读取" + infcltPath + "文件中的内容");
        List<InfcltBean> infcltBeans = fileUtil.getInfoInfclt(infcltPath);

        if (infcltBeans != null && infcltBeans.size() != 0) {
            LOG.info("读取完成！共" + infcltBeans.size() + "行数据！");
        } else {
            LOG.error(infcltPath + "中的内容为空，请检查文件中是否有内容！");
            return;
        }

        Set<String> urlKey = funUrlTestMap.keySet();

        Map<String, Integer> resultMap = new HashMap<String, Integer>();

        List<String> errorUrlList = new ArrayList<String>();

        for (InfcltBean infcltBean : infcltBeans) {

            boolean b = true;

            if (infcltBean.getClientType() == 1 || infcltBean.getClientType() == 12 || infcltBean.getClientType() == 101) {
                for (String key : urlKey) {
                    if (infcltBean.getUrl().equalsIgnoreCase(key)) {
                        if (resultMap.containsKey(funUrlTestMap.get(key))) {
                            int count = resultMap.get(funUrlTestMap.get(key));
                            count++;
                            resultMap.put(funUrlTestMap.get(key), count);
                        } else {
                            resultMap.put(funUrlTestMap.get(key), 1);
                        }
                        b = true;
                    }
                }
            } else {
                for (String key : urlKey) {
                    if (key.equalsIgnoreCase("i") || key.equalsIgnoreCase("o"))
                        continue;
                    if (infcltBean.getUrl().contains(key)) {
                        if (resultMap.containsKey(funUrlTestMap.get(key))) {
                            int count = resultMap.get(funUrlTestMap.get(key));
                            count++;
                            resultMap.put(funUrlTestMap.get(key), count);
                        } else {
                            resultMap.put(funUrlTestMap.get(key), 1);
                        }
                        b = true;
                    }
                }
            }
            if (b) {
            } else {
                errorUrlList.add(infcltBean.getUrl());
            }
        }
        LOG.info("正在将结果数据存储至" + infcltDestPath + ",请等待......");
        CSVUtils.writeCSV(infcltDestPath, resultMap);
        LOG.info("存储完成！");

        if (errorUrlList.size() == 0) {
            LOG.info("所有的url均已匹配完成，未发现异常的url！");
        } else {
            LOG.warn("在《三个环境指令整理.xls》中未发现与之匹配的url，请人工检查是否正确！");
            for (String url : errorUrlList) {
                LOG.warn(url);
            }
        }
    }

    /**
     * 判断客户端生成的日志与服务端的日志是否一致
     *
     * @param infcltPath
     * @param userOperationLogPath
     * @param userid
     */
    public void judgeSrcDest(String infcltPath, String userOperationLogPath, String userid) {

        List<InfcltBean> infcltBeans = fileUtil.getInfoInfclt(infcltPath);

        List<UserOperationLogBean> userOperationLogBeans = FileUtil.getUserOperationLog(userOperationLogPath, userid);
        System.out.println("client: total" + infcltBeans.size());
        System.out.println("server: total" + userOperationLogBeans.size());
//        Date begin = DateUtils.getDate("20150101000000");
//        Date end = DateUtils.getDate("20120101000000");
//        for (InfcltBean infcltBean : infcltBeans) {
//            if (infcltBean.getUpdateTime().before(begin))
//                begin = infcltBean.getUpdateTime();
//            if (infcltBean.getUpdateTime().after(end))
//                end = infcltBean.getUpdateTime();
//        }
//        int total = 0;
//        for (UserOperationLogBean userOperationLogBean : userOperationLogBeans) {
//            if ((userOperationLogBean.getUpdateTime().after(end) && userOperationLogBean.getUpdateTime().before(begin) || userOperationLogBean.getUpdateTime().equals(end) || userOperationLogBean.getUpdateTime().equals(begin)))
//                total++;
//
//        }
        if (userOperationLogBeans.size() != infcltBeans.size()) {
            if (userOperationLogBeans.size() > infcltBeans.size()) {
                System.err.println("服务端的数据采集超过了客户端的采集，客户端有漏掉记录日志的情况！查看客户端的配置文件是否打开了DEBUG模式！");
            } else {
                System.err.println("服务端未采集到客户端发送的所有日志记录！");
                System.err.println("客户端：" + infcltBeans.size());
                System.err.println("服务端：" + userOperationLogBeans.size());
                double error = (double) (infcltBeans.size() - userOperationLogBeans.size()) / (double) infcltBeans.size();
                System.out.println("错误率：" + DoubleUtils.convert(error * 100) + "%");
            }
        }
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        ConfigUtils conf = new ConfigUtils("infoconfig.properties");
        String mySQLIPAddress = conf.getString("mysql_IPAddress");
        String mySQLUser = conf.getString("mysql_user");
        String mySQLPassword = conf.getString("mysql_password");
        String mySQLDatabaseName = conf.getString("mysql_databaseName");
        String infcltFilePath = conf.getString("infclt_path");
        int clientVersion = conf.getInt("test_client_version");
        String userId = conf.getString("user_" + clientVersion);
        String functionDayPath = conf.getString("function_day_path");
        String functionDayDestPath = conf.getString("function_day_dest_path");
        String dataType = conf.getString("data_type");
        String infcltDestPath = conf.getString("infclt_dest_path");
        String url = "jdbc:mysql://" + mySQLIPAddress + "/" + mySQLDatabaseName + "?useUnicode=true&characterEncoding=utf8";
        LOG.info("MySQL数据库url：" + url);
        LOG.info("MySQL数据库用户名：" + mySQLUser);
        LOG.info("MySQL数据库密码：" + mySQLPassword);
        LOG.info("infclt.txt的文件路径为：" + infcltFilePath);
        LOG.info("infclt.csv的文件路径为：" + infcltDestPath);
        LOG.info("function_day_*.csv文件路径：" + functionDayPath);
        LOG.info("function_day_dest_path文件路径：" + functionDayDestPath);
        LOG.info("测试客户端的版本号：" + clientVersion);
        LOG.info("登录客户端的账户名：" + userId);
        LOG.info("客户端类型：" + dataType);
        MySQLUtil mySQLUtil = new MySQLUtil(url, mySQLUser, mySQLPassword);
        FileUtil fileUtil1 = new FileUtil();
        Set<Integer> dataTypeSet = new HashSet<Integer>();
        String[] array = dataType.split(",");
        for (String str : array) {
            dataTypeSet.add(Integer.parseInt(str));
        }
        AutoFunTest autoFunTest = new AutoFunTest(mySQLUtil, fileUtil1);
//        autoFunTest.start(args[0], args[1]);
        //autoFunTest.judgeSrcDest("D:\\test\\batest\\infclt.txt", "D:\\test\\batest\\user-operation.log.20131220.000002", "dzhdv116_224");
        if (args == null || args.length == 0) {
            autoFunTest.testInfclt(infcltFilePath, clientVersion, infcltDestPath);
            autoFunTest.testFunctionDay(functionDayPath, clientVersion, userId, functionDayDestPath);
        } else if (args[0].equalsIgnoreCase("0")) {
            autoFunTest.testInfclt(infcltFilePath, clientVersion, infcltDestPath);
        } else if (args[0].equalsIgnoreCase("1")) {
            autoFunTest.testFunctionDay(functionDayPath, clientVersion, userId, functionDayDestPath);
        }

        LOG.info("所有的运算过程均已经运行完毕！谢谢！");
        try {
            Thread.sleep(1000 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //autoFunTest.testFunctionDay("D:\\test\\用户行为分析测试\\function_day_20131220.csv", 112, "dzhtestw008");
    }
}
