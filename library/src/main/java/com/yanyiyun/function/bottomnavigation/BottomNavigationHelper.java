package com.yanyiyun.function.bottomnavigation;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.yanyiyun.R;
import com.yanyiyun.function.bottomnavigation.badgeitem.BadgeItem;
import com.yanyiyun.function.bottomnavigation.navigationtab.BottomNavigationTab;

/**
 * Class description : This is utils class specific for this library, most the common code goes here.
 *
 * @author ashokvarma
 * @version 1.0
 * @since 19 Mar 2016
 */
class BottomNavigationHelper {

    private BottomNavigationHelper() {
    }

    /**
     * MODE_FIXED模式下获得tab的宽度
     *
     * @param context     to fetch measurements
     * @param screenWidth total screen width  屏幕宽度
     * @param noOfTabs    no of bottom bar tabs  tab的数量
     * @param scrollable  is bottom bar scrollable  是否滑动
     * @return width of each tab 一个长度为2的数组 第一个值为tab的宽度
     */
    static int[] getMeasurementsForFixedMode(Context context, int screenWidth, int noOfTabs, boolean scrollable) {

        int[] result = new int[2];

        int minWidth = (int) context.getResources().getDimension(R.dimen.fixed_min_width_small_views);
        int maxWidth = (int) context.getResources().getDimension(R.dimen.fixed_min_width);

        int itemWidth = screenWidth / noOfTabs;

        if (itemWidth < minWidth && scrollable) {
            itemWidth = (int) context.getResources().getDimension(R.dimen.fixed_min_width);
        } else if (itemWidth > maxWidth) {
            itemWidth = maxWidth;
        }

        result[0] = itemWidth;

        return result;
    }

    /**
     * MODE_SHIFTING模式下获得tab的宽度
     *
     * @param context     to fetch measurements
     * @param screenWidth 屏幕宽度
     * @param noOfTabs    tab的数量
     * @param scrollable  是否滑动
     * @return min and max width of each tab
     */
    static int[] getMeasurementsForShiftingMode(Context context, int screenWidth, int noOfTabs, boolean scrollable) {

        int[] result = new int[2];

        int minWidth = (int) context.getResources().getDimension(R.dimen.shifting_min_width_inactive);
        int maxWidth = (int) context.getResources().getDimension(R.dimen.shifting_max_width_inactive);

        double minPossibleWidth = minWidth * (noOfTabs + 0.5);
        double maxPossibleWidth = maxWidth * (noOfTabs + 0.75);
        int itemWidth;
        int itemActiveWidth;

        if (screenWidth < minPossibleWidth) {
            if (scrollable) {
                itemWidth = minWidth;
                itemActiveWidth = (int) (minWidth * 1.5);
            } else {
                itemWidth = (int) (screenWidth / (noOfTabs + 0.5));
                itemActiveWidth = (int) (itemWidth * 1.5);
            }
        } else if (screenWidth > maxPossibleWidth) {
            itemWidth = maxWidth;
            itemActiveWidth = (int) (itemWidth * 1.75);
        } else {
            double minPossibleWidth1 = minWidth * (noOfTabs + 0.625);
            double minPossibleWidth2 = minWidth * (noOfTabs + 0.75);
            itemWidth = (int) (screenWidth / (noOfTabs + 0.5));
            itemActiveWidth = (int) (itemWidth * 1.5);
            if (screenWidth > minPossibleWidth1) {
                itemWidth = (int) (screenWidth / (noOfTabs + 0.625));
                itemActiveWidth = (int) (itemWidth * 1.625);
                if (screenWidth > minPossibleWidth2) {
                    itemWidth = (int) (screenWidth / (noOfTabs + 0.75));
                    itemActiveWidth = (int) (itemWidth * 1.75);
                }
            }
        }

        result[0] = itemWidth;
        result[1] = itemActiveWidth;

        return result;
    }

    /**
     * 把bottomNavigationItem和BottomNavigationTab结合
     *
     * @param bottomNavigationItem holds all the data
     * @param bottomNavigationTab  view to which data need to be set
     * @param bottomNavigationBar  view which holds all the tabs
     */
    static void bindTabWithData(BottomNavigationItem bottomNavigationItem, BottomNavigationTab bottomNavigationTab, BottomNavigationBar bottomNavigationBar) {

        Context context = bottomNavigationBar.getContext();

        bottomNavigationTab.setLabel(bottomNavigationItem.getTitle(context));
        bottomNavigationTab.setIcon(bottomNavigationItem.getIcon(context));

        int activeColor = bottomNavigationItem.getActiveColor(context);
        int inActiveColor = bottomNavigationItem.getInActiveColor(context);

        if (activeColor != Color.TRANSPARENT) {
            bottomNavigationTab.setActiveColor(activeColor);
        } else {
            bottomNavigationTab.setActiveColor(bottomNavigationBar.getActiveColor());
        }

        if (inActiveColor != Color.TRANSPARENT) {
            bottomNavigationTab.setInactiveColor(inActiveColor);
        } else {
            bottomNavigationTab.setInactiveColor(bottomNavigationBar.getInActiveColor());
        }

        if (bottomNavigationItem.isInActiveIconAvailable()) {
            Drawable inactiveDrawable = bottomNavigationItem.getInactiveIcon(context);
            if (inactiveDrawable != null) {
                bottomNavigationTab.setInactiveIcon(inactiveDrawable);
            }
        }

        bottomNavigationTab.setItemBackgroundColor(bottomNavigationBar.getBackgroundColor());

        BadgeItem badgeItem = bottomNavigationItem.getBadgeItem();
        if (badgeItem != null) {
            badgeItem.bindToBottomTab(bottomNavigationTab);
        }
    }

    /**
     * 当tab被选择时的水波纹动画
     *
     * @param clickedView       被选择的tab  view
     * @param backgroundView    存储水波纹视图和tab容器的 父类视图
     * @param bgOverlay         实现水波纹动画的视图
     * @param newColor          水波纹动画的颜色值
     * @param animationDuration 动画持续时间
     */
    static void setBackgroundWithRipple(View clickedView, final View backgroundView,
                                        final View bgOverlay, final int newColor, int animationDuration) {
        int centerX = (int) (clickedView.getX() + (clickedView.getMeasuredWidth() / 2));
        int centerY = clickedView.getMeasuredHeight() / 2;
        int finalRadius = backgroundView.getWidth();

        backgroundView.clearAnimation();
        bgOverlay.clearAnimation();

        Animator circularReveal;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils
                    .createCircularReveal(bgOverlay, centerX, centerY, 0, finalRadius);
        } else {
            bgOverlay.setAlpha(0);
            circularReveal = ObjectAnimator.ofFloat(bgOverlay, "alpha", 0, 1);
        }

        circularReveal.setDuration(animationDuration);
        circularReveal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                onCancel();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                onCancel();
            }

            private void onCancel() {
                backgroundView.setBackgroundColor(newColor);
                bgOverlay.setVisibility(View.GONE);
            }
        });

        bgOverlay.setBackgroundColor(newColor);
        bgOverlay.setVisibility(View.VISIBLE);
        circularReveal.start();
    }
}
