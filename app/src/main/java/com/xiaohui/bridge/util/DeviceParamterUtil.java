package com.xiaohui.bridge.util;

import com.xiaohui.bridge.XhApplication;

/**
 * 获取设备的一些固定参数工具类
 * Created by jztang on 14-7-31.
 */
public class DeviceParamterUtil {

    /**
     * dip转为像素
     */
    public static int dip2px(float dipValue) {
        return (int) (dipValue * getScreenScale() + 0.5f);
    }

    private static float getScreenScale() {
        try {
            final float scale = XhApplication.getApplication().getResources().getDisplayMetrics().density;
            return scale;
        } catch (Exception e) {
            return 1;
        }
    }

    /**
     * 像素转为dip
     */
    public static int px2dip(float pxValue) {
        return (int) (pxValue / getScreenDensity() + 0.5f);
    }

    /**
     * 整个设备屏幕宽度  像素  值
     */
    public static int getScreenPixelsWidth() {
        final int width = XhApplication.getApplication().getResources().getDisplayMetrics().widthPixels;
        return width;
    }

    /**
     * 整个设备屏幕高度  像素  值
     */
    public static int getScreenPixelsHeight() {
        final int height = XhApplication.getApplication().getResources().getDisplayMetrics().heightPixels;
        return height;
    }

    /**
     * 整个设备屏幕宽度  dp  值
     */
    public static int getScreenDpWidth() {
        float density = XhApplication.getApplication().getResources().getDisplayMetrics().density;
        int width = XhApplication.getApplication().getResources().getDisplayMetrics().widthPixels;
        int dpwidth = (int) Math.ceil((float) width / density);
        return dpwidth;
    }

    /**
     * 整个设备屏幕高度  dp  值
     */
    public static int getScreenDpHeight() {
        float density = XhApplication.getApplication().getResources().getDisplayMetrics().density;
        int height = XhApplication.getApplication().getResources().getDisplayMetrics().heightPixels;
        int dpheight = (int) Math.ceil((float) height / density);
        return dpheight;
    }

    /**
     * 获取设备屏幕像素密度
     */
    public static float getScreenDensity() {
        try {
            final float density = XhApplication.getApplication().getResources().getDisplayMetrics().density;
            return density;
        } catch (Exception e) {
            return 1;
        }
    }

//    /**
//     * 获取设备状态栏高度
//     */
//    public static int getStatusBarHeight() {
//        try {
//            Rect frame = new Rect();
//            ActivityStackManager.getCurrentActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
//            return frame.top;
//        } catch(Exception e){
//            return 0;
//        }
//    }
}
