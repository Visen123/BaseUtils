package com.yanyiyun.baseutils.library.function.dialog.factory;

import android.app.Dialog;
import android.content.Context;

import com.yanyiyun.baseutils.library.function.dialog.product.LoadDialog;
import com.yanyiyun.baseutils.library.function.dialog.product.TextCACD;

public  class DialogFactory {

    public enum DialogType{
        //中间文本内容的确定取消对话框
        TEXT_CACD,
        //加载对话框
        LOAD_DIALOG,
    }

    public static DialogFactory instance;

    public static DialogFactory getInstance() {
        if (instance == null) {
            synchronized (DialogFactory.class) {
                if (instance == null) {
                    instance = new DialogFactory();
                }
            }
        }
        return instance;
    }

    public Dialog createDialog(Context context,DialogType type){
        Dialog dialog=null;
        if(type==DialogType.TEXT_CACD){  //中间文本内容的确定取消对话框
            dialog=new TextCACD(context);
        }else if(type==DialogType.LOAD_DIALOG){  //加载对话框
            dialog=new LoadDialog(context);
        }
        return dialog;
    }
}
