package com.yanyiyun.function.zxing;

import android.app.Activity;
import android.os.Handler;

import com.yanyiyun.function.zxing.bean.ZxingConfig;
import com.yanyiyun.function.zxing.camera.CameraManager;
import com.google.zxing.Result;

public interface IBaseCaptureView {
    ZxingConfig getConfig();
    /**
     * 返回的扫描结果
     * @param rawResult
     */
    void handleDecode(Result rawResult);

    Activity getActivity();
    Handler getHandler();
    CameraManager getCameraManager();
}
