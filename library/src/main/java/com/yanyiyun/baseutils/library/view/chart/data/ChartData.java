package com.yanyiyun.baseutils.library.view.chart.data;

import com.yanyiyun.baseutils.library.view.chart.interfaces.iData.IChartData;

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
