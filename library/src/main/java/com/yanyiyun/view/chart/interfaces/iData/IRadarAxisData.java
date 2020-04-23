package com.yanyiyun.view.chart.interfaces.iData;

/**
 * 雷达图坐标系接口
 */
public interface IRadarAxisData extends IAxisData{

    /**
     * 设置雷达图各角字符串集合
     * @param type
     */
    public void setType(String[] type);

    /**
     * 获取雷达图各角字符串集合
     * @return
     */
    public String[] getTypes();

    /**
     * 设置雷达图坐标系颜色
     * @param webColor
     */
    public void setWebColor(int webColor);

    /**
     * 获取雷达图坐标系颜色
     * @return
     */
    public int getWebColor();

    /**
     *  获取雷达图cos数组
     * @return
     */
    public float[] getCosArray();

    /**
     * 设置雷达图cos数组
     * @param cosArray
     */
    public void setCosArray(float[] cosArray);

    /**
     * 获取雷达图sin数组
     * @return
     */
    public float[] getSinArray();

    /**
     * 设置雷达图sin数组
     * @param sinArray
     */
    public  void setSinArray(float[] sinArray);
}
