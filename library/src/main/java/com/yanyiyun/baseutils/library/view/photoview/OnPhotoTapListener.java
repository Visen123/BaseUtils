package com.yanyiyun.baseutils.library.view.photoview;

import android.widget.ImageView;

/**
 * 在图片内点击
 */
public interface OnPhotoTapListener {
    /**
     *
     * @param view
     * @param x  点击点的X轴坐标占图片宽度的百分比
     * @param y  点击点的Y轴坐标占图片高度的百分比
     */
    void onPhotoTap(ImageView view,float x,float y);
}
