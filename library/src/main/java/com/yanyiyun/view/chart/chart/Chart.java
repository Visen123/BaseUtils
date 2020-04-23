package com.yanyiyun.view.chart.chart;

import android.content.Context;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.yanyiyun.view.chart.interfaces.iChart.IChart;
import com.yanyiyun.view.chart.interfaces.iData.IChartData;
import com.yanyiyun.view.chart.render.ChartRender;

import java.util.ArrayList;

/**
 * 图标基类
 */
public abstract class Chart<T extends IChartData> extends View implements IChart {

    /**
     * 去除padding的宽高
     */
    protected int mWidth,mHeight;

    /**
     * 视图宽高
     */
    protected int mViewWidth,mViewHeight;

    /**
     * 图标数据
     */
    protected ArrayList<T> mDatas=new ArrayList<>();

    /**
     * 渲染列表
     */
    protected ArrayList<ChartRender> chartRenders=new ArrayList<>();

    protected Paint paintText=new Paint();

    public Chart(Context context) {
        this(context,null);
    }

    public Chart(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public Chart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(
                Math.max(getSuggestedMinimumWidth(),
                        resolveSize(getCurrentWidth(),
                                widthMeasureSpec)),
                Math.max(getSuggestedMinimumHeight(),
                        resolveSize(getCurrentHeight(),
                                heightMeasureSpec)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth=w;
        mViewHeight=h;

        mWidth=mViewWidth-getPaddingLeft()-getPaddingRight();
        mHeight=mViewHeight-getPaddingTop()-getPaddingBottom();
    }

    /**
     * 设置图表数据
     * @param chartDatas
     */
    public abstract void setDataList(ArrayList<T> chartDatas);

    /**
     * 设置图标数据
     * @param chartData
     */
    public abstract void setData(T chartData);

    public abstract int getCurrentWidth();
    public abstract int getCurrentHeight();
}
