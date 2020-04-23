package com.yanyiyun.baseutils.activity.selfview.recyclerview;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.adapter.SwipeMenuRecyclerViewAdapter;
import com.yanyiyun.function.BaseToolActivity;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.view.recyclerView.ItemDecoration.DefaultItemDecoration;
import com.yanyiyun.view.recyclerView.swipeMenu.interfaces.SwipeItemClickListener;
import com.yanyiyun.view.recyclerView.swipeMenu.view.SwipeMenuRecyclerView;

import java.util.ArrayList;

/**
 * 自定义加载更多
 */
public class DefineLoadMoreActivity extends BaseToolActivity implements View.OnClickListener {

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
        head_text_title.setText("自定义加载更多");

        refresh_layout=findViewById(R.id.refresh_layout);
        refresh_layout.setOnRefreshListener(refreshListener);

        list_lv=findViewById(R.id.list_lv);
        list_lv.setSwipeItemClickListener(mItemClickListener);

        list_lv.setLayoutManager(new LinearLayoutManager(mContext));
        list_lv.addItemDecoration(new DefaultItemDecoration(ContextCompat
                .getColor(mContext,R.color.tab_text_unselect)));

        // 自定义的核心就是DefineLoadMoreView类。
        DefineLoadMoreView defineLoadMoreView=new DefineLoadMoreView(mContext);
        list_lv.addFooterView(defineLoadMoreView);
        list_lv.setLoadMoreView(defineLoadMoreView);
        list_lv.setLoadMoreListener(loadMoreListener);

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

    private ArrayList<String> createDataList(int start){
        ArrayList<String> list=new ArrayList<>();
        for(int i=start;i<start+20;i++){
            list.add("第"+i+"个Item");
        }
        return list;
    }

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

    static final class DefineLoadMoreView extends LinearLayout implements SwipeMenuRecyclerView.LoadMoreView,View.OnClickListener{

        private ProgressBar mProgressBar;
        private TextView mTvMessage;
        private SwipeMenuRecyclerView.LoadMoreListener mLoadMoreListener;

        public DefineLoadMoreView(Context context){
            super(context);
            setLayoutParams(new ViewGroup.LayoutParams(-1,-2));
            setGravity(Gravity.CENTER);
            setVisibility(GONE);

            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

            int minHeight = (int)(displayMetrics.density * 60 + 0.5);
            setMinimumHeight(minHeight);

            inflate(context, R.layout.layout_fotter_loadmore, this);
            mProgressBar = findViewById(R.id.progress_bar);
            mTvMessage = findViewById(R.id.tv_message);
            setOnClickListener(this);
        }

        /**
         * 非自动加载更多时mLoadMoreListener才不为空。
         */
        @Override
        public void onClick(View v) {
            if (mLoadMoreListener != null) mLoadMoreListener.onLoadMore();
        }

        /**
         * 马上开始回调加载更多了，这里应该显示进度条。
         */
        @Override
        public void onLoading() {
            setVisibility(VISIBLE);
            mProgressBar.setVisibility(VISIBLE);
            mTvMessage.setVisibility(VISIBLE);
            mTvMessage.setText("正在努力加载，请稍后");
        }

        /**
         * 加载更多完成了。
         *
         * @param dataEmpty 是否请求到空数据。
         * @param hasMore 是否还有更多数据等待请求。
         */
        @Override
        public void onLoadFinish(boolean dataEmpty, boolean hasMore) {
            if (!hasMore) {
                setVisibility(VISIBLE);

                if (dataEmpty) {
                    mProgressBar.setVisibility(GONE);
                    mTvMessage.setVisibility(VISIBLE);
                    mTvMessage.setText("暂时没有数据");
                } else {
                    mProgressBar.setVisibility(GONE);
                    mTvMessage.setVisibility(VISIBLE);
                    mTvMessage.setText("没有更多数据啦");
                }
            } else {
                setVisibility(INVISIBLE);
            }
        }

        /**
         * 调用了setAutoLoadMore(false)后，在需要加载更多的时候，这个方法会被调用，并传入加载更多的listener。
         */
        @Override
        public void onWaitToLoadMore(SwipeMenuRecyclerView.LoadMoreListener loadMoreListener) {
            this.mLoadMoreListener = loadMoreListener;

            setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
            mTvMessage.setVisibility(VISIBLE);
            mTvMessage.setText("点我加载更多");
        }

        /**
         * 加载出错啦，下面的错误码和错误信息二选一。
         *
         * @param errorCode 错误码。
         * @param errorMessage 错误信息。
         */
        @Override
        public void onLoadError(int errorCode, String errorMessage) {
            setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
            mTvMessage.setVisibility(VISIBLE);

            // 这里要不直接设置错误信息，要不根据errorCode动态设置错误数据。
            mTvMessage.setText(errorMessage);
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
