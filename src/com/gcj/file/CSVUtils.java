package com.gcj.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gaochuanjun on 14-1-2.
 */
public class CSVUtils {
    public static void writeCSV(String destFile, Map<String, Integer> resultMap) {
        File csv = new File(destFile); // CSV文件
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(csv, true));
            Iterator<Map.Entry<String, Integer>> iterator = resultMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Integer> entry = iterator.next();
                String info = entry.getKey() + "," + entry.getValue();
                bw.write(info);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bw != null)
                    bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
