package com.xiaohui.bridge.util;

import java.io.File;

/**
 *
 * Created by Administrator on 2014/10/29.
 */
public class FileOperateUtil {
    public static long getFolderSize(String filePath) throws Exception {
        File file = new File(filePath);

        long size = 0;
        java.io.File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            if (fileList[i].isDirectory()) {
                size = size + getFolderSize(fileList[i].getPath());
            } else {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    public static void deleteAllFiles(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();
            int len = files.length;
            for (int i = 0; i < len; i++) {
                deleteAllFiles(files[i].getPath());
            }
        } else {
            file.delete();
        }
    }

}
