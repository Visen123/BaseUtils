package com.yanyiyun.function.banner.interfac;

import android.widget.ImageView;

/**
 * Created by mac_xihao on 17/5/5.
 * 图片加载的回调，把数据回调到上层，让上层自己选择用什么图片加载框架加载图片
 */
public interface OnBannerImgShowListener {

    void onBannerShow(String url, ImageView imgView);
}
