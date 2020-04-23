package com.yanyiyun.function.bottomnavigation.navigationtab;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yanyiyun.function.bottomnavigation.badgeitem.BadgeItem;
import com.yanyiyun.function.bottomnavigation.badgeitem.BadgeTextView;

/**
 * Class description
 *
 * @author ashokvarma
 * @version 1.0
 * @see FrameLayout
 * @since 19 Mar 2016
 */
public abstract class BottomNavigationTab extends FrameLayout {

    /**
     * 是否是没有标题模式
     */
    protected boolean isNoTitleMode;

    /**
     * 选中状态下paddingTop
     */
    protected int paddingTopActive;
    /**
     * 未选中状态下paddingTop
     */
    protected int paddingTopInActive;

    /**
     * tab 所在的位置
     */
    protected int mPosition;
    /**
     * 选中状态的颜色
     */
    protected int mActiveColor;
    /**
     * 未选中状态的颜色
     */
    protected int mInActiveColor;
    /**
     * 背景颜色
     */
    protected int mBackgroundColor;
    /**
     * 选中状态下的宽度
     */
    protected int mActiveWidth;
    /**
     * 未选中状态下的宽度
     */
    protected int mInActiveWidth;

    /**
     * 选中状态下的图标
     */
    protected Drawable mCompactIcon;
    /**
     * 未选择状态下的图标
     */
    protected Drawable mCompactInActiveIcon;
    /**
     * 是否设置 在未选择状态下的图标
     */
    protected boolean isInActiveIconSet = false;
    /**
     * 下方显示的文本
     */
    protected String mLabel;

    public BadgeItem badgeItem;

    /**
     * 是否选中
     */
    boolean isActive = false;

    /**
     * 存储整个tab的容器
     */
    View containerView;
    /**
     * 文本控件
     */
    TextView labelView;
    /**
     * 图标控件
     */
    ImageView iconView;
    /**
     * 存储 icon 和badge的容器
     */
    FrameLayout iconContainerView;
    public BadgeTextView badgeView;

    public BottomNavigationTab(Context context) {
        this(context, null);
    }

    public BottomNavigationTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomNavigationTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public BottomNavigationTab(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    void init() {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setIsNoTitleMode(boolean isNoTitleMode) {
        this.isNoTitleMode = isNoTitleMode;
    }

    public boolean getIsNoTitleMode() {
        return isNoTitleMode;
    }

    public void setActiveWidth(int activeWidth) {
        mActiveWidth = activeWidth;
    }

    public void setInactiveWidth(int inactiveWidth) {
        mInActiveWidth = inactiveWidth;
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = mInActiveWidth;
        setLayoutParams(params);
    }

    public void setIcon(Drawable icon) {
        mCompactIcon = DrawableCompat.wrap(icon);
    }

    public void setInactiveIcon(Drawable icon) {
        mCompactInActiveIcon = DrawableCompat.wrap(icon);
        isInActiveIconSet = true;
    }

    public void setLabel(String label) {
        mLabel = label;
        labelView.setText(label);
    }

    public void setActiveColor(int activeColor) {
        mActiveColor = activeColor;
    }

    public int getActiveColor() {
        return mActiveColor;
    }

    public void setInactiveColor(int inActiveColor) {
        mInActiveColor = inActiveColor;
        labelView.setTextColor(inActiveColor);
    }

    public void setItemBackgroundColor(int backgroundColor) {
        mBackgroundColor = backgroundColor;
    }

    public void setPosition(int position) {
        mPosition = position;
    }

    public void setBadgeItem(BadgeItem badgeItem) {
        this.badgeItem = badgeItem;
    }

    public int getPosition() {
        return mPosition;
    }

    public void select(boolean setActiveColor, int animationDuration) {
        isActive = true;

        //根据不同状态padding top的不同值 的向上或者向下移动的动画
        ValueAnimator animator = ValueAnimator.ofInt(containerView.getPaddingTop(), paddingTopActive);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                containerView.setPadding(containerView.getPaddingLeft(),
                        (Integer) valueAnimator.getAnimatedValue(),
                        containerView.getPaddingRight(),
                        containerView.getPaddingBottom());
            }
        });
        animator.setDuration(animationDuration);
        animator.start();

        iconView.setSelected(true);
        if (setActiveColor) {
            labelView.setTextColor(mActiveColor);
        } else {
            labelView.setTextColor(mBackgroundColor);
        }

        if (badgeItem != null) {
            badgeItem.select();
        }
    }

    public void unSelect(boolean setActiveColor, int animationDuration) {
        isActive = false;

        ValueAnimator animator = ValueAnimator.ofInt(containerView.getPaddingTop(), paddingTopInActive);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                containerView.setPadding(containerView.getPaddingLeft(),
                        (Integer) valueAnimator.getAnimatedValue(),
                        containerView.getPaddingRight(),
                        containerView.getPaddingBottom());
            }
        });
        animator.setDuration(animationDuration);
        animator.start();

        labelView.setTextColor(mInActiveColor);
        iconView.setSelected(false);

        if (badgeItem != null) {
            badgeItem.unSelect();
        }
    }

    @CallSuper
    public void initialise(boolean setActiveColor) {
        iconView.setSelected(false);
        //有设置未选中状态下的图标，切换图标
        if (isInActiveIconSet) {
            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_selected},
                    mCompactIcon);
            states.addState(new int[]{-android.R.attr.state_selected},
                    mCompactInActiveIcon);
            states.addState(new int[]{},
                    mCompactInActiveIcon);
            iconView.setImageDrawable(states);
            //未设置未选中状态下的图标，变化图标颜色
        } else {
            if (setActiveColor) {  //有设置选中状态下的颜色
                DrawableCompat.setTintList(mCompactIcon, new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_selected}, //1
                                new int[]{-android.R.attr.state_selected}, //2
                                new int[]{}
                        },
                        new int[]{
                                mActiveColor, //1
                                mInActiveColor, //2
                                mInActiveColor //3
                        }
                ));
            } else {  //未设置选中状态下的颜色，设置为背景颜色
                DrawableCompat.setTintList(mCompactIcon, new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_selected}, //1
                                new int[]{-android.R.attr.state_selected}, //2
                                new int[]{}
                        },
                        new int[]{
                                mBackgroundColor, //1
                                mInActiveColor, //2
                                mInActiveColor //3
                        }
                ));
            }
            iconView.setImageDrawable(mCompactIcon);
        }

        if (isNoTitleMode) {
            labelView.setVisibility(GONE);

            LayoutParams layoutParams = (LayoutParams) iconContainerView.getLayoutParams();
            layoutParams.gravity = Gravity.CENTER;
            setNoTitleIconContainerParams(layoutParams);
            iconContainerView.setLayoutParams(layoutParams);

            LayoutParams iconLayoutParams = (LayoutParams) iconView.getLayoutParams();
            setNoTitleIconParams(iconLayoutParams);
            iconView.setLayoutParams(iconLayoutParams);
        }
    }

    protected abstract void setNoTitleIconContainerParams(LayoutParams layoutParams);

    protected abstract void setNoTitleIconParams(LayoutParams layoutParams);
}
