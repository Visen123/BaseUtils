package com.yanyiyun.baseutils.activity.selfview.recyclerview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.adapter.SwipeMenuRecyclerViewAdapter;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.baseutils.library.tool.UnitConversionTool;
import com.yanyiyun.baseutils.library.view.recyclerView.ItemDecoration.DefaultItemDecoration;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.entity.SwipeMenu;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.entity.SwipeMenuBridge;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.entity.SwipeMenuItem;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.interfaces.SwipeItemClickListener;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.interfaces.SwipeMenuCreator;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.interfaces.SwipeMenuItemClickListener;
import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.view.SwipeMenuRecyclerView;

import java.util.ArrayList;

/**
 * 侧滑菜单
 */
public class SwipeMenuActivity extends BaseActivity implements View.OnClickListener,SwipeItemClickListener {

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
        head_text_title.setText("SwipeMenu");

        list_smrv=findViewById(R.id.list_smrv);

        //SwipeItemClickListener必须设置
        list_smrv.setSwipeItemClickListener(this);

        adapter=new SwipeMenuRecyclerViewAdapter(this,data);
        //设置LayoutManaget
        list_smrv.setLayoutManager(new LinearLayoutManager(this));
        //添加ItemDecoration
        list_smrv.addItemDecoration(new DefaultItemDecoration(ContextCompat.
                getColor(this,R.color.tab_text_unselect)));
        //设置侧滑菜单创建器
        list_smrv.setSwipeMenuCreator(swipeMenuCreator);

        //设置菜单点击事件，必须要在调用setAdapter之前
        list_smrv.setSwipeMenuItemClickListener(menuItemClickListener);
        list_smrv.setAdapter(adapter);

    }

    private void initData(){
        for(int i=0;i<20;i++){
            data.add("第"+i+"个Item");
        }
    }

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

    @Override
    public void onItemClick(View itemView, int position) {
        Toast.makeText(mContext, "第" + position + "个", Toast.LENGTH_SHORT).show();
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
