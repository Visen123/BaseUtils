package com.yanyiyun.baseutils.library.view.chart.data;

import android.graphics.Color;

import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IRadarAxisData;

/**
 * 雷达图坐标系类
 */
public class RadarAxisData extends AxisData implements IRadarAxisData {
    private String[] types;
    private int webColor = Color.GRAY;
    private float[] cosArray;
    private float[] sinArray;

    @Override
    public void setType(String[] type) {
        this.types=type;
    }

    @Override
    public String[] getTypes() {
        return types;
    }

    @Override
    public void setWebColor(int webColor) {
        this.webColor=webColor;
    }

    @Override
    public int getWebColor() {
        return webColor;
    }

    @Override
    public float[] getCosArray() {
        return cosArray;
    }

    @Override
    public void setCosArray(float[] cosArray) {
        this.cosArray=cosArray;
    }

    @Override
    public float[] getSinArray() {
        return sinArray;
    }

    @Override
    public void setSinArray(float[] sinArray) {
        this.sinArray=sinArray;
    }
}
