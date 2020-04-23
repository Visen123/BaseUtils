package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.library.view.checkView.CheckableImageView;
import com.yanyiyun.baseutils.library.view.checkView.CheckableLinearLayout;

public class CheckViewDemoActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;
    private CheckableLinearLayout check_ll;
    private CheckableImageView item_iv;

    private Context mContext;
    private boolean isCheck=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkview_demo_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("CheckView");

        check_ll=findViewById(R.id.check_ll);
        //父视图复制子视图的状态
        check_ll.setAddStatesFromChildren(true);

        //告诉子视图去复制父视图的状态
//        cable_L.setDuplicateParentStateEnabled(true);
        check_ll.setBackground(initStateListDrawable());

        item_iv=findViewById(R.id.item_iv);
        item_iv.setImageDrawable(getImageStateLIstDrawable());
        item_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isCheck=!isCheck;
                item_iv.setChecked(isCheck);
            }
        });
    }

    private StateListDrawable getImageStateLIstDrawable(){
        StateListDrawable stateListDrawable=new StateListDrawable();

        int check=android.R.attr.state_checked;

        stateListDrawable.addState(new int[]{check}, ContextCompat.getDrawable(mContext,R.mipmap.c));
        stateListDrawable.addState(new int[]{},ContextCompat.getDrawable(mContext,R.mipmap.cd));
        return stateListDrawable;
    }

    private StateListDrawable initStateListDrawable(){
        //初始化一个空对象
        StateListDrawable stateListDrawable=new StateListDrawable();

        //获取对应的属性值
        int check=android.R.attr.state_checked;

        //-check 为android.R.attr.state_checkable为false  int[]数组为空则表示没有任何状态设置的图片
        stateListDrawable.addState(new int[]{check},ContextCompat.getDrawable(mContext,android.R.color.holo_red_dark));
        stateListDrawable.addState(new int[]{},ContextCompat.getDrawable(mContext,android.R.color.holo_green_dark));
        return stateListDrawable;
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
