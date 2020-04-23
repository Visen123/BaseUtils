package com.yanyiyun.function.dialog.product;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

/**
 * 有确定取消两个按钮的对话框  中间的提示内容为文本
 */
public class ConfirmAndCancleTextContentDialog extends ConfirmAndCancleDialog {

    /**
     * 对话框中间内容
     */
    public TextView content_tv;

    public ConfirmAndCancleTextContentDialog(@NonNull Context context) {
        super(context);
        content_tv=new TextView(context);
        content_tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,13);
        content_tv.setGravity(Gravity.LEFT);
        content_tv.setTextColor(Color.parseColor("#333333"));
        setContentView(content_tv);
    }
}
