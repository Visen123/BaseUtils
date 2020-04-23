package com.yanyiyun.view.chart.data;

import com.yanyiyun.view.chart.interfaces.iData.IChartData;

public class ChartData extends BaseData implements IChartData {

    protected String name="";

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String getName() {
        return name;
    }
}
