package com.suntown.cloudmonitoring.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * 绘图工具类
 * Created by jia.wei on 15/7/29.
 */
public class DrawUtils {

    /**
     * 测量文本内容宽度
     *
     * @param text  文本内容
     * @param paint 画笔
     * @return 内容宽度
     */
    public static float measureTextWidth(String text, Paint paint) {
        float width = paint.measureText(text);

        return width;
    }

    /**
     * 测量文本内容高度
     *
     * @param paint 画笔
     * @return 内容高度
     */
    public static float measureTextHeight(Paint paint) {
        Paint.FontMetrics fm = paint.getFontMetrics();
        float height = fm.bottom - fm.top;

        return height;
    }

    /**
     * 文本内容相对于centerX点居中
     *
     * @param canvas  画布
     * @param paint   画笔
     * @param text    文本内容
     * @param centerX x轴方向中心点
     * @param y       文本内容y轴方向偏移量
     */
    public static void drawTextByCenterX(Canvas canvas, Paint paint, String text, float centerX, float y) {
        canvas.drawText(text, (float) (centerX - measureTextWidth(text, paint) * 0.5), y, paint);
    }

    /**
     * 文本内容相对于centerY点居中
     *
     * @param canvas  画布
     * @param paint   画笔
     * @param text    文本内容
     * @param x       文本内容x轴方向偏移量
     * @param centerY y轴方向中心点
     */
    public static void drawTextByCenterY(Canvas canvas, Paint paint, String text, float x, float centerY) {
        float bottom = paint.getFontMetrics().bottom;
        canvas.drawText(text, x, (float) (0.5 * measureTextHeight(paint) - bottom + centerY), paint);
    }

    /**
     * 文本内容相对于某一点居中
     *
     * @param canvas  画布
     * @param paint   画笔
     * @param text    文本内容
     * @param centerX x轴方向中心点
     * @param centerY y轴方向中心点
     */
    public static void drawTextInCenter(Canvas canvas, Paint paint, String text, float centerX, float centerY) {
        float bottom = paint.getFontMetrics().bottom;
        canvas.drawText(text, (float) (centerX - measureTextWidth(text, paint) * 0.5),
                (float) (0.5 * measureTextHeight(paint) - bottom + centerY), paint);
    }

    /**
     * As meaning of method name.
     * 获得两点之间的距离
     * @param p0
     * @param p1
     * @return
     */
    public static float getDistanceBetween2Points(PointF p0, PointF p1) {
        float distance = (float) Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
        return distance;
    }

    /**
     * Get the point of intersection between circle and line.
     * 获取 通过指定圆心，斜率为lineK的直线与圆的交点。
     * @param pMiddle The circle center point.
     * @param radius The circle radius.
     * @param lineK The slope of line which cross the pMiddle.
     * @return
     */
    public static PointF[] getIntersectionPoints(PointF pMiddle, float radius, Double lineK) {
        PointF[] points = new PointF[2];
        float radian, xOffset = 0, yOffset = 0;
        if(lineK != null){
            radian= (float) Math.atan(lineK);
            xOffset = (float) (Math.sin(radian) * radius);
            yOffset = (float) (Math.cos(radian) * radius);
        }else {
            xOffset = radius;
            yOffset = 0;
        }
        points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
        points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);
        return points;
    }


    /**
     * Get point between p1 and p2 by percent.
     * 根据百分比获取两点之间的某个点坐标
     * @param p1
     * @param p2
     * @param percent
     * @return
     */
    public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
        return new PointF(evaluateValue(percent, p1.x , p2.x), evaluateValue(percent, p1.y , p2.y));
    }

    /**
     * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
     * @param fraction
     * @param start
     * @param end
     * @return
     */
    public static float evaluateValue(float fraction, Number start, Number end){
        return start.floatValue() + (end.floatValue() - start.floatValue()) * fraction;
    }
}
