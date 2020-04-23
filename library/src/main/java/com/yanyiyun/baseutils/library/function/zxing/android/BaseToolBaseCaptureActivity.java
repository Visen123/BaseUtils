package com.yanyiyun.baseutils.library.function.zxing.android;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.yanyiyun.baseutils.library.R;
import com.yanyiyun.baseutils.library.function.BaseToolActivity;
import com.yanyiyun.baseutils.library.function.selectImage.AndSelectImage;
import com.yanyiyun.baseutils.library.function.selectImage.ItemEntity;
import com.yanyiyun.baseutils.library.function.zxing.IBaseCaptureView;
import com.yanyiyun.baseutils.library.function.zxing.ICaptureView;
import com.yanyiyun.baseutils.library.function.zxing.bean.ZxingConfig;
import com.yanyiyun.baseutils.library.function.zxing.camera.CameraManager;
import com.yanyiyun.baseutils.library.function.zxing.common.Constant;
import com.yanyiyun.baseutils.library.function.zxing.decode.DecodeImgCallback;
import com.yanyiyun.baseutils.library.function.zxing.decode.DecodeImgThread;
import com.yanyiyun.baseutils.library.function.zxing.decode.ImageUtil;
import com.yanyiyun.baseutils.library.tool.ImageProgressTool;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

public class BaseToolBaseCaptureActivity extends BaseToolActivity implements IBaseCaptureView {

    protected final static int SELECT_IMAGE = 20001;
    protected ZxingConfig config;
    protected static final String TAG = BaseToolCaptureActivity.class.getSimpleName();

    protected boolean hasSurface;
    protected InactivityTimer inactivityTimer;
    protected BeepManager beepManager;
    protected CameraManager cameraManager;
    protected CaptureActivityHandler handler;
    protected SurfaceHolder surfaceHolder;
    private Context mContext;

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        configuration();
        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraManager = new CameraManager(getApplication(), config);
        handler = null;
        beepManager.updatePrefs();
        inactivityTimer.onResume();
    }

    private void init() {
        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(config.isPlayBeep());
        beepManager.setVibrate(config.isShake());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode==RESULT_OK){
            Bundle bundle=intent.getExtras();
            if(requestCode==Constant.REQUEST_IMAGE){
                String path = ImageUtil.getImageAbsolutePath(this, intent.getData());

                new DecodeImgThread(path, new DecodeImgCallback() {
                    @Override
                    public void onImageDecodeSuccess(Result result) {
                        handleDecode(result);
                    }

                    @Override
                    public void onImageDecodeFailed() {
                        Toast.makeText(mContext, "抱歉，解析失败,换个图片试试.", Toast.LENGTH_SHORT).show();
                    }
                }).run();
            }else if(requestCode==SELECT_IMAGE){  //相册选择图片返回
                ArrayList<ItemEntity> list=bundle.getParcelableArrayList(AndSelectImage.SELECT_IMAGE);
                if(list!=null&&list.size()>0){
                    ItemEntity id=list.get(0);
                    Result result=scanningImage(id.getPath());
                    if(result!=null){
                        Intent intent1 = getIntent();
                        intent1.putExtra(Constant.CODED_CONTENT,result.getText());
                        setResult(RESULT_OK, intent1);
                        this.finish();
                    }else {
                        Toast.makeText(mContext,"抱歉，解析失败,换个图片试试.",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    /**
     * 配置信息
     */
    protected void configuration() {
        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(Color.BLACK);
        }

        /*先获取配置信息*/
        try {
            config = (ZxingConfig) getIntent().getExtras().get(Constant.INTENT_ZXING_CONFIG);
        } catch (Exception e) {
        }
        if (config == null) {
            config = new ZxingConfig();
        }
    }

    /**
     * 扫码二维码图片
     *
     * @return
     */
    protected Result scanningImage(String path) {
        if (path == null) {
            return null;
        }

        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");

        Bitmap scanBitmap = ImageProgressTool.getImage(path);
        int[] data = new int[scanBitmap.getWidth() * scanBitmap.getHeight()];
        scanBitmap.getPixels(data, 0, scanBitmap.getWidth(), 0, 0, scanBitmap.getWidth(), scanBitmap.getHeight());
        RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap.getWidth(), scanBitmap.getHeight(), data);
        BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
        QRCodeReader reader = new QRCodeReader();

        try {
            Result result = reader.decode(bitmap1, hints);
//            log.e("result:"+result);
            return result;
        } catch (NotFoundException e) {
            e.printStackTrace();
        } catch (ChecksumException e) {
            e.printStackTrace();
        } catch (FormatException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected void initCamera(SurfaceHolder surfaceHolder, ICaptureView captureView) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(captureView, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("扫一扫");
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    /**
     * @param pm
     * @return 是否有闪光灯
     */
    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public ZxingConfig getConfig() {
        return config;
    }

    /**
     * @param rawResult 返回的扫描结果
     */
    @Override
    public void handleDecode(Result rawResult) {

        inactivityTimer.onActivity();

        beepManager.playBeepSoundAndVibrate();

        Intent intent = getIntent();
        intent.putExtra(Constant.CODED_CONTENT, rawResult.getText());
        setResult(RESULT_OK, intent);
        this.finish();


    }


    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    protected void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();
        super.onPause();
    }


    @Override
    public CameraManager getCameraManager() {
        return cameraManager;
    }


}
