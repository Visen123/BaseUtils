package com.yanyiyun.baseutils.library.function.network;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/6/13 0013.
 */

public abstract class MySubscriber<T> extends Subscriber<T> {

    public abstract void succeed(T t);

    public abstract void failed(Result result);

    public abstract void error(Throwable e);
    public abstract void onCompleted();

    private OnCodeBeanCallBack onCodeBeanCallBack;

    public MySubscriber setOnCodeBeanCallBack(OnCodeBeanCallBack onCodeBeanCallBack) {
        this.onCodeBeanCallBack = onCodeBeanCallBack;
        return this;
    }

    @Override
    public void onNext(T t) {
        if (null != t) {
            if (t instanceof CodeBean) {
                if (onCodeBeanCallBack != null) {
                    onCodeBeanCallBack.onReceive((CodeBean) t);
                }
                failed(((CodeBean) t).getResult());
            } else {
               succeed(t);
            }
        } else {
            failed(new Result("获取数据失败",0));
        }
        onCompleted();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        error(e);
    }

    public interface OnCodeBeanCallBack {

        void onReceive(CodeBean codeBean);

    }

}
