package com.yanyiyun.function.screenAdaptation.conversion;

import android.view.View;

import com.yanyiyun.function.screenAdaptation.loadviewhelper.AbsLoadViewHelper;


/**
 * Created by yatoooon on 2018/2/6.
 */

public class CustomConversion implements IConversion {

    @Override
    public void transform(View view, AbsLoadViewHelper loadViewHelper) {
        if (view.getLayoutParams() != null) {
            loadViewHelper.loadWidthHeightFont(view);
            loadViewHelper.loadPadding(view);
            loadViewHelper.loadLayoutMargin(view);
            loadViewHelper.loadMaxWidthAndHeight(view);
            loadViewHelper.loadMinWidthAndHeight(view);
        }
    }

}
