package com.yanyiyun.function.dialog.product;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yanyiyun.R;

/**
 * 有确定取消两个按钮的对话框
 */
public class ConfirmAndCancleDialog extends Dialog implements View.OnClickListener {

    public View view;
    /**
     * 标题
     */
    public TextView title_tv;
    /**
     * 取消按钮
     */
    public TextView cancle_tv;
    /**
     * 确定按钮
     */
    public TextView confirm_tv;
    /**
     * 对话框内容容器
     */
    public LinearLayout content_ll;
    /**
     * 对话框容器
     */
    public LinearLayout main_ll;
    /**
     * 按钮容器
     */
    public LinearLayout button_ll;

    private OnCancleAndConfirmListener listener;

    public ConfirmAndCancleDialog(@NonNull Context context) {
        super(context);
        view=LayoutInflater.from(context).inflate(R.layout.confirm_and_cancle_dialog,null);
        initview();
    }

    private void initview() {
        title_tv=view.findViewById(R.id.title_tv);
        main_ll=view.findViewById(R.id.main_ll);
        content_ll=view.findViewById(R.id.content_ll);

        button_ll=view.findViewById(R.id.button_ll);

        cancle_tv=view.findViewById(R.id.cancle_tv);
        cancle_tv.setOnClickListener(this);

        confirm_tv=view.findViewById(R.id.confirm_tv);
        confirm_tv.setOnClickListener(this);

        Window window=getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void setOnCancleAndConfirmListener(OnCancleAndConfirmListener l){
        listener=l;
    }

    public interface OnCancleAndConfirmListener {
        public void cancle();

        public void confirm();
    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.cancle_tv){//取消
            if(listener!=null){
                listener.cancle();
            }
        }else if(v.getId()==R.id.confirm_tv){ //确定
            if(listener!=null){
                listener.confirm();
            }
        }
    }
}
