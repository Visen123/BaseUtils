package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;

public class CircleImageViewDemoActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.circleimageview_demo_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("CircleImageView");
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
