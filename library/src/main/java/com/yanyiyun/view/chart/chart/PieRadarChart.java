package com.yanyiyun.view.chart.chart;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yanyiyun.view.chart.interfaces.iChart.IPieRadarChart;
import com.yanyiyun.view.chart.interfaces.iData.IChartData;

import java.util.ArrayList;

/**
 * 扇形图、雷达图绘制基类
 * @param <T>
 */
public abstract class PieRadarChart<T extends IChartData> extends Chart<T> implements IPieRadarChart {

    public PieRadarChart(Context context) {
        super(context);
    }

    public PieRadarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PieRadarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setData(T chartData) {
        mDatas.clear();
        mDatas.add(chartData);
    }

    @Override
    public void setDataList(ArrayList<T> chartDataList) {
        mDatas = chartDataList;
    }
}
