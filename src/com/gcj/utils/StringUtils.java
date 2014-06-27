package com.gcj.utils;

public class StringUtils {

    public static String delDoubleQuotes(String value) {
        String result;
        int len = value.length();
        result = value.substring(1, len - 1);
        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub
        String result = StringUtils.delDoubleQuotes("\"root://fund/365index/index.html\"");
        System.out.println(result);
        String s = "file:///D:/Program%20Files/dzh116/ui/stk/StkScreen_QY.gcj.ui?obj=B$993091.stk";
        if(s.contains("/ui/stk/StkScreen_QY.gcj.ui")){
            System.out.println("YES");
        }
    }
}
