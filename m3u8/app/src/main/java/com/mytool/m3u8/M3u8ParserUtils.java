package com.mytool.m3u8;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanghaofei on 2017/12/28.
 */

public class M3u8ParserUtils {

    /**
     * 解析M3u8文件获取里面的指定flag的行
     *
     * @param path
     * @return
     */
    public static List<String> getM3u8ToFlag(String path, String flag) {
        // 存储ts格式的视频的名称
        List<String> listTs = new ArrayList<String>();
        listTs.clear();
        File file = new File(path);
        FileInputStream inputStream = null;
        BufferedReader reader = null;
        try {
            inputStream = new FileInputStream(file);
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = reader.readLine()) != null) {
                if (line.contains(flag)) {
                    listTs.add(line.toString());

                }
            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (reader != null)
                    reader.close();
            } catch (Exception e) {

                e.printStackTrace();
            }
        }
        return listTs;

    }


}
