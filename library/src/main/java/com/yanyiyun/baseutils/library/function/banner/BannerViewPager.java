package com.yanyiyun.baseutils.library.function.banner;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.reflect.Field;

/**
 * Created by Voctex on 2016/10/26.
 * 适用于banner广告的viewpager
 */
public class BannerViewPager extends ViewPager {

    public BannerViewPager(Context context) {
        super(context);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 默认是打开的，可以滑动
     */
    private boolean canScroll = true;

    // 设置viewpager是否可以滑动
    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        /**
         * 坑，解决在RecyclerView中使用的bug
         * 设ViewPager中有3张照片
         * 当ViewPager滑动一遍之后，向下滑动RecyclerView列表
         * 直到完全隐藏此ViewPager，并执行了onDetachedFromWindow
         * 再回来时，将会出现bug，第一次滑动时没有动画效果，并且，经常出现view没有加载的情况
         */
        try {
            Field mFirstLayout = ViewPager.class.getDeclaredField("mFirstLayout");
            mFirstLayout.setAccessible(true);
            mFirstLayout.set(this, false);
            getAdapter().notifyDataSetChanged();
            setCurrentItem(getCurrentItem());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
            /* return false;//super.onTouchEvent(arg0); */
        if (!canScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (!canScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }

}
