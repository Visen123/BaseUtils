package com.yanyiyun.baseutils.library.function.permission.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.yanyiyun.baseutils.library.R;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;

/**
 * 权限提示对话框
 */
public class PermissionHintDialog extends Dialog {

    private View view;
    private TextView title_tv,hint_tv,cancle_tv,confirm_tv;

    public PermissionHintDialog(@NonNull Context context) {
        super(context);
        view=LayoutInflater.from(context).inflate(R.layout.permission_hint_dialog,null);
        ScreenAdapterTools.getInstance().loadView(view);
        initview();
    }

    private void initview() {
        title_tv=view.findViewById(R.id.title_tv);
        hint_tv=view.findViewById(R.id.hint_tv);
        cancle_tv=view.findViewById(R.id.cancle_tv);
        confirm_tv=view.findViewById(R.id.confirm_tv);

        Window window=getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    /**
     * 设置标题
     * @param title
     * @return
     */
    public PermissionHintDialog setTitle(String title){
        title_tv.setText(title);
        return this;
    }

    /**
     * 设置提示内容
     * @param content
     * @return
     */
    public PermissionHintDialog setContent(String content){
        hint_tv.setText(content);
        return this;
    }

    /**
     * 取消按钮
     * @param content
     * @param listener
     * @return
     */
    public PermissionHintDialog setNegativeButton(String content, View.OnClickListener listener){
        cancle_tv.setText(content);
        cancle_tv.setOnClickListener(listener);
        return this;
    }

//    public PermissionHintDialog setNegativeButton(String content){
//        cancle_tv.setText(content);
//        return this;
//    }
//
//    public PermissionHintDialog setNegativeButton(View.OnClickListener listener){
//        cancle_tv.setOnClickListener(listener);
//        return this;
//    }

    /**
     * 确定按钮
     * @param content
     * @param listener
     * @return
     */
    public PermissionHintDialog setPositiveButton(String content, View.OnClickListener listener){
        confirm_tv.setText(content);
        confirm_tv.setOnClickListener(listener);
        return this;
    }

//    public PermissionHintDialog setPositiveButton(String content){
//        confirm_tv.setText(content);
//        return this;
//    }
//
//    public PermissionHintDialog setPositiveButton(View.OnClickListener listener){
//        confirm_tv.setOnClickListener(listener);
//        return this;
//    }

}
