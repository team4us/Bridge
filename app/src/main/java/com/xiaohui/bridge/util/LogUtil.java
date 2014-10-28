package com.xiaohui.bridge.util;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by xiaohui on 13-12-16.
 */
public class LogUtil {

    private static String sGlobalTag = "LogUtil";
    private static boolean sIsEnable = true;
    private static final int LOG_MAX_LENGTH = 3000;

    public static void setEnable(boolean enable) {
        sIsEnable = enable;
    }

    public static boolean isEnbale() {
        return sIsEnable;
    }

    public static void setGlobalTag(String tag) {
        sGlobalTag = tag;
    }

    public static String getGlobalTag() {
        return sGlobalTag;
    }

    public static void v(String msg) {
        v(getGlobalTag(), msg, 3);
    }

    public static void v(String tag, String msg) {
        v(tag, msg, 3);
    }

    private static void v(String tag, String msg, int index) {
        if (!isEnbale())
            return;
        Log.v(tag, msg + getTraceInfo(index));
    }

    public static void d(String msg) {
        d(getGlobalTag(), msg, 3);
    }

    public static void d(String tag, String msg) {
        d(tag, msg, 3);
    }

    private static void d(String tag, String msg, int index) {
        if (!isEnbale())
            return;
        Log.d(tag, msg + getTraceInfo(index));
    }

    public static void i(String msg) {
        i(getGlobalTag(), msg, 3);
    }

    public static void i(String tag, String msg) {
        i(tag, msg, 3);
    }

    private static void i(String tag, String msg, int index) {
        if (!isEnbale())
            return;
        int start = 0;
        int end = start + LOG_MAX_LENGTH;
        while (end < msg.length()) {
            Log.i(tag, msg.substring(start, end));
            start = end;
            end = start + LOG_MAX_LENGTH;
        }
        Log.i(tag, msg.substring(start) + getTraceInfo(index));
    }

    public static void w(String msg) {
        w(getGlobalTag(), msg, 3);
    }

    public static void w(String tag, String msg) {
        w(tag, msg, 3);
    }

    private static void w(String tag, String msg, int index) {
        if (!isEnbale())
            return;
        Log.w(tag, msg + getTraceInfo(index));
    }

    public static void e(String msg) {
        e(getGlobalTag(), msg, 3);
    }

    public static void e(String tag, String msg) {
        e(tag, msg, 3);
    }

    private static void e(String tag, String msg, int index) {
        if (!isEnbale())
            return;
        Log.e(tag, msg + getTraceInfo(index));
    }

    public static void e(String tag, Throwable throwable) {
        e(tag, throwable.getMessage(), throwable);
    }

    public static void e(String tag, String msg, Throwable throwable) {
        if (!isEnbale())
            return;
        Log.e(tag, msg + getTraceInfo(3), throwable);
    }

    public static void printFunction() {
        if (!isEnbale())
            return;
        Log.i(getGlobalTag(), getTraceInfo(2));
    }

    private static String getTraceInfo(int index) {
        StringBuffer sb = new StringBuffer();
        StackTraceElement[] stacks = new Throwable().getStackTrace();
        if (stacks.length <= index)
            return "{at unknwon}";
        sb.append("   {at ")
                .append(stacks[index].getClassName())
                .append(".")
                .append(stacks[index].getMethodName())
                .append("(")
                .append(stacks[index].getFileName())
                .append(":")
                .append(stacks[index].getLineNumber())
                .append(")}");
        return sb.toString();
    }

    public static void logToFile(String TAG, String string) {

        i(TAG, string);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        Date now = new Date();

        StringBuffer sb = new StringBuffer(dateTimeFormat.format(now));
        sb.append(" ").append(TAG).append(" ").append(string).append('\n');

        dateTimeFormat.applyPattern("yyyy-MM-dd");
        File logDir = new File(Environment.getExternalStorageDirectory(), "Ctrip");
        if (!logDir.exists()) {
            logDir.mkdirs();
        }
        File logFile = new File(logDir, "log-" + dateTimeFormat.format(now) + ".log");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e(TAG, e.getMessage(), e);
            }
        }

        BufferedWriter writer = null;
        try {
            FileWriter fileWriter = new FileWriter(logFile, true);
            writer = new BufferedWriter(fileWriter);
            writer.write(sb.toString());
        } catch (IOException e) {
            e(TAG, e.getMessage(), e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e(TAG, e.getMessage(), e);
                }
            }
        }

    }

    private LogUtil() {
    }

}
