package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;

public class AnimViewDemoActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animview_demo_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title= (TextView) findViewById(R.id.head_text_title);
        head_text_title.setText("实现ViewAnimationUtils动画的自定义View");
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
