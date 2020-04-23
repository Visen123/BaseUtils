package com.yanyiyun.baseutils.library.view.chart.interfaces.iChart;

/**
 * 雷达图绘制类接口
 */
public interface IRadarChart {

    /**
     * 计算雷达图坐标
     */
    public void computeRadar();

    /**
     * 设置各角类型文本颜色
     * @param color
     */
    public void setAxisValueColor(int color);

    /**
     * 设置各角文本集合
     * @param types  文本字符串集合
     */
    public void setType(String[] types);
}
