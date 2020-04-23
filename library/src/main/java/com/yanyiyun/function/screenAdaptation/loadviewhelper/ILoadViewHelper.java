package com.yanyiyun.function.screenAdaptation.loadviewhelper;

import android.view.View;


public interface ILoadViewHelper {
    /**
     * 设置视图的高宽
     *
     * @param view
     */
    void loadWidthHeightFont(View view);

    /**
     * 设置视图的Padding
     *
     * @param view
     */
    void loadPadding(View view);

    /**
     * 设置视频的Margin
     * @param view
     */
    void loadLayoutMargin(View view);

    /**
     * 设置视图的最大高度和宽度
     * @param view
     */
    void loadMaxWidthAndHeight(View view);

    /**
     * 设置视图的最小高度和宽度
     * @param view
     */
    void loadMinWidthAndHeight(View view);

    /**
     * 加载自定义属性
     * @param px
     * @return
     */
    int loadCustomAttrValue(int px);
}
