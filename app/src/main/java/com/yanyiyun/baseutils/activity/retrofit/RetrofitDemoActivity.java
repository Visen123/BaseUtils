package com.yanyiyun.baseutils.activity.retrofit;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RetrofitDemoActivity extends BaseActivity {

    @BindView(R.id.head_text_title)
    TextView head_text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.retrofit_demo_activity);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        head_text_title.setText("Retrofit");
    }

    @OnClick({R.id.head_img_left,R.id.upload_tv})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
                //上传文件
            case R.id.upload_tv:
                break;
        }
    }
}
