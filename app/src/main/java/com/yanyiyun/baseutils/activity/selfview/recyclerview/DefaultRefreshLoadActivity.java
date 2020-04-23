package com.yanyiyun.baseutils.activity.selfview.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.adapter.SwipeMenuRecyclerViewAdapter;
import com.yanyiyun.baseutils.library.function.BaseToolActivity;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.baseutils.library.view.recyclerView.ItemDecoration.DefaultItemDecoration;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.interfaces.SwipeItemClickListener;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.view.SwipeMenuRecyclerView;

import java.util.ArrayList;

/**
 * 默认下拉刷新加载更多
 */
public class DefaultRefreshLoadActivity extends BaseToolActivity implements View.OnClickListener {

    private TextView head_text_title;
    private SwipeRefreshLayout refresh_layout;
    private SwipeMenuRecyclerView list_lv;

    private Context mContext;
    private SwipeMenuRecyclerViewAdapter adapter;
    private ArrayList<String> data=new ArrayList<>();
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.default_refresh_load_activity);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("下拉刷新加载更多");

        refresh_layout=findViewById(R.id.refresh_layout);
        //设置下拉刷新
        refresh_layout.setOnRefreshListener(refreshListener);

        list_lv=findViewById(R.id.list_lv);

        //设置加载更多
        list_lv.setLoadMoreListener(loadMoreListener);
        list_lv.setSwipeItemClickListener(mItemClickListener);

        list_lv.setLayoutManager(new LinearLayoutManager(mContext));
        list_lv.addItemDecoration(new DefaultItemDecoration(ContextCompat.
                getColor(mContext,R.color.tab_text_unselect)));

        adapter=new SwipeMenuRecyclerViewAdapter(mContext,data);
        list_lv.setAdapter(adapter);

        loadData();
    }

    private void loadData(){
        data.clear();
        data.addAll(createDataList(0));
        adapter.notifyDataSetChanged();
        refresh_layout.setRefreshing(false);
        // 第一次加载数据：一定要调用这个方法，否则不会触发加载更多。
        // 第一个参数：表示此次数据是否为空，假如你请求到的list为空(== null || list.size == 0)，那么这里就要true。
        // 第二个参数：表示是否还有更多数据，根据服务器返回给你的page等信息判断是否还有更多，这样可以提供性能，如果不能判断则传true。
        list_lv.loadMoreFinish(false,true);
    }

    /**
     * 加载更多监听
     */
    private SwipeMenuRecyclerView.LoadMoreListener loadMoreListener=new SwipeMenuRecyclerView.LoadMoreListener() {
        @Override
        public void onLoadMore() {
            list_lv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ArrayList<String> list=createDataList(adapter.getItemCount());
                    data.addAll(list);
                    adapter.notifyItemRangeInserted(data.size()-list.size(),list.size());
                    // 数据完更多数据，一定要掉用这个方法。
                    // 第一个参数：表示此次数据是否为空。
                    // 第二个参数：表示是否还有更多数据。
                    list_lv.loadMoreFinish(false,true);

                    // 如果加载失败调用下面的方法，传入errorCode和errorMessage。
                    // errorCode随便传，你自定义LoadMoreView时可以根据errorCode判断错误类型。
                    // errorMessage是会显示到loadMoreView上的，用户可以看到。
                    // mRecyclerView.loadMoreError(0, "请求网络失败");
                }
            },1000);
        }
    };

    private ArrayList<String> createDataList(int start){
        ArrayList<String> list=new ArrayList<>();
        for(int i=start;i<start+20;i++){
            list.add("第"+i+"个Item");
        }
        return list;
    }

    /**
     * Item点击监听。
     */
    private SwipeItemClickListener mItemClickListener = new SwipeItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            showToast("第" + position + "个");
        }
    };

    /**
     * 刷新监听器
     */
    private SwipeRefreshLayout.OnRefreshListener refreshListener=new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            list_lv.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loadData();
                }
            },1000);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
        }
    }
}
