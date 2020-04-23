package com.yanyiyun.baseutils.library.function;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.yanyiyun.baseutils.library.R;
import com.yanyiyun.baseutils.library.function.dialog.product.LoadDialog;
import com.yanyiyun.baseutils.library.function.mvp.IBaseView;
import com.yanyiyun.baseutils.library.tool.ScreenUitl;

public abstract class BaseToolFragment extends Fragment implements IBaseView {

    public Context mContext;
    public View mRootView;
    private LoadDialog loadDialog;

    public abstract int getContentViewId();

    public abstract void initAllMembersView(Bundle savedInstanceState);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView=inflater.inflate(getContentViewId(),null);
        mContext=getActivity();
        if(getContext()!=null){
            loadDialog=new LoadDialog(getContext());
        }
        initAllMembersView(savedInstanceState);
        return mRootView;
    }

    @Override
    public void showLoading() {
        if(loadDialog!=null&&!loadDialog.isShowing()){
            loadDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if(loadDialog!=null&&loadDialog.isShowing()){
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
        if(mContext==null){
            mContext=super.getActivity();
        }
        return mContext;
    }

    @Override
    public Activity getAcitvity() {
        return super.getActivity();
    }

    public void setTextStatusBar(){
        ScreenUitl.setHead(getActivity(),mRootView.findViewById(R.id.head_top),
                android.R.color.white,ScreenUitl.StatusBarStyle.COLOR);
        ScreenUitl.setStatusBarLightMode(getActivity());
    }

    public void setImageStatusBar(){
        ScreenUitl.setHead(getActivity(), mRootView.findViewById(R.id.head_top),
                android.R.color.transparent, ScreenUitl.StatusBarStyle.IMAGE);
        ScreenUitl.setStatusBarDarkMode(getActivity());
    }
}
