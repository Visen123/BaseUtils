package com.yanyiyun.baseutils.library.view.chart.chart;

import android.content.Context;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.yanyiyun.baseutils.library.view.chart.data.RadarAxisData;
import com.yanyiyun.baseutils.library.view.chart.interfaces.iChart.IRadarChart;
import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IRadarAxisData;
import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IRadarData;

/**
 * 雷达图绘制类
 */
public class RadarChart extends PieRadarChart<IRadarData>  implements IRadarChart {

    protected IRadarAxisData mRadarAxisData=new RadarAxisData();

    private PathMeasure measure=new PathMeasure();

    public RadarChart(Context context) {
        super(context);
    }

    public RadarChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RadarChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    @Override
    public int getCurrentWidth() {
        return 0;
    }

    @Override
    public int getCurrentHeight() {
        return 0;
    }

    @Override
    public void setAxisTextSize(float axisTextSize) {

    }

    @Override
    public void setAxisColor(int axisColor) {

    }

    @Override
    public void setAxisWidth(float axisWidth) {

    }

    @Override
    public void computeRadar() {

    }

    @Override
    public void setAxisValueColor(int color) {

    }

    @Override
    public void setType(String[] types) {

    }
}
