package com.yanyiyun.baseutils.activity.okio;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.baseutils.library.tool.log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class OkioDemoActivity extends BaseActivity {

    @BindView(R.id.head_text_title)
    TextView head_text_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okio_demo_activity);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        head_text_title.setText("okio");
    }

    private void readLines(InputStream inputStream){
        try {
            Source fileSource= Okio.source(inputStream);
            BufferedSource bufferedSource=Okio.buffer(fileSource);
            while (true){
                String line=bufferedSource.readUtf8Line();
                if(line==null){
                    break;
                }
                log.e(line);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入文本
     * @param outputStream
     */
    private void writeText(OutputStream outputStream){
        Sink sink=Okio.sink(outputStream);
        BufferedSink bufferedSink=Okio.buffer(sink);
        try {
            bufferedSink.writeUtf8("我的");
            bufferedSink.writeUtf8("了打卡的看看打卡");
            bufferedSink.writeUtf8("新的");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.head_img_left,R.id.read_text_tv,R.id.write_text_tv})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
                //读取文本
            case R.id.read_text_tv:
                try {
                    readLines(getAssets().open("text"));
                } catch (IOException e) {
                    log.e("文件找不到");
                    e.printStackTrace();
                }
                break;
                //写入文本
            case R.id.write_text_tv:

                break;
        }
    }
}
