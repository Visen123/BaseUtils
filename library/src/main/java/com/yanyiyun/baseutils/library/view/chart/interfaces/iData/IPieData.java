package com.yanyiyun.baseutils.library.view.chart.interfaces.iData;

public interface IPieData extends IChartData {

    /**
     * 设置单个扇形区域的数据
     * @param value
     */
    void setValue(float value);

    /**
     * 获得单个扇形区域的数据
     * @return
     */
    float getValue();

    /**
     * 设置扇形区域颜色
     * @param color
     */
    void setColor(int color);

    /**
     * 扇形区域颜色
     * @return
     */
    int getColor();

    /**
     * 设置扇形区域标记文本
     * @param marker
     */
    void setMarker(String marker);

    /**
     * 扇形区域标记文本
     * @return
     */
    String getMarker();

    /**
     * 设置扇形区域的百分比
     * @param percentage  百分比小数
     */
    void setPercentage(float percentage);

    /**
     * 获得扇形区域的百分比
     * @return
     */
    float getPercentage();

    /**
     * 设置扇形区域经过角度
     * @param angle 角度
     */
    void setAngle(float angle);

    /**
     * 获取扇形区域经过角度
     * @return 角度
     */
    float getAngle();

    /**
     * 获取扇形区域的当前起始角度
     * @return 角度
     */
    float getCurrentAngle();

    /**
     * 设置扇形区域的当前起始角度
     * @param currentAngle 角度
     */
    void setCurrentAngle(float currentAngle);
}
