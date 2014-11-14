package com.xiaohui.bridge.component;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.xiaohui.bridge.util.ListUtil;
import com.xiaohui.bridge.util.MathUtil;

import java.util.List;

/**
 * Created by xiaohui on 14/11/11.
 */
public class CoordinateView extends View {

    //系统坐标系的值
    private float viewWidth; //控件的宽度
    private float viewHeight; //控件的高度
    private float x;        //坐标系的x
    private float y;        //坐标系的y
    private float width;    //坐标系的宽度
    private float height; //坐标系的高度
    private float stepX; //x轴的步长
    private float stepY; //y轴的步长
    private float hPadding; //水平边距
    private float vPadding; //垂直边距

    //自己画的坐标系的值，都以s开头，表示myself
    private float maxX = 0; //x轴的最大值
    private float minX = 0; //x轴的最小值
    private float maxY = 0; //y轴的最大值
    private float minY = 0; //y轴的最小值
    private float mX;       //x原点
    private float mY;       //y原点
    private float mStepX;   //x轴的步长
    private float mStepY;   //y轴的步长

    //选择的坐标点，以系统坐标系记录的，所以返回时需要调用转换函数
    private PointF touchPoint;
    private PointF selectPointStart;
    private PointF selectPointStop;
    private float length;


    private int touchCount;
    private boolean needOnePoint; //表示是选择一个点还是两个点
    private List<PointF> points;
    private List<Integer[]> shapes;
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
    }

    private void initPaint() {
        coordinatePaint.setAntiAlias(true);
        coordinatePaint.setColor(Color.BLACK);
        coordinatePaint.setStrokeWidth(5.0f);
        coordinatePaint.setTextSize(30);
        coordinatePaint.setTypeface(Typeface.DEFAULT_BOLD);
        coordinatePaint.setStyle(Paint.Style.FILL);

        gridPaint.setAntiAlias(true);
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(2.0f);
        gridPaint.setPathEffect(new DashPathEffect(new float[]{5, 5, 5, 5}, 1));
        gridPaint.setColor(Color.GREEN);

        shapePaint.setAntiAlias(true);
        shapePaint.setColor(Color.BLUE);
        shapePaint.setStrokeWidth(4.0f);

        pointPaint.setAntiAlias(true);
        pointPaint.setColor(Color.RED);
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setStrokeWidth(4.0f);
    }

    public void setShapes(List<PointF> points, List<Integer[]> shapes) {
        if (ListUtil.isEmpty(points) || ListUtil.isEmpty(shapes))
            return;
        this.points = points;
        this.shapes = shapes;

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

        mX = minX;
        mY = minY;

        clear();
    }

    public void setSelectOnePoint(boolean isOnePoint) {
        this.needOnePoint = isOnePoint;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldW, int oldH) {
        viewWidth = w;
        viewHeight = h;
        super.onSizeChanged(w, h, oldW, oldH);
    }

    private float round(float value) {
        return MathUtil.round(value, 2);
    }

    //转换自己坐标系的坐标为系统坐标系的坐标用于画图
    private PointF convertSelfToSystem(PointF point) {
        float x = this.x + ((point.y - mY) / mStepY) * stepX;
        float y = this.y + ((point.x - mX) / mStepX) * stepY;
        return new PointF(x, y);
    }

    //转换自己坐标系的坐标为系统坐标系的坐标用于记录
    private PointF convertSystemToSelf(PointF point) {
        float x = mX + ((point.y - this.y) / stepY) * mStepX;
        float y = mY + ((point.x - this.x) / stepX) * mStepY;
        return new PointF(round(x), round(y));
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
        canvas.drawLine(hPadding, y, hPadding, y + height, coordinatePaint); // 画X轴
        canvas.drawLine(x, vPadding, x + width, vPadding, coordinatePaint); // 画Y轴

        // 画X轴箭头
        PointF endY = new PointF(hPadding, y + height);
        drawTriangle(canvas, new PointF(endY.x, endY.y + 30), new PointF(endY.x - 15, endY.y), new PointF(endY.x + 15, endY.y));
        canvas.drawText("X", endY.x - 40, endY.y + 20, coordinatePaint);

        // 画Y轴箭头
        PointF endX = new PointF(x + width, vPadding);
        drawTriangle(canvas, new PointF(endX.x + 30, endX.y), new PointF(endX.x, endX.y - 15), new PointF(endX.x, endX.y + 15));
        canvas.drawText("Y", endX.x, endX.y - 20, coordinatePaint);
    }

    private void drawMark(Canvas canvas) {
        mStepX = round((maxX - minX) / 5);
        mStepY = round((maxY - minY) / 5);
        stepX = (float) Math.floor(width * 0.9f / 5);
        stepY = (float) Math.floor(height * 0.9f / 5);
        float tStepX = (float) Math.floor(width * 0.9f / 10);
        float tStepY = (float) Math.floor(height * 0.9f / 10);

        //画刻度
        //系统坐标系下当前X轴和Y轴值
        float currentX = x;
        float currentY = y;
        //自己坐标系下X轴和Y轴值
        float mCurrentX = mX;
        float mCurrentY = mY;
        for (int i = 0; i <= 10; i++) {
            Path path = new Path();
            path.moveTo(currentX, vPadding + 40);
            path.lineTo(currentX, vPadding + height);
            canvas.drawPath(path, gridPaint);

            path.moveTo(hPadding + 40, currentY);
            path.lineTo(hPadding + width, currentY);
            canvas.drawPath(path, gridPaint);

            if (i % 2 == 1) {
                canvas.drawLine(currentX, vPadding - 2, currentX, vPadding + 10, coordinatePaint);
                canvas.drawLine(hPadding - 2, currentY, hPadding + 10, currentY, coordinatePaint);
            } else {
                canvas.drawLine(currentX, vPadding - 2, currentX, vPadding + 20, coordinatePaint);
                canvas.drawLine(hPadding - 2, currentY, hPadding + 20, currentY, coordinatePaint);
                Rect bounds = new Rect();
                String textX = String.format("%.1f", mCurrentY);
                coordinatePaint.getTextBounds(textX, 0, textX.length(), bounds);
                canvas.drawText(textX, currentX - bounds.width() / 2, vPadding - bounds.height(), coordinatePaint);

                String textY = String.format("%.1f", mCurrentX);
                coordinatePaint.getTextBounds(textY, 0, textY.length(), bounds);
                canvas.drawText(textY, hPadding - bounds.width() - 10, currentY + bounds.height() / 2, coordinatePaint);
                mCurrentX += mStepX;
                mCurrentY += mStepY;
            }
            currentX += tStepX;
            currentY += tStepY;
        }
    }

    private void drawShapes(Canvas canvas) {
        Path path = new Path();
        for (Integer[] shape : shapes) {
            boolean isNewShape = true;
            for (Integer index : shape) {
                PointF point = convertSelfToSystem(points.get(index - 1));
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
        PointF systemPointStart = null;
        PointF systemPointStop = null;
        if (selectPointStart != null) {
            systemPointStart = convertSelfToSystem(selectPointStart);
            canvas.drawCircle(systemPointStart.x, systemPointStart.y, 10, pointPaint);
            canvas.drawText("A", systemPointStart.x + 10, systemPointStart.y + 10, coordinatePaint);
        }

        if (selectPointStop != null) {
            systemPointStop = convertSelfToSystem(selectPointStop);
            canvas.drawCircle(systemPointStop.x, systemPointStop.y, 10, pointPaint);
            canvas.drawText("B", systemPointStop.x + 10, systemPointStop.y + 10, coordinatePaint);
        }

        if (!needOnePoint && systemPointStart != null && systemPointStop != null) {
            canvas.drawLine(systemPointStart.x, systemPointStart.y,
                    systemPointStop.x, systemPointStop.y, pointPaint);
        }
    }

    private void drawTouchPoint(Canvas canvas) {
        Rect bounds = new Rect();
        String textX = "";
        if (selectPointStart != null) {
            textX += String.format("A(%.1f, %.1f)", selectPointStart.x, selectPointStart.y);
            if (selectPointStop != null) {
                textX += String.format("   B(%.1f, %.1f)", selectPointStop.x, selectPointStop.y);
                length = MathUtil.lengthWithTwoPoint(selectPointStart, selectPointStop);
                textX += String.format("   长度 %.1f", length);
            } else if (touchPoint != null) {
                textX += String.format("   B(%.1f, %.1f)", touchPoint.x, touchPoint.y);
                textX += String.format("   长度 %.1f", MathUtil.lengthWithTwoPoint(selectPointStart, touchPoint));
            }
        } else if (touchPoint != null) {
            textX += String.format("A(%.1f, %.1f)", touchPoint.x, touchPoint.y);
        }

        coordinatePaint.getTextBounds(textX, 0, textX.length(), bounds);
        canvas.drawText(textX, x + (width - bounds.width()) / 2, y + height + (vPadding - bounds.height()) / 2, coordinatePaint);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (ListUtil.isEmpty(points) || ListUtil.isEmpty(shapes))
            return;
        canvas.drawColor(Color.WHITE);
        drawCoordinate(canvas);
        drawShapes(canvas);
        drawSelectPoints(canvas);
        drawTouchPoint(canvas);
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

    //判断一个点是否在圈定的图形范围内
    //0 点p在轮廓上；1 点p在多边形内；-1 点p在多边形外
    private int pointInShapes(PointF point) {
        int i, CrossPoints = 0;
        float xmin, ymin, xmax, ymax;
        double x;
        for (i = 0; i < points.size() - 1; i++) {
            PointF point1 = points.get(i);
            PointF point2 = points.get(i + 1);
            xmin = Math.min(point1.x, point2.x);
            xmax = Math.max(point1.x, point2.x);
            ymin = Math.min(point1.y, point2.y);
            ymax = Math.max(point1.y, point2.y);
            if (point.y > ymax || point.y < ymin) continue; //水平射线不可能与轮廓线段i->i+1有交
            if (point.y == ymax) {
                if (ymax == point1.y && point.x == point1.x || ymax == point2.y &&
                        point.x == point2.x)
                    return 0;          // p位于多边形轮廓上
                else if (ymin == ymax) {
                    if (point.x >= xmin && point.x <= xmax)
                        return 0; // p位于多边形轮廓上
                }
                continue; // 放弃求交（避免重复交点）
            }
            //计算水平射线与轮廓线段i->i+1交点的x值
            x = (double) (point.y - point1.y) / (point2.y - point1.y) * (point2.x - point1.x) + point1.x;
            if (x == point.x)
                return 0;    // p位于多边形轮廓上
            if (x > point.x)
                CrossPoints++; // 统计位于p右方的交点数
        }
        if (CrossPoints % 2 == 0)
            return -1; // p在多边形外
        else
            return 1;  // p在多边形内
    }

    public void clear() {
        selectPointStart = null;
        selectPointStop = null;
        length = 0;
        invalidate();
    }

    public PointF getSelectPointStart() {
        return selectPointStart;
    }

    public PointF getSelectPointStop() {
        return selectPointStop;
    }

    public void setSelectPointStart(PointF point) {
        if (point == null)
            return;
        selectPointStart = point;
        invalidate();
    }

    public void setSelectPointStop(PointF point) {
        if (point == null)
            return;
        selectPointStop = point;
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        touchPoint = convertSystemToSelf(new PointF(event.getX(), event.getY()));
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                if (!isFirstTime()) {
                    selectPointStop = touchPoint;
                    invalidate();
                } else {
                    clear();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isFirstTime()) {
                    selectPointStart = touchPoint;
                    touchCount++;
                } else {
                    selectPointStop = touchPoint;
                    touchCount = 0;
                }
                touchPoint = null;
                invalidate();
                break;
        }
        return true;
    }

    private boolean isFirstTime() {
        return needOnePoint || touchCount == 0;
    }
}
