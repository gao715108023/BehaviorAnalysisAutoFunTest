package com.gcj.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by gaochuanjun on 14-4-1.
 */
public class FileUtils {

    public static void readFileByLines(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempString;
            int index = 0;
            while ((tempString = reader.readLine()) != null) {
                if (tempString.contains("AccountName")) {
                    index++;
                }
            }
            System.out.println(index);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        FileUtils.readFileByLines("");
    }
}