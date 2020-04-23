package com.yanyiyun.baseutils.library.function.banner;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.yanyiyun.baseutils.library.R;

/**
 * Created by voctex on 20160707
 * 点的view
 */
public class BannerPoint extends View {

    private Paint mPaint;
    private int vWidth;
    private int vHeight;
    private int radius;
    private int mLightColor = ContextCompat.getColor(getContext(), R.color.banner_theme);
    private int mDimColor = ContextCompat.getColor(getContext(), R.color.banner_gery_dark);

    private boolean isSelect = false;

    public BannerPoint(Context context) {
        this(context, null);
        initView();
    }

    public BannerPoint(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initView();
    }

    public BannerPoint(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(21)
    public BannerPoint(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {

        mPaint = new Paint();
        mPaint.setColor(mLightColor);
        mPaint.setAntiAlias(true);//true为抗锯齿
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isSelect) {
            mPaint.setColor(mLightColor);
            mPaint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(vWidth / 2, vHeight / 2, radius, mPaint);
        } else {
            mPaint.setColor(mDimColor);
            mPaint.setStyle(Paint.Style.STROKE);
            canvas.drawCircle(vWidth / 2, vHeight / 2, radius, mPaint);
        }

    }

    /**
     * 设置点是否在当前位置
     */
    void setSelect(boolean select) {
        isSelect = select;
        invalidate();
    }

    /**
     * 设置点是否在当前位置，并根据点的半径来生成
     */
    void setPoint(boolean select, int radius) {
        this.isSelect = select;
        this.radius = radius;
        invalidate();
    }


    /**
     * 设置点的颜色
     */
    void setPointColor(int lightColor, int dimColor) {
        this.mLightColor = lightColor;
        this.mDimColor = dimColor;
        invalidate();
    }

    /**
     * 重写onMeasure，解决在wrap_content下与match_parent效果一样的问题
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
        vWidth = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
        vHeight = getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec);
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
