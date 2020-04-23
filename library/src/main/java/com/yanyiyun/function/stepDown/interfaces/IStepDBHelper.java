package com.yanyiyun.function.stepDown.interfaces;

import com.yanyiyun.function.stepDown.StepData;

import java.util.List;

public interface IStepDBHelper {

    /**
     * 创建表
     */
    public void createTable();

    /**
     * 删除表
     */
    public void deleteTable();

    /**
     * 根据limit来清除数据库
     *  例如：
     *      curDate = 2018-01-10 limit=0;表示只保留2018-01-10
     *      curDate = 2018-01-10 limit=1;表示保留2018-01-10、2018-01-09等
     * @param curData  清楚数据的开始时间
     * @param limit  -1失效  保留天数
     */
    public void clearCapacity(String curData,int limit);

    public boolean isExist(StepData stepData);

    /**
     * 本地是否存有某天的数据
     * @param time
     * @return
     */
    public boolean isExistDay(long time);

    public void insert(StepData stepData);

    /**
     * 获取最大步数，根据时间
     * @param millis
     * @return
     */
    StepData getMaxStepByDate(long millis);

    List<StepData> getQueryAll();

    /**
     * 根据时间获取步数列表
     *
     * @param dateString
     * @return
     */
    List<StepData> getStepListByData(String dateString);

    /**
     * 根据时间和天数获取步数列表
     * 例如：
     *     startDate = 2018-01-15
     *     days = 3
     *     2018-01-15、2018-01-16、2018-01-17三天的步数
     * @param startDate  格式yyyy-MM-dd
     * @param days
     * @return
     */
    List<StepData> getStepListByStartDateAndDays(String startDate,int days);
}
