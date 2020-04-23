package com.yanyiyun.baseutils.library.function.dialog.product;

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
public class PermissionHintDialog extends Dialog implements View.OnClickListener {

    private View view;
    private TextView title_tv,hint_tv,cancle_tv,confirm_tv;

    private ButtonClickListener cancleClick,confirmClick;

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
        cancle_tv.setOnClickListener(this);
        confirm_tv=view.findViewById(R.id.confirm_tv);
        confirm_tv.setOnClickListener(this);

        setCancelable(false);

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
    public PermissionHintDialog setNegativeButton(String content, ButtonClickListener listener){
        cancle_tv.setText(content);
        cancleClick=listener;
        return this;
    }

    public PermissionHintDialog setNegativeButton(String content){
        cancle_tv.setText(content);
        return this;
    }

    public PermissionHintDialog setNegativeButton(ButtonClickListener listener){
        cancleClick=listener;
        return this;
    }

    /**
     * 确定按钮
     * @param content
     * @param listener
     * @return
     */
    public PermissionHintDialog setPositiveButton(String content, ButtonClickListener listener){
        confirm_tv.setText(content);
        confirmClick=listener;
        return this;
    }

    public interface ButtonClickListener{
        public void onClick();
    }

    public PermissionHintDialog setPositiveButton(String content){
        confirm_tv.setText(content);
        return this;
    }

    public PermissionHintDialog setPositiveButton(ButtonClickListener listener){
        confirmClick=listener;
        return this;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.cancle_tv){
            dismiss();
            cancleClick.onClick();
        }else if(v.getId()==R.id.confirm_tv){
            dismiss();
            confirmClick.onClick();
        }
    }


}
