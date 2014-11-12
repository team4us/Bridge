package com.xiaohui.bridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14/11/11.
 */
public class CoordinateView extends View {

    private List<PointF> points = new ArrayList<PointF>();
    private List<Integer[]> shapes = new ArrayList<Integer[]>();
    //系统坐标系的值
    private float viewWidth; //控件的宽度
    private float viewHeight; //控件的高度
    private float maxX = 0; //x轴的最大值
    private float minX = 0; //x轴的最小值
    private float maxY = 0; //y轴的最大值
    private float minY = 0; //y轴的最小值
    private float x;        //坐标系的x
    private float y;        //坐标系的y
    private float width;    //坐标系的宽度
    private float height; //坐标系的高度
    private float hPadding; //水平边距
    private float vPadding; //垂直边距
    private float offsetWidth;
    private float offsetHeight;

    //自己画的坐标系的值，都以s开头，表示self
    private float sX;        //x
    private float sY;        //y
    private float valueOffsetX;
    private float valueOffsetY;

    private Paint coordinatePaint = new Paint();
    private Paint gridPaint = new Paint();
    private Paint shapePaint = new Paint();


    public CoordinateView(Context context) {
        super(context);
        init();
    }

    private void init() {
        initPaint();
        initPoints();
    }

    private void initPaint() {
        coordinatePaint.setAntiAlias(true);
        coordinatePaint.setColor(Color.BLACK);
        coordinatePaint.setStrokeWidth(5.0f);
        coordinatePaint.setTextSize(30);
        coordinatePaint.setTypeface(Typeface.DEFAULT_BOLD);
        coordinatePaint.setStyle(Paint.Style.FILL);

        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        gridPaint.setStyle(Paint.Style.STROKE); //空心
        gridPaint.setStrokeWidth(2.0f);
        gridPaint.setPathEffect(effects);
        gridPaint.setColor(Color.GREEN);

        shapePaint.setColor(Color.BLUE);
        shapePaint.setStyle(Paint.Style.STROKE);
        shapePaint.setStrokeWidth(4.0f);
    }

    private void initPoints() {
        points.add(new PointF(0, 1));
        points.add(new PointF(0, 0.5f));
        points.add(new PointF(0, -0.5f));
        points.add(new PointF(0, -1));
        points.add(new PointF(20, 1));
        points.add(new PointF(20, 0.5f));
        points.add(new PointF(20, -0.5f));
        points.add(new PointF(20, -1));

        shapes.add(new Integer[]{1, 2, 6, 5, 1});
        shapes.add(new Integer[]{2, 3, 7, 6, 2});
        shapes.add(new Integer[]{3, 4, 8, 7, 3});

        for (PointF point : points) {
            if (maxX < point.x) {
                maxX = point.x;
            } else if (minX > point.x) {
                minX = point.x;
            }
            if (maxY < point.y) {
                maxY = point.y;
            } else if (minY > point.y) {
                minY = point.y;
            }
        }
    }

    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        viewWidth = w;
        viewHeight = h;
        super.onSizeChanged(w, h, oldW, oldH);
    }

    private float round(float value) {
        return (float) (Math.round(value * 10) / 10.0);
    }

    //转换自己坐标系的坐标为系统坐标系的坐标用于画图
    private PointF convertSelfToSystem(PointF point) {
        float offsetX = point.x - sX;
        float offsetY = point.y - sY;
        float x = this.x + (offsetX / valueOffsetX) * offsetWidth;
        float y = this.y + (offsetY / valueOffsetY) * offsetHeight;
        return new PointF(x, y);
    }

    //转换自己坐标系的坐标为系统坐标系的坐标用于记录
    private PointF convertSystemToSelf(PointF point) {
        return point;
    }

    //坐标系
    private void drawCoordinate(Canvas canvas) {
        drawXY(canvas);
        drawMark(canvas);
    }

    private void drawXY(Canvas canvas) {
        // 画直线
        hPadding = (float) Math.floor(viewWidth / 10);
        vPadding = (float) Math.floor(viewHeight / 10);
        x = hPadding + 40;
        y = vPadding + 40;
        width = viewWidth - 2 * hPadding;
        height = viewHeight - 2 * vPadding;
        canvas.drawLine(x, vPadding, x + width, vPadding, coordinatePaint);
        canvas.drawLine(hPadding, y, hPadding, y + height, coordinatePaint);

        // 画X轴箭头
        PointF endX = new PointF(x + width, vPadding);
        drawTriangle(canvas, new PointF(endX.x + 30, endX.y), new PointF(endX.x, endX.y - 15), new PointF(endX.x, endX.y + 15));
        canvas.drawText("X", endX.x, endX.y - 20, coordinatePaint);
        // 画Y轴箭头
        PointF endY = new PointF(hPadding, y + height);
        drawTriangle(canvas, new PointF(endY.x, endY.y + 30), new PointF(endY.x - 15, endY.y), new PointF(endY.x + 15, endY.y));
        canvas.drawText("Y", endY.x - 40, endY.y + 20, coordinatePaint);
    }

    private void drawMark(Canvas canvas) {
        valueOffsetX = round((maxX - minX) / 5);
        valueOffsetY = round((maxY - minY) / 5);
        this.offsetWidth = (float) Math.floor(width * 0.9f / 5);
        this.offsetHeight = (float) Math.floor(height * 0.9f / 5);

        float offsetWidth = (float) Math.floor(width * 0.9f / 10);
        float offsetHeight = (float) Math.floor(height * 0.9f / 10);

        //画刻度
        float xx = x;
        float yy = y;
        float valueX = minX;
        float valueY = minY;
        sX = minX;
        sY = minY;
        for (int i = 0; i < 11; i++) {
            Path path = new Path();
            path.moveTo(xx, vPadding + 40);
            path.lineTo(xx, vPadding + height);
            canvas.drawPath(path, gridPaint);

            path.moveTo(hPadding + 40, yy);
            path.lineTo(hPadding + width, yy);
            canvas.drawPath(path, gridPaint);

            if (i % 2 == 1) {
                canvas.drawLine(xx, vPadding - 2, xx, vPadding + 10, coordinatePaint);
                canvas.drawLine(hPadding - 2, yy, hPadding + 10, yy, coordinatePaint);
            } else {
                canvas.drawLine(xx, vPadding - 2, xx, vPadding + 20, coordinatePaint);
                canvas.drawLine(hPadding - 2, yy, hPadding + 20, yy, coordinatePaint);
                Rect bounds = new Rect();
                String textX = String.format("%.1f", valueX);
                coordinatePaint.getTextBounds(textX, 0, textX.length(), bounds);
                canvas.drawText(textX, xx - bounds.width() / 2, vPadding - bounds.height(), coordinatePaint);

                String textY = String.format("%.1f", valueY);
                coordinatePaint.getTextBounds(textY, 0, textY.length(), bounds);
                canvas.drawText(textY, hPadding - bounds.width() - 10, yy + bounds.height() / 2, coordinatePaint);
                valueX += valueOffsetX;
                valueY += valueOffsetY;
            }
            xx += offsetWidth;
            yy += offsetHeight;
        }
    }

    private void drawShapes(Canvas canvas) {
        Path path = new Path();
        for (Integer[] shape : shapes) {
            boolean isNewShape = true;
            for (Integer p : shape) {
                PointF point = convertSelfToSystem(points.get(p - 1));
                if (isNewShape) {
                    path.moveTo(point.x, point.y);
                    isNewShape = false;
                } else {
                    path.lineTo(point.x, point.y);
                }
                shapePaint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(point.x, point.y, 10, shapePaint);
            }
            path.close();
            shapePaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, shapePaint);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        drawCoordinate(canvas);
        drawShapes(canvas);
    }

    /**
     * 画三角形 用于画坐标轴的箭头
     */
    private void drawTriangle(Canvas canvas, PointF p1, PointF p2, PointF p3) {
        Path path = new Path();
        path.moveTo(p1.x, p1.y);
        path.lineTo(p2.x, p2.y);
        path.lineTo(p3.x, p3.y);
        path.close();
        canvas.drawPath(path, coordinatePaint);
    }

    /*
     * 用于保存拖动时的上一个点的位置
     */
    int x0, y0;

    /*
     * 拖动事件监听
     */
//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        int action = event.getAction();
//        /*
//         * (x,y)点为发生事件时的点，它的坐标值为相对于该控件左上角的距离
//         */
//        int x = (int) event.getX();
//        int y = (int) event.getY();
//        switch (action) {
//
//            case MotionEvent.ACTION_DOWN: // 按下
//                x0 = x;
//                y0 = y;
//                Log.i("down", "(" + x0 + "," + y0 + ")");
//                break;
//            case MotionEvent.ACTION_MOVE: // 拖动
//            /*
//             * 拖动时圆心坐标相对运动 (x0,y0)保存先前一次事件发生的坐标
//             * 拖动的时候只要计算两个坐标的delta值，然后加到圆心中，即移动了圆心。
//             * 然后调用invalidate()让系统调用onDraw()刷新以下屏幕，即实现了坐标移动。
//             */
//                po.x += x - x0;
//                po.y += y - y0;
//                x0 = x;
//                y0 = y;
//                Log.i("move", "(" + x0 + "," + y0 + ")");
//                invalidate();
//                break;
//            case MotionEvent.ACTION_UP: // 弹起
//                break;
//        }
//
//        /*
//         * 注意：这里一定要返回true
//         * 返回false和super.onTouchEvent(event)都会本监听只能检测到按下消息
//         * 这是因为false和super.onTouchEvent(event)的处理都是告诉系统该控件不能处理这样的消息，
//         * 最终系统会将这些事件交给它的父容器处理。
//         */
//        return true;
//
//    }
}
