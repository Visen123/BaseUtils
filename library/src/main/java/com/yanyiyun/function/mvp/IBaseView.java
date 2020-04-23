package com.yanyiyun.function.mvp;

import android.app.Activity;
import android.content.Context;

public interface IBaseView {
    /**
     * 显示正在加载的进度框
     */
    void showLoading();

    /**
     * 隐藏正在加载进度框
     */
    void hideLoading();

    /**
     * 显示提示
     *
     * @param msg
     */
    void showToast(String msg);

    /**
     * 显示请求错误提示
     */
    void showErr(String msg);

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    Context getContext();

    Activity getAcitvity();
}
