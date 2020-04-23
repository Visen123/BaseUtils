package com.yanyiyun.baseutils.library.function;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yanyiyun.baseutils.library.function.dialog.product.LoadDialog;
import com.yanyiyun.baseutils.library.function.mvp.IBaseView;
import com.yanyiyun.baseutils.library.tool.ScreenUitl;

public class BaseToolActivity extends AppCompatActivity implements IBaseView {

    private LoadDialog loadDialog;

    public void setTextStatusBar(int emptyID){
        ScreenUitl.setHead(this,findViewById(emptyID),
                android.R.color.white,ScreenUitl.StatusBarStyle.COLOR);
        ScreenUitl.setStatusBarLightMode(this);
    }

    public void setImageStatusBar(int emptyID){
        ScreenUitl.setHead(this, findViewById(emptyID),
                android.R.color.transparent, ScreenUitl.StatusBarStyle.IMAGE);
        ScreenUitl.setStatusBarDarkMode(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadDialog=new LoadDialog(this);
    }

    @Override
    public void showLoading() {
        if(!loadDialog.isShowing()){
            loadDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if(loadDialog.isShowing()){
            loadDialog.dismiss();
        }
    }

    @Override
    public void showToast(String msg) {
        try {
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Looper.prepare();
            Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
    }

    @Override
    public void showErr(String msg) {
        if(msg!=null&&!msg.isEmpty()){
            Toast.makeText(getContext(),msg, Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(),"发生错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public Activity getAcitvity() {
        return this;
    }


    @Override
    protected void onDestroy() {
        if(loadDialog.isShowing()){
            loadDialog.dismiss();
        }
        super.onDestroy();
    }
}
