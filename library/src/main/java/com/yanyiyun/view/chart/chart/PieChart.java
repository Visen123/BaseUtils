package com.yanyiyun.view.chart.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yanyiyun.view.chart.animation.ChartAnimator;
import com.yanyiyun.view.chart.compute.ComputePie;
import com.yanyiyun.view.chart.data.PieAxisData;
import com.yanyiyun.view.chart.interfaces.iChart.IPieChart;
import com.yanyiyun.view.chart.interfaces.iData.IPieAxisData;
import com.yanyiyun.view.chart.interfaces.iData.IPieData;
import com.yanyiyun.view.chart.render.ChartRender;
import com.yanyiyun.view.chart.render.PieChartRender;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class PieChart extends PieRadarChart<IPieData> implements IPieChart {

    /**
     * 半径
     */
    private float radius,radiusOut,radiusIn;
    /**
     * 放大半径
     */
    private float offsetRadius,offsetRadiusOut,offsetRadiusIn;
    /**
     * 坐标数据
     */
    private IPieAxisData mPieAxisData = new PieAxisData();
    /**
     * 扇形 透明 白色圆弧的外切矩形
     */
    private RectF[] mRectFs = new RectF[3];
    private RectF rectF=new RectF(),rectFOut=new RectF(),rectFIn=new RectF();
    /**
     * 放大的 扇形 透明 白色圆弧的外切矩形
     */
    private RectF[] mOffsetRectFs = new RectF[3];
    private RectF offsetRectF = new RectF(),offsetRectFOut = new RectF(),offsetRectFIn = new RectF();

    private PieChartRender pieChartRender;

    /**
     * 是否显示动画
     */
    private boolean isAnimated=true;
    private float animatedValue;
    private ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    private ChartAnimator mChartAnimator;

    // 点击判断
    private boolean isDownTouch = false;

    private int angleId = -1;

    private ComputePie computePie=new ComputePie(mPieAxisData);

    public PieChart(Context context) {
        this(context,null);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PieChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        radius=Math.min(mWidth,mHeight)*0.4f;

        if(radius*2>Math.min(mWidth,mHeight)){
            radius=0;
        }

        mPieAxisData.setAxisLength(radius);

        //饼状图绘制区域
        rectF.left=-radius;
        rectF.top=-radius;
        rectF.right=radius;
        rectF.bottom=radius;
        mRectFs[0]=rectF;

        //透明圆弧 白色圆弧
        radiusOut=radius*mPieAxisData.getOutsideRadiusScale();
        rectFOut.left=-radiusOut;
        rectFOut.top=-radiusOut;
        rectFOut.right=radiusOut;
        rectFOut.bottom=radiusOut;
        mRectFs[1]=rectFOut;

        //白色扇形
        radiusIn=radius*mPieAxisData.getInsideRadiusScale();
        rectFIn.left=-radiusIn;
        rectFIn.top=-radiusIn;
        rectFIn.right=radiusIn;
        rectFIn.bottom=radiusIn;
        mRectFs[2]=rectFIn;

        //放大圆环 半径
        offsetRadius=radius*mPieAxisData.getOffsetRadiusScale();

        //饼状图绘制区域
        offsetRectF.left=-offsetRadius;
        offsetRectF.top=-offsetRadius;
        offsetRectF.right=offsetRadius;
        offsetRectF.bottom=offsetRadius;
        mOffsetRectFs[0]=offsetRectF;

        //透明圆弧 白色圆弧
        offsetRadiusOut=offsetRadius*mPieAxisData.getOutsideRadiusScale();
        offsetRectFOut.left=-offsetRadiusOut;
        offsetRectFOut.top=-offsetRadiusOut;
        offsetRectFOut.right=offsetRadiusOut;
        offsetRectFOut.bottom=offsetRadiusOut;
        mOffsetRectFs[1]=offsetRectFOut;

        //白色扇形
        offsetRadiusIn=offsetRadius*mPieAxisData.getInsideRadiusScale();
        offsetRectFIn.left=-offsetRadiusIn;
        offsetRectFIn.top=-offsetRadiusIn;
        offsetRectFIn.right=offsetRadiusIn;
        offsetRectFIn.bottom=offsetRadiusIn;
        mOffsetRectFs[2]=offsetRectFIn;

        /**
         * 设置扇形半径、透明内外半径属性
         */
        mPieAxisData.setRectFs(mRectFs);
        mPieAxisData.setOffsetRectFs(mOffsetRectFs);

        animated();

        chartRenders.clear();
        for(int i=0;i<mDatas.size();i++){
            pieChartRender=new PieChartRender(mPieAxisData,mDatas.get(i));
            chartRenders.add(pieChartRender);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        canvas.translate(mViewWidth/2,mViewHeight/2);

        canvas.save();

        canvas.rotate(mPieAxisData.getStartAngle());
        for(ChartRender chartRender:chartRenders){
            chartRender.drawGraph(canvas,animatedValue);
        }
        canvas.restore();

        for (ChartRender chartRender : chartRenders){
            PieChartRender pieChartRender = (PieChartRender)chartRender;
            pieChartRender.drawGraphText(canvas,animatedValue);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDatas.size()>0){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX()-(mWidth/2);
                    float y = event.getY()-(mHeight/2);
                    float touchAngle = 0;
                    if (x<0&&y<0){
                        touchAngle += 180;
                    }else if (y<0&&x>0){
                        touchAngle += 360;
                    }else if (y>0&&x<0){
                        touchAngle += 180;
                    }
                    touchAngle +=Math.toDegrees(Math.atan(y/x));
                    touchAngle = touchAngle-mPieAxisData.getStartAngle();
                    if (touchAngle<0){
                        touchAngle = touchAngle+360;
                    }
                    float touchRadius = (float) Math.sqrt(y*y+x*x);
                    if (radiusOut< touchRadius && touchRadius< radius){
                        angleId = -Arrays.binarySearch(mPieAxisData.getStartAngles(),(touchAngle))-1;
                        PieChartRender pieChartRender = (PieChartRender)chartRenders.get(angleId);
                        pieChartRender.setTouchFlag(true);
                        invalidate();
                        isDownTouch = true;
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    if (isDownTouch){
                        PieChartRender pieChartRender = (PieChartRender)chartRenders.get(angleId);
                        pieChartRender.setTouchFlag(false);
                        invalidate();
                        isDownTouch = false;
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    protected void animated(){
        if(!isAnimated){
            animatedValue=360f;
        }else {
            mAnimatorUpdateListener=new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    animatedValue= (float) animation.getAnimatedValue();
                    invalidate();
                }
            };
            mChartAnimator=new ChartAnimator(mAnimatorUpdateListener);
            mChartAnimator.animatedY(2000,360f);
        }
    }

    @Override
    public void setDataList(ArrayList<IPieData> chartDatas) {
        super.setDataList(chartDatas);
        computePie();
    }

    @Override
    public void setData(IPieData chartData) {
        super.setData(chartData);
        computePie();
    }

    @Override
    public int getCurrentWidth() {
        int wrapSize;
        if (mDatas!=null&&mDatas.size()>1){
            NumberFormat numberFormat =NumberFormat.getPercentInstance();
            numberFormat.setMinimumFractionDigits(mPieAxisData.getDecimalPlaces());
            paintText.setTextSize(mPieAxisData.getTextSize());
            paintText.setStrokeWidth(mPieAxisData.getPaintWidth());
            float percentWidth = paintText.measureText(numberFormat.format(10));
            float nameWidth = paintText.measureText(mPieAxisData.getName());
            wrapSize = (int) ((percentWidth*4+nameWidth*1.1)* mPieAxisData.getOffsetRadiusScale());
        }else {
            wrapSize = 0;
        }
        return wrapSize;
    }

    @Override
    public int getCurrentHeight() {
        int wrapSize;
        if (mDatas!=null&&mDatas.size()>1){
            NumberFormat numberFormat =NumberFormat.getPercentInstance();
            numberFormat.setMinimumFractionDigits(mPieAxisData.getDecimalPlaces());
            paintText.setTextSize(mPieAxisData.getTextSize());
            paintText.setStrokeWidth(mPieAxisData.getPaintWidth());
            float percentWidth = paintText.measureText(numberFormat.format(10));
            float nameWidth = paintText.measureText(mPieAxisData.getName());
            wrapSize = (int) ((percentWidth*4+nameWidth*1.1)* mPieAxisData.getOffsetRadiusScale());
        }else {
            wrapSize = 0;
        }
        return wrapSize;
    }


    @Override
    public void setAxisTextSize(float axisTextSize) {
        mPieAxisData.setTextSize(axisTextSize);
    }

    @Override
    public void setAxisColor(int axisColor) {
        mPieAxisData.setColor(axisColor);
    }

    @Override
    public void setAxisWidth(float axisWidth) {
        mPieAxisData.setPaintWidth(axisWidth);
    }

    @Override
    public void computePie() {
        computePie.computePie(mDatas);
    }

    @Override
    public void setInsideRadiusScale(float insideRadiusScale) {
        mPieAxisData.setInsideRadiusScale(insideRadiusScale);
    }

    @Override
    public void setOutsideRadiusScale(float outsideRadiusScale) {
        mPieAxisData.setOutsideRadiusScale(outsideRadiusScale);
    }

    @Override
    public void setOffsetRadiusScale(float offsetRadiusScale) {
        mPieAxisData.setOffsetRadiusScale(offsetRadiusScale);
    }

    @Override
    public void setStartAngle(float startAngle) {
        while (startAngle<0){
            startAngle = startAngle+360;
        }
        while (startAngle>360){
            startAngle = startAngle-360;
        }
        mPieAxisData.setStartAngle(startAngle);
    }

    @Override
    public void setMinAngle(float minAngle) {
        mPieAxisData.setMinAngle(minAngle);
    }

    @Override
    public void setDecimalPlaces(int decimalPlaces) {
        mPieAxisData.setDecimalPlaces(decimalPlaces);
    }
}
