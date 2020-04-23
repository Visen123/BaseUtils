package com.yanyiyun.view.chart.data;

import com.yanyiyun.view.chart.interfaces.iData.IAxisData;

/**
 * 坐标轴数据基类
 */
public class AxisData extends BaseData implements IAxisData {

    protected float axisLength;
    protected  float maximum;
    protected float minimum;
    protected float interval;
    protected  String unit="";
    /**
     * 辅助收敛坐标轴
     */
    protected float narrowMax;
    protected float narrowMin;

    /**
     * 小数位数
     */
    protected int decimalPlaces;
    /**
     * 数据坐标与实际坐标比例
     */
    protected float axisScale;
    @Override
    public void setAxisLength(float axisLength) {
        this.axisLength=axisLength;
    }

    @Override
    public float getAxisLength() {
        return axisLength;
    }

    @Override
    public void setMaximum(float maximum) {
        this.maximum=maximum;
    }

    @Override
    public float getMaximum() {
        return maximum;
    }

    @Override
    public void setMinimum(float minimum) {
        this.minimum=minimum;
    }

    @Override
    public float getMinimum() {
        return minimum;
    }

    @Override
    public void setInterval(float interval) {
        this.interval=interval;
    }

    @Override
    public float getInterval() {
        return interval;
    }

    @Override
    public void setUnit(String unit) {
        this.unit=unit;
    }

    @Override
    public String getUnit() {
        return unit;
    }

    @Override
    public void setDecimalPlaces(int decimalPlaces) {
        this.decimalPlaces=decimalPlaces;
    }

    @Override
    public int getDecimalPlaces() {
        return decimalPlaces;
    }

    @Override
    public void setAxisScale(float axisScale) {
        this.axisLength=axisScale;
    }

    @Override
    public float getAxisScale() {
        return axisScale;
    }

    @Override
    public void setNarrowMax(float narrowMax) {
        this.narrowMax=narrowMax;
    }

    @Override
    public float getNarrowMax() {
        return narrowMax;
    }

    @Override
    public void setNarrowMin(float narrowMin) {
        this.narrowMin=narrowMin;
    }

    @Override
    public float getNarrowMin() {
        return narrowMin;
    }

    @Override
    public String toString() {
        return "AxisData{" +
                "axisLength=" + axisLength +
                ", maximum=" + maximum +
                ", minimum=" + minimum +
                ", interval=" + interval +
                ", unit='" + unit + '\'' +
                ", narrowMax=" + narrowMax +
                ", narrowMin=" + narrowMin +
                ", decimalPlaces=" + decimalPlaces +
                ", axisScale=" + axisScale +
                '}';
    }
}
