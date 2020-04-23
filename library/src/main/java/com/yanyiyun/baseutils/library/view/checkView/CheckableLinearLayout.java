package com.yanyiyun.baseutils.library.view.checkView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable {

    private Context mContext;
    private boolean mCheck=false;

    /**
     * 加入的android自带的state_checked  和自定义的my_state_checked
     */
    private static final int[] CHECKED_STATE_SET={android.R.attr.state_checked};

    public CheckableLinearLayout(Context context) {
        super(context);
        mContext=context;
    }

    public CheckableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext=context;
    }

    @Override
    public void setChecked(boolean checked) {
        if(checked!=mCheck){
            mCheck=checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mCheck;
    }

    @Override
    public void toggle() {
        mCheck=!mCheck;
        refreshDrawableState();
    }

    /**
     * 获得控件的DrawableState数组
     * @param extraSpace
     * @return
     */
    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        //获取控件原有的drawableState
        final int[] drawableState=super.onCreateDrawableState(extraSpace+CHECKED_STATE_SET.length);
        if(isChecked()){
            //合并原有和自定义添加的drawableState
            mergeDrawableStates(drawableState,CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }
}
