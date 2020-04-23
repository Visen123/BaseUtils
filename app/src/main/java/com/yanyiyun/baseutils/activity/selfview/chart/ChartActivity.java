package com.yanyiyun.baseutils.activity.selfview.chart;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.adapter.MainAdapter;
import com.yanyiyun.baseutils.entity.MainItem;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;

import java.util.ArrayList;

public class ChartActivity extends BaseActivity implements View.OnClickListener {

    private TextView head_text_title;
    private ListView list_lv;

    private Context mContext;
    private ArrayList<MainItem> data=new ArrayList<>();
    private MainAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        mContext=this;
        initData();
        initview();
    }

    private void initData() {
        MainItem peiChart=new MainItem("pieChartView","饼状图");
        data.add(peiChart);
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("ChartView");

        list_lv=  findViewById(R.id.list_lv);
        adapter=new MainAdapter(mContext,data);
        list_lv.setAdapter(adapter);
        list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click(position);
            }
        });
    }

    private void click(int position){
        switch (position){
            //饼状图
            case 0:
                startIntent(mContext,PieChartViewActivity.class);
                break;
        }
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
