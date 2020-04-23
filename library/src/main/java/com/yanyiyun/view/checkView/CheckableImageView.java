package com.yanyiyun.view.checkView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.Checkable;

public class CheckableImageView extends AppCompatImageView implements Checkable {

    private boolean mChecked;
    private int[] CHECKED_STATE_SET={android.R.attr.state_checked};

    public CheckableImageView(Context context) {
        super(context);
    }

    public CheckableImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setChecked(boolean checked) {
        if(checked!=mChecked){
            mChecked=checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        mChecked=!mChecked;
        refreshDrawableState();
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState=super.onCreateDrawableState(extraSpace+CHECKED_STATE_SET.length);
        if(isChecked()){
            mergeDrawableStates(drawableState,CHECKED_STATE_SET);
        }
        return drawableState;
    }
}
