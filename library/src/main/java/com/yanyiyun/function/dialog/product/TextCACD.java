package com.yanyiyun.function.dialog.product;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;

import com.yanyiyun.R;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;

/**
 * 中间是文本的确定取消对话框
 */
public class TextCACD extends ConfirmAndCancleDialog {

    /**
     * 对话框文本内容
     */
    public TextView content_tv;

    public TextCACD(@NonNull Context context) {
        super(context);
        content_tv=new TextView(context);
        content_tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,22);
        content_tv.setGravity(Gravity.LEFT);
        content_tv.setTextColor(ContextCompat.getColor(context, R.color.tab_text_unselect));
        content_ll.addView(content_tv);
        ScreenAdapterTools.getInstance().loadView(view);
    }
}
