package com.yanyiyun.baseutils.library.function.zxing;

import android.app.Activity;
import android.os.Handler;

import com.yanyiyun.baseutils.library.function.zxing.bean.ZxingConfig;
import com.yanyiyun.baseutils.library.function.zxing.camera.CameraManager;
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
