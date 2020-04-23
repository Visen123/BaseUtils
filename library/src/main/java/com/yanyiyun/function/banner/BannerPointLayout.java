package com.yanyiyun.function.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by Voctex on 2016/8/27.
 * 点的视图组
 */
public class BannerPointLayout extends LinearLayout {
    public BannerPointLayout(Context context) {
        super(context);
        initView();
    }

    public BannerPointLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BannerPointLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)
    public BannerPointLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private int pointCount = 0;
    private int beforeIndex;
    private int currentIndex;

    private int pointRadius = 3;
    private int pointViewWidth = 12;

    private void initView() {

    }

    /**
     * 根据给的半径和内边距来生成点的view
     */
    void setPointSize(int pointRadius, int pointPadding) {
        this.pointRadius = pointRadius;
        this.pointViewWidth = pointRadius * 2 + pointPadding * 2;
        if (getChildCount() > 0) {
            for (int i = 0; i < this.pointCount; i++) {
                BannerPoint bannerPoint = (BannerPoint) getChildAt(i);
                LayoutParams params = (LayoutParams) bannerPoint.getLayoutParams();
                params.width = dip2px(pointViewWidth);
                params.height = dip2px(pointViewWidth);
                bannerPoint.setLayoutParams(params);
                bannerPoint.setPoint(false, dip2px(pointRadius));

            }
        }
    }

    /**
     * 根据数量来生成点的数量
     */
    void setPointCount(int pointCount) {
        if (pointCount > 0) {
            this.pointCount = pointCount;
            this.removeAllViews();
            for (int i = 0; i < pointCount; i++) {
                BannerPoint point = new BannerPoint(getContext());
                LayoutParams params = new LayoutParams(dip2px(pointViewWidth), dip2px(pointViewWidth));
                point.setLayoutParams(params);
                point.setPoint(false, dip2px(pointRadius));
                this.addView(point);
            }
        }

        if(getChildCount()<=1){
            setVisibility(INVISIBLE);
        }else {
            setVisibility(VISIBLE);
        }
    }

    /**
     * 设置当前位置的视图和点的方法
     */
    void setPosition(int position) {
        BannerPoint beforePoint = (BannerPoint) getChildAt(beforeIndex);
        beforePoint.setSelect(false);
        this.beforeIndex = position;
        BannerPoint point = (BannerPoint) getChildAt(position);
        point.setSelect(true);
    }

    /**
     * 设置点的颜色
     */
    void setPointColor(int lightColor, int dimColor) {
        for (int i = 0; i < pointCount; i++) {
            BannerPoint point = (BannerPoint) getChildAt(i);
            point.setPointColor(lightColor, dimColor);
        }
    }


    /**
     * 将dip或dp值转换为px值，保证尺寸大小不变
     *
     * @param dipValue scale
     *                 （DisplayMetrics类中属性density）
     * @return
     */
    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
