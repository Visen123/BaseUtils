package com.yanyiyun.baseutils.util;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;


/**
 * 确定请求对话框
 */
public class ConfirmCancleDialog extends Dialog implements View.OnClickListener {

    private View view;
    private LinearLayout content_ll;
    private TextView title_tv,cancle_tv,confirm_tv,confirm1_tv;
    private RelativeLayout single_rl,confirm_and_cancle_rl;

    /**
     * 按钮数量 1个只有确定按钮  2有确定取消按钮
     */
    private int buttonNumber=2;
    private OnCancleAndConfirmListener listener;


    public ConfirmCancleDialog(@NonNull Context context) {
        super(context);
        view = LayoutInflater.from(context).inflate(R.layout.confirm_and_cancle_dialog, null);

       initview();
    }

    private void initview() {
        content_ll=view.findViewById(R.id.content_ll);
        title_tv=view.findViewById(R.id.title_tv);
//        single_rl=view.findViewById(R.id.single_rl);
//        confirm_and_cancle_rl=view.findViewById(R.id.confirm_and_cancle_rl);
        cancle_tv=view.findViewById(R.id.cancle_tv);
        confirm_tv=view.findViewById(R.id.confirm_tv);

        progressButtonNumber();

        Window window=getWindow();
        cancle_tv = view.findViewById(R.id.cancle_tv);
        cancle_tv.setOnClickListener(this);
        confirm_tv =  view.findViewById(R.id.confirm_tv);
        confirm_tv.setOnClickListener(this);
//        confirm1_tv = view.findViewById(R.id.confirm1_tv);
//        confirm1_tv.setOnClickListener(this);
        window.setGravity(Gravity.CENTER);
        window.setContentView(view);
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void progressButtonNumber(){
        if (buttonNumber == 1) {
//            single_rl.setVisibility(View.VISIBLE);
//            confirm_and_cancle_rl.setVisibility(View.GONE);
        } else if (buttonNumber == 2) {
//            single_rl.setVisibility(View.GONE);
//            confirm_and_cancle_rl.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置监听器
     * @param listener
     */
    public void  withListener(OnCancleAndConfirmListener listener){
        this.listener=listener;
    }

    public void setButtonNumber(int number){
        buttonNumber=number;
        progressButtonNumber();
    }

    /**
     * 设置dialog中间内容
     */
    public void setContent(View content) {
        content_ll.addView(content);
    }

    public void setContentView(View view) {
        content_ll.addView(view);
    }

    public void setTitleGone() {
        title_tv.setVisibility(View.GONE);
    }

    public void setTitleColor(int color) {
        title_tv.setTextColor(color);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    public void setTitle(String title) {
        title_tv.setText(title);
    }

    public void setTitleGravity(int gravity) {
        title_tv.setGravity(gravity);
    }
    public void setTitleSize(float size) {
        title_tv.setTextSize(size);
    }

    public void setCancleBackground(int id){
        cancle_tv.setBackgroundResource(id);
    }

    public void setCancleText(String text){
        cancle_tv.setText(text);
    }

    public void setConfirmBackground(int resid){
        confirm_tv.setBackgroundResource(resid);
        confirm1_tv.setBackgroundResource(resid);
    }

    public void setConfirmText(String text){
        confirm_tv.setText(text);
//        confirm1_tv.setText(text);
    }
    public void setCancleTextColor(int color){
        cancle_tv.setTextColor(color);
    }

    public void setConfirmTextColor(int color){
        confirm1_tv.setTextColor(color);
        confirm_tv.setTextColor(color);
    }

    public interface OnCancleAndConfirmListener {
        public void cancle();

        public void confirm();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancle_tv:
                dismiss();
                listener.cancle();
                break;

//            case R.id.confirm1_tv:
            case R.id.confirm_tv:
                dismiss();
                listener.confirm();
                break;
            default:
                break;
        }
    }
}
