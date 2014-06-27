package com.gcj.file;

import com.gcj.bean.FunctionDayBean;
import com.gcj.bean.InfcltBean;
import com.gcj.bean.UserOperationLogBean;
import com.gcj.utils.DateUtils;
import com.gcj.utils.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public List<InfcltBean> getInfoInfclt(String filePath) {
        List<InfcltBean> infcltBeans = new ArrayList<InfcltBean>();
        File file = new File(filePath);
        BufferedReader reader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fileInputStream, "gbk"));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                String[] array = tempString.split(",");
                infcltBeans.add(new InfcltBean(DateUtils.getDate(StringUtils.delDoubleQuotes(array[0])), Integer.parseInt(StringUtils.delDoubleQuotes(array[1])), StringUtils.delDoubleQuotes(array[2])));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return infcltBeans;
    }

    public static List<UserOperationLogBean> getUserOperationLog(String filePath, String userId) {
        List<UserOperationLogBean> infcltBeans = new ArrayList<UserOperationLogBean>();
        File file = new File(filePath);
        BufferedReader reader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fileInputStream, "gbk"));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                String[] array = tempString.split(" ");
                if (array[1].equalsIgnoreCase(userId)) {
                    array[0] = array[0].substring(0, array[0].indexOf("+"));
                    array[0] = array[0].replace('T', ' ');
                    array[2] = array[2].substring(array[2].indexOf("p=") + 2, array[2].length());
                    infcltBeans.add(new UserOperationLogBean(DateUtils.getDate2(array[0]), array[1], array[2]));
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return infcltBeans;
    }

    public List<FunctionDayBean> getFunctionDay(String filePath) {
        List<FunctionDayBean> functionDayBeans = new ArrayList<FunctionDayBean>();
        File file = new File(filePath);
        BufferedReader reader = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(fileInputStream, "gbk"));
            String tempString;
            while ((tempString = reader.readLine()) != null) {
                String[] array = tempString.split(",");
                functionDayBeans.add(new FunctionDayBean(array[0], DateUtils.getDate1(array[1]), array[2], array[3], array[4]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            try {
                if (fileInputStream != null)
                    fileInputStream.close();
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return functionDayBeans;
    }

    public void writeFile(String filePath, String info) {
        BufferedWriter bufferedWriter = null;
        File file = new File(filePath);

        try {
            if (file.exists()) {
                bufferedWriter = new BufferedWriter(new FileWriter(file, true));
            } else {
                bufferedWriter = new BufferedWriter(new FileWriter(file));
            }
            bufferedWriter.write(info + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null)
                    bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        FileUtil fileUtil = new FileUtil();
        List<InfcltBean> infcltBeans = fileUtil.getInfoInfclt("D:\\test\\用户行为分析测试\\infclt.txt");
        for (InfcltBean infcltBean : infcltBeans) {
            System.out.println(infcltBean.getUpdateTime() + "," + infcltBean.getClientType() + "," + infcltBean.getUrl());
        }
    }
}
