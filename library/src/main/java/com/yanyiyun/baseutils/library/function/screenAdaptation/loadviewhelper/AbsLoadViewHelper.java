package com.yanyiyun.baseutils.library.function.screenAdaptation.loadviewhelper;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.yanyiyun.baseutils.library.function.screenAdaptation.conversion.IConversion;
import com.yanyiyun.baseutils.library.function.screenAdaptation.conversion.SimpleConversion;
import com.yanyiyun.baseutils.library.tool.ScreenUitl;


public abstract class AbsLoadViewHelper implements ILoadViewHelper{

    /**
     * 真实的像素密度
     */
    protected float actualDensity;
    /**
     * 真实的dpi
     */
    protected float actualDensityDpi;
    /**
     * 真实屏幕宽度
     */
    protected float actualWidth;
    /**
     * 真实屏幕高度
     */
    protected float actualHeight;


    /**
     * 设计的屏幕高度
     */
    protected int designWidth;
    /**
     * 设计的dpi
     */
    protected int designDpi;
    /**
     * 全局字体的大小倍数
     */
    protected float fontSize;
    /**
     * 单位 px dp sp
     */
    protected String unit;

    public AbsLoadViewHelper(Context context, int designWidth, int designDpi, float fontSize, String unit) {
        this.designWidth = designWidth;
        this.designDpi = designDpi;
        this.fontSize = fontSize;
        this.unit = unit;
        setActualParams(context);
    }

    /**
     * 重新设置实际参数
     * @param context
     */
    public void reset(Context context) {
        setActualParams(context);
    }

    /**
     * 设置实际参数
     * @param context
     */
    private void setActualParams(Context context) {
        actualWidth = ScreenUitl.getScreenWidth(context);
        actualHeight = ScreenUitl.getScreenHeight(context);
        actualDensity = ScreenUitl.getDensity(context);
        actualDensityDpi = ScreenUitl.getDensityDpi(context);
    }

    // if subclass has owner conversion，you need override this method and provide your conversion
    public void loadView(View view) {
        loadView(view, new SimpleConversion());
    }

    public final void loadView(View view, IConversion conversion) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            conversion.transform(viewGroup, this);
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    loadView(viewGroup.getChildAt(i), conversion);
                } else {
                    conversion.transform(viewGroup.getChildAt(i), this);
                }
            }
        } else {
            conversion.transform(view, this);
        }

    }

    public  abstract  float calculateValue(float value);
}
