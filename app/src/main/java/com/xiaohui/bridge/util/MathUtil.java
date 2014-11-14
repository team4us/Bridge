package com.xiaohui.bridge.util;

import android.graphics.PointF;

/**
 * Created by xiaohui on 14/11/14.
 */
public class MathUtil {

    //digit表示保留小数点后几位
    public static float round(float value, int digit) {
        double number = Math.pow(10, digit);
        return (float) (Math.round(value * number) / number);
    }

    public static float lengthWithTwoPoint(PointF point1, PointF point2) {
        return round((float) Math.sqrt(Math.pow((double) (point1.x - point2.x), 2)
                + Math.pow((double) (point1.y - point2.y), 2)), 2);
    }
}
