package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.library.function.bottomnavigation.BottomNavigationBar;
import com.yanyiyun.baseutils.library.function.bottomnavigation.BottomNavigationItem;
import com.yanyiyun.baseutils.library.function.bottomnavigation.badgeitem.TextBadgeItem;

/**
 * BottomNavigationBar
 */
public class BottomNavigationActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;
    private BottomNavigationBar bottom_bnv;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navigation_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("BottomNavigationBar");

        //item的右上角文字
       TextBadgeItem textBadgeItem=new TextBadgeItem();
       textBadgeItem.setBackgroundColor(Color.RED);
       textBadgeItem.setTextColor(Color.WHITE);
       textBadgeItem.setText("99");

        bottom_bnv=findViewById(R.id.bottom_bnv);
        bottom_bnv.setMode(BottomNavigationBar.MODE_FIXED);
        bottom_bnv.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottom_bnv.setBarBackgroundColor(android.R.color.white);
        bottom_bnv.addItem(new BottomNavigationItem(R.mipmap.gb_sy_h,"主页")
                               .setInactiveIcon(getResources().getDrawable(R.mipmap.gb_sy))
                               .setActiveColorResource(R.color.tab_color).setBadgeItem(textBadgeItem))
                  .addItem(new BottomNavigationItem(R.mipmap.gb_gwc_h,"购物车")
                               .setInactiveIcon(getResources().getDrawable(R.mipmap.gb_gwc))
                               .setActiveColorResource(R.color.tab_color))
                  .addItem(new BottomNavigationItem(R.mipmap.gb_nav_kf_h,"客服")
                               .setInactiveIcon(getResources().getDrawable(R.mipmap.gb_kf))
                               .setActiveColorResource(R.color.tab_color))
                  .addItem(new BottomNavigationItem(R.mipmap.gb_nav_wd_h,"我的")
                               .setInactiveIcon(getResources().getDrawable(R.mipmap.gb_wd))
                               .setActiveColorResource(R.color.tab_color))
                  .setFirstSelectedPosition(0)
                  .setActiveColor(R.color.tab_color)
                  .setInActiveColor(R.color.tab_text_unselect)
                  .initialise();//初始化
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
