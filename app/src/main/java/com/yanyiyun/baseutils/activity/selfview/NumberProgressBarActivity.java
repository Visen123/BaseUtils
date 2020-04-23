package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.view.NumberProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class NumberProgressBarActivity extends BaseActivity implements NumberProgressBar.OnProgressBarListener,View.OnClickListener{
    private Context mContext;

    private NumberProgressBar progress_npb;
    private TextView head_text_title;
    private Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.number_progress_bar_activity);
        mContext=this;
        initview();
    }

    private void initview() {

        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("NumberProgressBar");

        progress_npb=findViewById(R.id.progress_npb);
        progress_npb.setOnProgressBarListener(this);

        timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progress_npb.incrementProgressBy(1);
                    }
                });
            }
        },1000,100);
    }

    @Override
    public void onProgressChange(int current, int max) {
        if(current==max){
            progress_npb.setProgress(0);
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
