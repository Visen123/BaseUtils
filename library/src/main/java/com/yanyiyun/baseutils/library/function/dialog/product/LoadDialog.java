package com.yanyiyun.baseutils.library.function.dialog.product;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yanyiyun.baseutils.library.R;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;

public class LoadDialog extends Dialog {

    public View view;
    private ProgressBar progress_bar;
    private TextView hint_tv;

    public LoadDialog(@NonNull Context context) {
        super(context);
        view=LayoutInflater.from(context).inflate(R.layout.load_dialog_basetool,null);
        ScreenAdapterTools.getInstance().loadView(view);
        setCanceledOnTouchOutside(false);
        Window window = getWindow();
        window.setContentView(view);
        window.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        initview();
    }

    private void initview() {
        progress_bar=view.findViewById(R.id.progress_bar);
        hint_tv=view.findViewById(R.id.hint_tv);
    }

    /**
     * 设置加载框颜色
     *
     * @param color
     */
    public void setProgressBarColor(int color) {
        ClipDrawable d = new ClipDrawable(new ColorDrawable(color), Gravity.LEFT, ClipDrawable.HORIZONTAL);
        progress_bar.setProgressDrawable(d);
    }

    public void setHint(int hint) {
        hint_tv.setText(hint);
    }

    public void setHint(String hint) {
        hint_tv.setText(hint);
    }

    public void setHintColor(int color) {
        hint_tv.setTextColor(color);
    }
}
