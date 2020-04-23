package com.yanyiyun.view.chart.data;


import com.yanyiyun.view.chart.interfaces.iData.IPieData;

/**
 * 扇形数据类
 */
public class PieData extends ChartData implements IPieData {

    private float value;
    private String marker;
    private float percentage;
    private float angle = 0;
    private float currentAngle;

    @Override
    public String getMarker() {
        return marker;
    }

    @Override
    public void setPercentage(float percentage) {
        this.percentage=percentage;
    }

    @Override
    public float getPercentage() {
        return percentage;
    }

    @Override
    public void setAngle(float angle) {
        this.angle=angle;
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getCurrentAngle() {
        return currentAngle;
    }

    @Override
    public void setCurrentAngle(float currentAngle) {
        this.currentAngle=currentAngle;
    }

    @Override
    public void setMarker(String marker) {
        this.marker = marker;
    }

    @Override
    public float getValue() {
        return value;
    }

    @Override
    public void setValue(float value) {
        this.value = value;
    }

    @Override
    public void setIsTextSize(boolean isTextSize) {

    }

    @Override
    public boolean getIsTextSize() {
        return false;
    }
}
