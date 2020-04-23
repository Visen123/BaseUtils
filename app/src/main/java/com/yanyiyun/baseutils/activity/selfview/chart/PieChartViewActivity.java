package com.yanyiyun.baseutils.activity.selfview.chart;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.view.chart.chart.PieChart;
import com.yanyiyun.view.chart.data.PieData;
import com.yanyiyun.view.chart.interfaces.iData.IPieData;

import java.util.ArrayList;

public class PieChartViewActivity extends BaseActivity implements View.OnClickListener {

    private TextView head_text_title;
    private PieChart pie_view;

    private ArrayList<IPieData> data=new ArrayList<>();
    private Context mContext;
    protected int[] mColors = {0xFFCCFF00, 0xFF6495ED, 0xFFE32636, 0xFF800000, 0xFF808000, 0xFFFF8C69, 0xFF808080,
            0xFFE6B800, 0xFF7CFC00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pei_chart_view_activity);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("PieChartView");

        pie_view=findViewById(R.id.pie_view);

        for (int i=0; i<9; i++){
            PieData pieData = new PieData();
            pieData.setName("区域"+i);
            pieData.setValue((float)i+1);
            pieData.setColor(mColors[i]);
            data.add(pieData);
        }

        pie_view.setDataList(data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
        }
    }
}
