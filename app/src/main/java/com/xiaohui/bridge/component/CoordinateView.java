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
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaohui on 14/11/11.
 */
public class CoordinateView extends View {

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

    //选择的坐标点，以系统坐标系记录的，所以返回时需要调用转换函数
    private PointF selectPointStart;
    private PointF selectPointStop;
    private List<PointF> movePoints = new ArrayList<PointF>();

    private boolean isOnePoint; //表示是选择一个点还是两个点
    private List<PointF> points = new ArrayList<PointF>();
    private List<Integer[]> shapes = new ArrayList<Integer[]>();
    private Paint coordinatePaint = new Paint();
    private Paint gridPaint = new Paint();
    private Paint shapePaint = new Paint();
    private Paint pointPaint = new Paint();

    public CoordinateView(Context context) {
        super(context);
        init();
    }

    public CoordinateView(Context context, AttributeSet attrs) {
        super(context, attrs);
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
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2.0f);
        gridPaint.setPathEffect(effects);
        gridPaint.setColor(Color.GREEN);

        shapePaint.setColor(Color.BLUE);
        shapePaint.setStrokeWidth(4.0f);

        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(4.0f);
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

    public void setSelectOnePoint(boolean isOnePoint) {
        this.isOnePoint = isOnePoint;
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
        float offsetX = point.x - this.x;
        float offsetY = point.y - this.y;
        float x = sX + (offsetX / offsetWidth) * valueOffsetX;
        float y = sY + (offsetY / offsetHeight) * valueOffsetY;
        return new PointF(x, y);
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

    private void drawSelectPoints(Canvas canvas) {
        if (selectPointStart == null || selectPointStop == null)
            return;
        canvas.drawCircle(selectPointStart.x, selectPointStart.y, 10, pointPaint);
        canvas.drawCircle(selectPointStop.x, selectPointStop.y, 10, pointPaint);
        if (!isOnePoint) {
            float startX = selectPointStart.x;
            float startY = selectPointStart.y;
            float stopX;
            float stopY;
            for (PointF point : movePoints) {
                stopX = point.x;
                stopY = point.y;
                canvas.drawLine(startX, startY, stopX, stopY, pointPaint);
                startX = point.x;
                startY = point.y;
            }
            stopX = selectPointStop.x;
            stopY = selectPointStop.y;
            canvas.drawLine(startX, startY, stopX, stopY, pointPaint);
        }
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        drawCoordinate(canvas);
        drawShapes(canvas);
        drawSelectPoints(canvas);
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

    public void clear() {
        selectPointStart = null;
        selectPointStop = null;
        movePoints.clear();
        invalidate();
    }

    public PointF getSelectPointStart() {
        return convertSystemToSelf(selectPointStart);
    }

    public PointF getSelectPointStop() {
        return convertSystemToSelf(selectPointStop);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        float x = event.getX();
        float y = event.getY();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                selectPointStart = new PointF(x, y);
                selectPointStop = new PointF(x, y);
                movePoints.clear();
                if (isOnePoint) {
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isOnePoint) {
                    movePoints.add(new PointF(x, y));
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!isOnePoint) {
                    selectPointStop = new PointF(x, y);
                    invalidate();
                }
                break;
        }
        return true;
    }
}
