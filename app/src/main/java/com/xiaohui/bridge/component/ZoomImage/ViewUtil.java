package com.xiaohui.bridge.component.ZoomImage;

import android.annotation.TargetApi;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Build.VERSION;
import android.view.View;
import android.widget.ImageView;

/**
 * 视图操作类
 */
public class ViewUtil {
    /**
     * 设置图片透明度
     *
     * @param v
     * @param alpha
     */
    @SuppressWarnings("deprecation")
    public static void setImageAlpha(ImageView v, int alpha) {
        if (null == v) {
            return;
        }

        if (VERSION.SDK_INT >= 16) {
            setImageAlphaJellyBean(v, alpha);
        } else {
            v.setAlpha(alpha);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void setImageAlphaJellyBean(ImageView v, int alpha) {
        v.setImageAlpha(alpha);
    }

    /**
     * 设置背景视图
     *
     * @param v
     * @param d
     */
    @SuppressWarnings("deprecation")
    public static void setBackground(View v, Drawable d) {
        if (null == v) {
            return;
        }

        if (VERSION.SDK_INT >= 16) {
            setBackgroundJellyBean(v, d);
        } else {
            v.setBackgroundDrawable(d);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void setBackgroundJellyBean(View v, Drawable d) {
        v.setBackground(d);
    }
}
