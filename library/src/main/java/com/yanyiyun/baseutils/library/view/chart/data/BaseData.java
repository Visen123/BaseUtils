package com.yanyiyun.baseutils.library.view.chart.data;

import android.graphics.Color;

import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IBaseData;

/**
 * 所有数据基类
 */
public class BaseData implements IBaseData {

    private int color=Color.BLACK;
    private float paintWidth=1;
    private float textSize=30;
    private boolean isTextSize=true;

    @Override
    public void setIsTextSize(boolean isTextSize) {
        this.isTextSize=isTextSize;
    }

    @Override
    public boolean getIsTextSize() {
        return isTextSize;
    }

    @Override
    public void setColor(int color) {
        this.color=color;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void setTextSize(float textSize) {
        this.textSize=textSize;
    }

    @Override
    public float getTextSize() {
        return textSize;
    }

    @Override
    public void setPaintWidth(float paintWidth) {
        this.paintWidth=paintWidth;
    }

    @Override
    public float getPaintWidth() {
        return paintWidth;
    }
}
