package com.yanyiyun.baseutils.library.view.chart.interfaces.iData;

import java.util.ArrayList;

public interface IRadarData extends IChartData {

    /**
     * 设置雷达图值
     * @param value  数据集合
     */
    public void setValue(ArrayList<Float> value);

    /**
     * 获取雷达图值
     * @return 数据集合
     */
    public ArrayList<Float> getValue();

    /**
     * 设置雷达图透明度
     * @param alpha 透明度
     */
    public void setAlpha(int alpha);

    /**
     * 获取雷达图透明度
     * @return 透明度
     */
    public int getAlpha();
}
