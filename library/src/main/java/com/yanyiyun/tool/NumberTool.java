package com.yanyiyun.tool;

import java.math.BigDecimal;

public class NumberTool {

    /**
     * 向绝对值最大的方向舍入，只要舍弃位非0即进位
     * @param number
     * @param length
     * @return
     */
    public static double roundUp(double number,int length){
        return process(number,length,BigDecimal.ROUND_UP);
    }

    /**
     * 向绝对值最小的方向输入，所有的位都要舍弃，不存在进位情况
     * @param number
     * @param length
     * @return
     */
    public static double roundDown(double number,int length){
        return process(number,length,BigDecimal.ROUND_DOWN);
    }

    /**
     * 向正最大方向靠拢。若是正数，舍入行为类似于ROUND_UP，若为负数，舍入行为类似于ROUND_DOWN
     * 如 13.4 结果  14  -13.4 结果 -13
     * @param number
     * @param length
     * @return
     */
    public static double roundCeiling(double number,int length){
        return process(number,length,BigDecimal.ROUND_CEILING);
    }

    /**
     * 向负无穷方向舍入。向负无穷方向靠拢。若是正数，舍入行为类似于ROUND_DOWN；若为负数，舍入行为类似于ROUND_UP。
     * 如 13.4 结果  13  -13.4 结果 -14
     * @param number
     * @param length
     * @return
     */
    public static double roundFloor(double number,int length){
        return process(number,length,BigDecimal.ROUND_FLOOR);
    }

    /**
     * 最近数字舍入(5进)。这是我们最经典的四舍五入。
     * @param number
     * @param length
     * @return
     */
    public static double halfUp(double number,int length){
        return process(number,length,BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 最近数字舍入(5舍)。在这里5是要舍弃的。
     * @param number
     * @param length
     * @return
     */
    public static double halfDown(double number,int length){
        return process(number,length,BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * 银行家舍入法。
     * 11.556 = 11.56  六入
     * 11.554 = 11.55  四舍
     * 11.5551 = 11.56  五后有数进位
     * 11.545 = 11.54 五后无数，若前位为偶数应舍去
     * 11.555 = 11.56 五后无数，若前位为奇数应进位
     * @param number
     * @param length
     * @return
     */
    public static double hailEven(double number,int length){
        return process(number,length,BigDecimal.ROUND_HALF_EVEN);
    }

    public static double process(double number,int length,int type){
        BigDecimal bigDecimal=new BigDecimal(number);
        return bigDecimal.setScale(length,type).doubleValue();
    }
}
