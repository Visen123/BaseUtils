package com.yanyiyun.baseutils.activity.selfview.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.adapter.SwipeMenuRecyclerViewAdapter;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.tool.UnitConversionTool;
import com.yanyiyun.view.recyclerView.ItemDecoration.DefaultItemDecoration;
import com.yanyiyun.view.recyclerView.swipeMenu.entity.SwipeMenu;
import com.yanyiyun.view.recyclerView.swipeMenu.entity.SwipeMenuBridge;
import com.yanyiyun.view.recyclerView.swipeMenu.entity.SwipeMenuItem;
import com.yanyiyun.view.recyclerView.swipeMenu.interfaces.SwipeItemClickListener;
import com.yanyiyun.view.recyclerView.swipeMenu.interfaces.SwipeMenuCreator;
import com.yanyiyun.view.recyclerView.swipeMenu.interfaces.SwipeMenuItemClickListener;
import com.yanyiyun.view.recyclerView.swipeMenu.touch.OnItemMoveListener;
import com.yanyiyun.view.recyclerView.swipeMenu.touch.OnItemStateChangedListener;
import com.yanyiyun.view.recyclerView.swipeMenu.view.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.Collections;

/**
 * 拖拽+侧滑菜单
 */
public class DragSwipeActivity extends BaseActivity implements View.OnClickListener,SwipeItemClickListener {

    private TextView head_text_title;
    private SwipeMenuRecyclerView list_smrv;

    private Context mContext;
    private SwipeMenuRecyclerViewAdapter adapter;
    private ArrayList<String> data=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.swipe_menu_recycler_view_demo_activitty);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        mContext=this;
        initData();
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("DragSwipe");

        list_smrv=findViewById(R.id.list_smrv);

        list_smrv.setSwipeMenuItemClickListener(menuItemClickListener);
        list_smrv.setSwipeMenuCreator(swipeMenuCreator);
        list_smrv.setSwipeItemClickListener(this);

        //设置LayoutManaget
        list_smrv.setLayoutManager(new LinearLayoutManager(this));
        //添加ItemDecoration
        list_smrv.addItemDecoration(new DefaultItemDecoration(ContextCompat.
                getColor(this,R.color.tab_text_unselect)));

        adapter=new SwipeMenuRecyclerViewAdapter(this,data);

        list_smrv.setAdapter(adapter);

        // 监听拖拽和侧滑删除，更新UI和数据源。
        list_smrv.setOnItemMoveListener(onItemMoveListener);
        //Item的拖拽/侧滑删除时，手指状态发生变化监听。
        list_smrv.setOnItemStateChangedListener(mOnItemStateChangedListener);

        //设置长按拖拽  默认关闭
        list_smrv.setLongPressDragEnabled(true);
        //滑动删除，默认关闭。
        list_smrv.setItemViewSwipeEnabled(false);

    }

    private void initData(){
        for(int i=0;i<20;i++){
            data.add("第"+i+"个Item");
        }
    }

    private SwipeMenuItemClickListener menuItemClickListener=new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            // 左侧还是右侧菜单。
            int direction=menuBridge.getDirection();
            // 菜单在RecyclerView的Item中的Position。
            int menuPosition=menuBridge.getPosition();

            if(direction==SwipeMenuRecyclerView.RIGHT_DIRECTION){
                Toast.makeText(mContext, "list第" + position + "; 右侧菜单第" + menuPosition,
                        Toast.LENGTH_SHORT) .show();
            }else if(direction==SwipeMenuRecyclerView.LEFT_DIRECTION){
                Toast.makeText(mContext, "list第" + position + "; 左侧菜单第" + menuPosition,
                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 菜单创建器
     */
    private SwipeMenuCreator swipeMenuCreator=new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int position) {

            int width= (int) UnitConversionTool.dip2px(mContext,90);
            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height=ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加左侧的，如果不添加，则左侧不会出现菜单。
            SwipeMenuItem addItem=new SwipeMenuItem(mContext)
                    .setBackground(R.drawable.selector_green)
                    .setImage(R.mipmap.ic_action_add)
                    .setWidth(width)
                    .setHeight(height);
            leftMenu.addMenuItem(addItem);

            SwipeMenuItem closeItem = new SwipeMenuItem(mContext)
                    .setBackground(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_close)
                    .setWidth(width)
                    .setHeight(height);
            leftMenu.addMenuItem(closeItem); // 添加菜单到左侧。

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            SwipeMenuItem deleteItem = new SwipeMenuItem(mContext).
                    setBackground(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            rightMenu.addMenuItem(deleteItem);// 添加菜单到右侧。

            SwipeMenuItem addItem1 = new SwipeMenuItem(mContext)
                    .setBackground(R.drawable.selector_green)
                    .setText("添加")
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            rightMenu.addMenuItem(addItem1); // 添加菜单到右侧。
        }
    };

    /**
     * 监听拖拽和侧滑删除，更新UI和数据源。
     */
    private OnItemMoveListener onItemMoveListener=new OnItemMoveListener() {
        //拖拽移动
        @Override
        public boolean onItemMove(RecyclerView.ViewHolder srcHolder, RecyclerView.ViewHolder targetHolder) {
            // 不同的ViewType不能拖拽换位置。
            if(srcHolder.getItemViewType()!=targetHolder.getItemViewType()){
                return false;
            }
            // 真实的Position：通过ViewHolder拿到的position都需要减掉HeadView的数量。
            int fromPosition=srcHolder.getAdapterPosition()-list_smrv.getHeaderItemCount();
            int toPosition=targetHolder.getAdapterPosition()-list_smrv.getHeaderItemCount();
            Collections.swap(data,fromPosition,toPosition);
            adapter.notifyItemMoved(fromPosition,toPosition);
            // 返回true表示处理了并可以换位置，返回false表示你没有处理并不能换位置。
            return true;
        }

        //侧滑删除
        @Override
        public void onItemDismiss(RecyclerView.ViewHolder srcHolder) {
            int adapterPosition=srcHolder.getAdapterPosition();
            int position=adapterPosition-list_smrv.getHeaderItemCount();

            if(list_smrv.getHeaderItemCount()>0&&adapterPosition==0){
//                list_smrv.removeHeaderView(mHeaderView);
                Toast.makeText(mContext, "HeaderView被删除。", Toast.LENGTH_SHORT).show();
            }else {
                data.remove(position);
                adapter.notifyDataSetChanged();
                Toast.makeText(mContext, "现在的第" + position + "条被删除。", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * Item的拖拽/侧滑删除时，手指状态发生变化监听。
     */
    private OnItemStateChangedListener mOnItemStateChangedListener=new OnItemStateChangedListener() {
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if(actionState==OnItemStateChangedListener.ACTION_STATE_DRAG){
                // 拖拽的时候背景就透明了，这里我们可以添加一个特殊背景。
                viewHolder.itemView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white_pressed));
            }else if(actionState==OnItemStateChangedListener.ACTION_STATE_SWIPE){
                //侧滑删除
            }else if(actionState==OnItemStateChangedListener.ACTION_STATE_IDLE){
                //在手松开的时候还原背景。
                ViewCompat.setBackground(viewHolder.itemView,ContextCompat.getDrawable(mContext,R.drawable.select_white));
            }
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

    @Override
    public void onItemClick(View itemView, int position) {
        Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_SHORT).show();
    }
}
