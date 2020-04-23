package com.yanyiyun.baseutils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.baseutils.activity.CreditCardActivity;
import com.yanyiyun.baseutils.activity.DialogFactoryActivity;
import com.yanyiyun.baseutils.activity.SelectImageAndActivity;
import com.yanyiyun.baseutils.activity.StepDownActivity;
import com.yanyiyun.baseutils.activity.okhttp.OkHttpDemoActivity;
import com.yanyiyun.baseutils.activity.okio.OkioDemoActivity;
import com.yanyiyun.baseutils.activity.selfview.SelfViewActivity;
import com.yanyiyun.baseutils.adapter.MainAdapter;
import com.yanyiyun.baseutils.entity.MainItem;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.baseutils.library.function.zxing.android.BaseToolCaptureActivity;
import com.yanyiyun.baseutils.library.function.zxing.common.Constant;
import com.yanyiyun.baseutils.library.tool.NotificationsUtils;
import com.yanyiyun.baseutils.library.tool.ScreenUitl;
import com.yanyiyun.baseutils.library.tool.filter.inputFilter.DecimalNumberInputFilter;
import com.yanyiyun.baseutils.library.tool.log;
import com.yanyiyun.baseutils.util.FilePicker;

import java.util.ArrayList;

public class MainActivity extends BaseActivity  {

    private ListView list_lv;
    private ImageView head_img_left;
    private TextView head_text_title;

    private Context mContext;
    private ArrayList<MainItem> data=new ArrayList<>();
    private MainAdapter adapter;
    private final int QR_CODE=11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ScreenUitl.setHead(this,findViewById(R.id.head_top), R.color.head_translucent,
                ScreenUitl.StatusBarStyle.COLOR);
        mContext=this;
        initData();
        initview();


        findViewById(R.id.scan_b).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(BaseToolCaptureActivity.createIntent(mContext),QR_CODE);
            }
        });

        EditText input_et=findViewById(R.id.input_et);
        input_et.setFilters(new InputFilter[]{new DecimalNumberInputFilter(8)});


        if(!NotificationsUtils.isNotificationEnabled(mContext)){
            NotificationsUtils.toNotificationSetting(this);
        }
    }


    private void initview() {

        head_img_left=findViewById(R.id.head_img_left);
        head_img_left.setVisibility(View.GONE);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("Demo");

        list_lv=  findViewById(R.id.list_lv);
        adapter=new MainAdapter(mContext,data);
        list_lv.setAdapter(adapter);
        list_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                click(position);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK){
            if(requestCode==QR_CODE){
                String scanResult = data.getStringExtra(Constant.CODED_CONTENT);
                log.e("scanResult:"+scanResult);
            }
        }

        if(requestCode==NotificationsUtils.GET_PER){
            log.e("获取权限回调");
        }
    }

    private void initData() {
        MainItem selfview=new MainItem("自定义View","");
        data.add(selfview);
        MainItem credit=new MainItem("CreditCard","信用卡账单");
        data.add(credit);
        MainItem fileS=new MainItem("文件系统","文件系统实例");
        data.add(fileS);
        MainItem selectImage=new MainItem("选择本地图片","选择本地图片实例");
        data.add(selectImage);
        MainItem dialogFactory=new MainItem("DialogFactory","对话框工厂");
        data.add(dialogFactory);
        MainItem stepDown=new MainItem("StepService","本地记步");
        data.add(stepDown);
        MainItem okhttp=new MainItem("OkHttp","");
        data.add(okhttp);
        MainItem okio=new MainItem("okio","");
        data.add(okio);
    }

    private void click(int position){
        switch (position){
            //自定义View
            case 0:
                startIntent(mContext,SelfViewActivity.class);
                break;
                //信用卡账单
            case 1:
                startIntent(mContext,CreditCardActivity.class);
                break;
                //文件系统
            case 2:
                new FilePicker().withActivity(this)
                        .withRequestCode(0)
                        .withHiddenFiles(true)
                        .withTitle("Sample title")
                        .start();
                break;
                //选择本地图片
            case 3:
                startIntent(mContext,SelectImageAndActivity.class);
                break;
                //对话框工厂
            case 4:
                startIntent(mContext,DialogFactoryActivity.class);
                break;
                //本地记步
            case 5:
                startIntent(mContext,StepDownActivity.class);
                break;
                //okhttp
            case 6:
                startIntent(mContext, OkHttpDemoActivity.class);
                break;
                //
            case 7:
                startIntent(mContext, OkioDemoActivity.class);
                break;
        }
    }


}
