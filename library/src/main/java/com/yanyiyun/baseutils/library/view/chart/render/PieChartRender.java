package com.yanyiyun.baseutils.library.view.chart.render;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.RectF;

import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IPieAxisData;
import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IPieData;
import com.yanyiyun.baseutils.library.view.chart.interfaces.listener.TouchListener;

import java.text.NumberFormat;

public class PieChartRender extends ChartRender implements TouchListener {

    private IPieAxisData pieAxisData;
    private IPieData pieData;

    private Paint mPaint=new Paint();
    private Paint textPaint=new Paint();

    private Path outPath=new Path(),midPath=new Path(),inPath=new Path();
    private Path outMidPath=new Path(),midInPath=new Path();

    /**
     * 绘制扇形的度数
     */
    private float drawAngle;

    /**
     * 是否触摸放大
     */
    private boolean touchFlag=false;

    private NumberFormat numberFormat;

    /**
     * 标记线长度
     */
    private float markerLineLength=30f;


    /**
     * 标记文本大小
     */
    private float textSize=13;

    /**
     * 标记文本的高度
     */
    private float textHeight=100;
    private float textBottom;
    /**
     * 饼状图 内部绘制文字的点坐标
     */
    private PointF mPointF = new PointF();


    public PieChartRender(IPieAxisData iPieAxisData,IPieData iPieData ) {
        this.pieAxisData=iPieAxisData;
        this.pieData=iPieData;

        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        textPaint.setAntiAlias(true);
        textPaint.setDither(true);
        textPaint.setColor(pieAxisData.getColor());
        textPaint.setTextSize(pieAxisData.getTextSize());
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(pieAxisData.getPaintWidth());

        numberFormat=NumberFormat.getPercentInstance();
        numberFormat.setMinimumFractionDigits(pieAxisData.getDecimalPlaces());
    }


    @Override
    public void drawGraph(Canvas canvas, float animatedValue) {

        /**
         * 绘制视图
         */
        if(Math.min(pieData.getAngle()-1,animatedValue-pieData.getCurrentAngle())>=0){
            drawAngle=Math.min(pieData.getAngle()-1,animatedValue-pieData.getCurrentAngle());
        }else {
            drawAngle=0;
        }

        if(touchFlag){
            drawArc(canvas,pieData.getCurrentAngle(),drawAngle,pieData,pieAxisData.getOffsetRectFs()[0],
                    pieAxisData.getOffsetRectFs()[1],pieAxisData.getOffsetRectFs()[2],mPaint);
        }else {
            drawArc(canvas,pieData.getCurrentAngle(),drawAngle,pieData,pieAxisData.getRectFs()[0],
                    pieAxisData.getRectFs()[1],pieAxisData.getRectFs()[2],mPaint);
        }
    }

    /**
     * 在饼状图里面绘制文本
     * @param canvas
     * @param animatedValue
     */
    public void drawGraphText(Canvas canvas,float animatedValue){
        if(pieAxisData.getIsTextSize()&&animatedValue>pieData.getCurrentAngle()+pieData.getAngle()/2){
            if(touchFlag){
                drawText(canvas,pieData,pieData.getCurrentAngle()+pieAxisData.getStartAngle(),
                        numberFormat,true);
            } else {
                if(pieData.getAngle()>pieAxisData.getMinAngle()){
                    drawText(canvas,pieData,pieData.getCurrentAngle()+
                            pieAxisData.getStartAngle(),numberFormat,false);
                }
            }
        }
    }


    /**
     * 绘制扇形
     * @param canvas
     * @param currentStartAnge  开始角度
     * @param drawAngle  扇形的度数
     * @param pie  扇形数据
     * @param outRectf  最外层矩形
     * @param midRectf  中间层矩形
     * @param inRectf  里面层矩形
     * @param paint
     */
    private void drawArc(Canvas canvas,float currentStartAnge,float drawAngle,IPieData pie,RectF outRectf,
                         RectF midRectf,RectF inRectf,Paint paint){
        outPath.moveTo(0,0);
        outPath.arcTo(outRectf,currentStartAnge,drawAngle);
        midPath.moveTo(0,0);
        midPath.arcTo(midRectf,currentStartAnge,drawAngle);
        inPath.moveTo(0,0);
        inPath.arcTo(inRectf,currentStartAnge,drawAngle);
        outMidPath.op(outPath,midPath,Path.Op.DIFFERENCE);
        midInPath.op(midPath,inPath,Path.Op.DIFFERENCE);
        paint.setColor(pie.getColor());
        canvas.drawPath(outMidPath,paint);
        paint.setAlpha(0x80); //设置透明度
        canvas.drawPath(midInPath,paint);
        outPath.reset();
        midPath.reset();
        inPath.reset();
        outMidPath.reset();
        midInPath.reset();
    }

    /**
     *
     * @param canvas
     * @param pie
     * @param currentStartAngle  当前弧形的开始角度
     * @param numberFormat
     * @param flag 是否绘制名称
     */
    private void drawText(Canvas canvas, IPieData pie, float currentStartAngle,
                          NumberFormat numberFormat,boolean flag){
        int textPathX=(int)(Math.cos(Math.toRadians(currentStartAngle+pie.getAngle()/2))*
                pieAxisData.getAxisLength()*(1+pieAxisData.getOutsideRadiusScale())/2);
        int textPathY=(int)(Math.sin(Math.toRadians(currentStartAngle+pie.getAngle()/2))*
                pieAxisData.getAxisLength()*(1+pieAxisData.getOutsideRadiusScale())/2);
        mPointF.x=textPathX;
        mPointF.y=textPathY;
        String[] strings;
        if(flag) {
            strings = new String[]{pie.getName(), numberFormat.format(pie.getPercentage()) + ""};
        }else {
            strings=new String[]{numberFormat.format(pie.getPercentage())+""};
        }
        textCenter(strings,textPaint,canvas,mPointF,Paint.Align.CENTER);
    }

    public void setMarkerLineLength(float markerLineLength) {
        this.markerLineLength = markerLineLength;
    }

    @Override
    public void setTouchFlag(boolean touchFlag) {
        this.touchFlag=touchFlag;
    }
}
