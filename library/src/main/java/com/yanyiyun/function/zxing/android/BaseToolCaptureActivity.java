package com.yanyiyun.function.zxing.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.R;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.function.selectImage.AndSelectImage;
import com.yanyiyun.function.zxing.ICaptureView;
import com.yanyiyun.function.zxing.common.Constant;
import com.yanyiyun.function.zxing.view.ViewfinderView;


/**
 *
 *
 * 扫一扫
 */

public class BaseToolCaptureActivity extends BaseToolBaseCaptureActivity implements SurfaceHolder.Callback,ICaptureView,View.OnClickListener {

    private TextView head_text_title,open_or_close,right_tv;
    private SurfaceView previewView;
    private ViewfinderView viewfinderView;

    private Context mContext;


    public static Intent createIntent(Context context){
        Intent intent=new Intent(context,BaseToolCaptureActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.basetool_activity_scanner);
        mContext=this;
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        initView();
    }

    private void initView() {
        previewView = findViewById(R.id.preview_view);

        viewfinderView = findViewById(R.id.viewfinder_content);
        viewfinderView.setZxingConfig(config);


        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("");

        right_tv=findViewById(R.id.right_tv);
        right_tv.setOnClickListener(this);
        right_tv.setVisibility(View.VISIBLE);
        right_tv.setText("相册");

        findViewById(R.id.open_sdt).setOnClickListener(this);

        open_or_close = findViewById(R.id.open_or_close);
    }


    /**
     * @param flashState 切换闪光灯图片
     */
    @Override
    public void switchFlashImg(int flashState) {

        if (flashState == Constant.FLASH_OPEN) {
//            flashLight_iv.setImageResource(R.drawable.ic_open);
            open_or_close.setText("关闭闪光灯");
        } else {
//            flashLight_iv.setImageResource(R.drawable.ic_close);
            open_or_close.setText("打开闪光灯");
        }

    }


    @Override
    protected void onResume() {
        super.onResume();

        viewfinderView.setCameraManager(cameraManager);
        surfaceHolder = previewView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder,this);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!hasSurface) {
            surfaceHolder.removeCallback(this);
        }

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder,this);
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    @Override
    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }



    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.head_img_left){
            finish();
        }else if(view.getId()==R.id.open_sdt){
            cameraManager.switchFlashLight(handler);
        }else if(view.getId()==R.id.right_tv){  //选择图片
            new AndSelectImage().withActivity(this)
                    .withNumber(1)
                    .withRequestCode(SELECT_IMAGE)
                    .withType(AndSelectImage.TYPE_IMAGE)
                    .withColumnNumber(4)
                    .withTakePhoto(false)
                    .start();
        }
    }


}
