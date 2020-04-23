package com.yanyiyun.baseutils.activity;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.entity.LiveDataBean;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * LiveData例子
 */
public class LiveDataDemoActivity extends BaseActivity {

    @BindView(R.id.head_text_title) TextView head_text_title;
    @BindView(R.id.text_tv)TextView text_tv;
    @BindView(R.id.text_et)EditText text_et;

    private LiveDataBean liveDataBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.livedata_demo_activity);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        head_text_title.setText("LiveData例子");

        liveDataBean= ViewModelProviders.of(this).get(LiveDataBean.class);
    }

    @OnClick(R.id.undate_tv)
    public void onClick(View v){
        switch (v.getId()){
            //返回
            case R.id.head_img_left:
                finish();
                break;
            //更新数据
            case R.id.undate_tv:
                break;
        }
    }
}
