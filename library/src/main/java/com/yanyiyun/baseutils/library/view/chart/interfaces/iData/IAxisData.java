package com.yanyiyun.baseutils.library.view.chart.interfaces.iData;

/**
 * 坐标轴基类接口
 */
public interface IAxisData extends IBaseData {

    /**
     * 设置坐标轴长度
     * @param axisLength  长度
     */
    public void setAxisLength(float axisLength);

    /**
     * 获取坐标轴长度
     * @return 长度
     */
    public float getAxisLength();

    /**
     * 设置坐标轴最大值
     * @param maximum  最大值
     */
    public void setMaximum(float maximum);

    /**
     * 获取坐标轴最大值
     * @return
     */
    public float getMaximum();

    /**
     * 设置坐标轴最小值
     * @param minimum
     */
    public void setMinimum(float minimum);

    /**
     * 获取坐标轴最小值
     * @return
     */
    public float getMinimum();

    /**
     * 设置坐标轴区间值
     * @param interval
     */
    public void setInterval(float interval);

    /**
     * 获取坐标轴区间值
     * @return
     */
    public float getInterval();

    /**
     * 设置单位
     * @param unit
     */
    public void setUnit(String unit);

    /**
     * 获取单位
     * @return
     */
    public String getUnit();

    /**
     * 设置坐标轴小数点位数
     * @param decimalPlaces
     */
    public void setDecimalPlaces(int decimalPlaces);

    /**
     * 获取坐标轴小数点位数
     * @return
     */
    public int getDecimalPlaces();

    /**
     * 设置坐标轴刻度长度与View长度的比例
     * @param axisScale 比例
     */
    public void setAxisScale(float axisScale);

    /**
     * 获取坐标轴刻度长度与View长度的比例
     * @return
     */
    public float getAxisScale();

    /**
     * 保存计算前的坐标轴最大值
     * @param narrowMax
     */
    public void setNarrowMax(float narrowMax);

    /**
     * 获取计算前的坐标轴最大值
     * @return
     */
    public float getNarrowMax();

    /**
     * 保存计算前的坐标轴最小值
     * @param narrowMin
     */
    public void setNarrowMin(float narrowMin);

    /**
     * 获取计算前的坐标轴最小值
     * @return
     */
    public float getNarrowMin();
}
