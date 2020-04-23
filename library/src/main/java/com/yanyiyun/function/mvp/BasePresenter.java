package com.yanyiyun.function.mvp;


import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

public abstract class BasePresenter<V extends IBaseView> {

    protected Reference<V> viewRef;

    public void attachView(V view) {
        //持有的是Activity的软引用，
        viewRef = new WeakReference<V>(view);
    }

    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }


    /**
     * 是否与View建立连接
     * 每次调用业务请求的时候都要出先调用方法检查是否与View建立连接
     */
    public boolean isViewAttached() {
        return viewRef.get() != null;
    }

    /**
     * 获取连接的view
     */
    public V getView() {
        return viewRef.get();
    }

}
