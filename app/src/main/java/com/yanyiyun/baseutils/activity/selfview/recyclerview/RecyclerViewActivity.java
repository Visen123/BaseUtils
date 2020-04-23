package com.yanyiyun.baseutils.activity.selfview.recyclerview;

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

public class RecyclerViewActivity extends BaseActivity implements View.OnClickListener {

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

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("RecyclerView");

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

    private void initData(){
        MainItem swipeMenu=new MainItem("SwipeMenu","侧滑菜单");
        data.add(swipeMenu);
        MainItem dragSwipe=new MainItem("DragSwipe","拖拽+侧滑");
        data.add(dragSwipe);
        MainItem defaultRefreshLoad=new MainItem("DefaultRefreshLoad","默认的下拉刷新加载更多");
        data.add(defaultRefreshLoad);
        MainItem defineLoadMore=new MainItem("DefineLoadMore","自定义加载更多");
        data.add(defaultRefreshLoad);
    }
    private void click(int position){
        switch (position){
            //侧滑菜单
            case 0:
                startIntent(mContext,SwipeMenuActivity.class);
                break;
                //拖拽+侧滑
            case 1:
                startIntent(mContext,DragSwipeActivity.class);
                break;
                //默认的下拉刷新加载更多
            case 2:
                startIntent(mContext,DefaultRefreshLoadActivity.class);
                break;
                //自定义加载更多
            case 3:
                startIntent(mContext,DefineLoadMoreActivity.class);
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
