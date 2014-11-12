package com.xiaohui.bridge.util;

import android.content.Context;

/**
 * 获取设备的一些固定参数工具类
 * Created by jztang on 14-7-31.
 */
public class DeviceParameterUtil {

    /**
     * dip转为像素
     */
    public static int dp2px(Context context, float dpValue) {
        return (int) (dpValue * getScreenScale(context) + 0.5f);
    }

    private static float getScreenScale(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 像素转为dip
     */
    public static int px2dp(Context context, float pxValue) {
        return (int) (pxValue / getScreenDensity(context) + 0.5f);
    }

    /**
     * 整个设备屏幕宽度  像素  值
     */
    public static int getScreenPxWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 整个设备屏幕高度  像素  值
     */
    public static int getScreenPxHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 整个设备屏幕宽度  dp  值
     */
    public static int getScreenDpWidth(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int width = context.getResources().getDisplayMetrics().widthPixels;
        return (int) Math.ceil((float) width / density);
    }

    /**
     * 整个设备屏幕高度  dp  值
     */
    public static int getScreenDpHeight(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        int height = context.getResources().getDisplayMetrics().heightPixels;
        return (int) Math.ceil((float) height / density);
    }

    /**
     * 获取设备屏幕像素密度
     */
    public static float getScreenDensity(Context context) {
        try {
            return context.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return 1;
        }
    }
}
