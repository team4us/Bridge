package com.xiaohui.bridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.xiaohui.bridge.util.DeviceParameterUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14/11/11.
 */
public class CoordinateView extends View {

//    private List<PointF> points = new ArrayList<PointF>();
//    private List<Integer[]> shapes = new ArrayList<Integer[]>();
//    private int screenWidth;
//    private int screenHeight;
//
//    public CoordinateView(Context context) {
//        super(context);
//        initPoints();
//        screenWidth = DeviceParameterUtil.getScreenPxWidth(getContext());
//        screenHeight = DeviceParameterUtil.getScreenPxHeight(getContext());
//    }
//
//    private void initPoints() {
//        points.add(new PointF(0, 1));
//        points.add(new PointF(0, 0.5f));
//        points.add(new PointF(0, -0.5f));
//        points.add(new PointF(0, -1));
//        points.add(new PointF(20, 1));
//        points.add(new PointF(20, 0.5f));
//        points.add(new PointF(20, -0.5f));
//        points.add(new PointF(20, -1));
//
//        shapes.add(new Integer[]{1, 2, 6, 5, 1});
//        shapes.add(new Integer[]{2, 3, 7, 6, 2});
//        shapes.add(new Integer[]{3, 4, 8, 7, 3});
//    }
//
//    private void drawCoordinateSystem(Canvas canvas) {
//        // 画坐标轴
//        if (canvas != null) {
//            canvas.drawColor(Color.WHITE);
//            // 画直线
//            canvas.drawLine(0, centerY, 2 * centerX, centerY, paint);
//            canvas.drawLine(centerX, 0, centerX, 2 * centerY, paint);
//            // 画X轴箭头
//            int x = 2 * centerX, y = centerY;
//            drawTriangle(canvas, new Point(x, y), new Point(x - 10, y - 5),
//                    new Point(x - 10, y + 5));
//            canvas.drawText("X", x - 15, y + 18, paint);
//            // 画Y轴箭头
//            x = centerX;
//            y = 0;
//            drawTriangle(canvas, new Point(x, y), new Point(x - 5, y + 10),
//                    new Point(x + 5, y + 10));
//            canvas.drawText("Y", x + 12, y + 15, paint);
//            // 画中心点坐标
//            // 先计算出来当前中心点坐标的值
//            String centerString = "(" + (centerX - po.x) / 2 + ","
//                    + (po.y - centerY) / 2 + ")";
//            // 然后显示坐标
//            canvas.drawText(centerString, centerX - 25, centerY + 15, paint);
//        }
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        drawCoordinateSystem(canvas);
//    }

    /*
 * 自定义控件一般写两个构造方法 CoordinatesView(Context context)用于java硬编码创建控件
 * 如果想要让自己的控件能够通过xml来产生就必须有第2个构造方法 CoordinatesView(Context context,
 * AttributeSet attrs) 因为框架会自动调用具有AttributeSet参数的这个构造方法来创建继承自View的控件
 */
    public CoordinateView(Context context) {
        super(context, null);
    }

    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /*
     * 两个外部数据
     */
    Point pa = new Point(10, 10);
    Point pb = new Point(20, 40);

    /*
     * 圆心（坐标值是相对与控件的左上角的）
     */
    Point po = new Point();
    /*
     * 控件的中心点
     */
    int centerX, centerY;

    /*
     * 控件创建完成之后，在显示之前都会调用这个方法，此时可以获取控件的大小 并得到中心坐标和坐标轴圆心所在的点。
     */
    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        centerX = w / 2;
        centerY = h / 2;
        po.set(centerX, centerY);
        super.onSizeChanged(w, h, oldw, oldh);
    }

    /*
     * 自定义控件一般都会重载onDraw(Canvas canvas)方法，来绘制自己想要的图形
     */
    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);

        // 画坐标轴
        if (canvas != null) {
            canvas.drawColor(Color.WHITE);
            // 画直线
            canvas.drawLine(0, centerY, 2 * centerX, centerY, paint);
            canvas.drawLine(centerX, 0, centerX, 2 * centerY, paint);
            // 画X轴箭头
            int x = 2 * centerX, y = centerY;
            drawTriangle(canvas, new Point(x, y), new Point(x - 10, y - 5),
                    new Point(x - 10, y + 5));
            canvas.drawText("X", x - 15, y + 18, paint);
            // 画Y轴箭头
            x = centerX;
            y = 0;
            drawTriangle(canvas, new Point(x, y), new Point(x - 5, y + 10),
                    new Point(x + 5, y + 10));
            canvas.drawText("Y", x + 12, y + 15, paint);
            // 画中心点坐标
            // 先计算出来当前中心点坐标的值
            String centerString = "(" + (centerX - po.x) / 2 + ","
                    + (po.y - centerY) / 2 + ")";
            // 然后显示坐标
            canvas.drawText(centerString, centerX - 25, centerY + 15, paint);
        }

        if (canvas != null) {
            /*
             * TODO 画数据 所有外部需要在坐标轴上画的数据，都在这里进行绘制
             */
            canvas.drawCircle(po.x + 2 * pa.x, po.y - 2 * pa.y, 2, paint);
            canvas.drawCircle(po.x + 2 * pb.x, po.y - 2 * pb.y, 2, paint);
            canvas.drawLine(po.x + 2 * pa.x, po.y - 2 * pa.y, po.x + 2 * pb.x,
                    po.y - 2 * pb.y, paint);
            // canvas.drawPoint(pa.x+po.x, po.y-pa.y, paint);
        }

    }

    /**
     * 画三角形 用于画坐标轴的箭头
     */
    private void drawTriangle(Canvas canvas, Point p1, Point p2, Point p3) {
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.close();

        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        // 绘制这个多边形
        canvas.drawPath(path, paint);
    }

    /*
     * 用于保存拖动时的上一个点的位置
     */
    int x0, y0;

    /*
     * 拖动事件监听
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        /*
         * (x,y)点为发生事件时的点，它的坐标值为相对于该控件左上角的距离
         */
        int x = (int) event.getX();
        int y = (int) event.getY();
        switch (action) {

            case MotionEvent.ACTION_DOWN: // 按下
                x0 = x;
                y0 = y;
                Log.i("down", "(" + x0 + "," + y0 + ")");
                break;
            case MotionEvent.ACTION_MOVE: // 拖动
            /*
             * 拖动时圆心坐标相对运动 (x0,y0)保存先前一次事件发生的坐标
             * 拖动的时候只要计算两个坐标的delta值，然后加到圆心中，即移动了圆心。
             * 然后调用invalidate()让系统调用onDraw()刷新以下屏幕，即实现了坐标移动。
             */
                po.x += x - x0;
                po.y += y - y0;
                x0 = x;
                y0 = y;
                Log.i("move", "(" + x0 + "," + y0 + ")");
                invalidate();
                break;
            case MotionEvent.ACTION_UP: // 弹起
                break;
        }

        /*
         * 注意：这里一定要返回true
         * 返回false和super.onTouchEvent(event)都会本监听只能检测到按下消息
         * 这是因为false和super.onTouchEvent(event)的处理都是告诉系统该控件不能处理这样的消息，
         * 最终系统会将这些事件交给它的父容器处理。
         */
        return true;

    }
}
