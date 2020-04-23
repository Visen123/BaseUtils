package com.yanyiyun.baseutils.util;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.library.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.baseutils.library.view.CountDownButton;

/**
 * 铛铛易物  绑定手机号对话框
 */
public class BarterLoginDialog extends ConfirmCancleDialog {

    private EditText mobile_et,code_et;
    public CountDownButton get_code_cdb;

    public BarterLoginDialog(@NonNull Context context) {
        super(context);
        View view=LayoutInflater.from(context).inflate(R.layout.barter_login_content,null);
        ScreenAdapterTools.getInstance().loadView(view);
        setCancleText("绑定");
        setCancleTextColor(Color.WHITE);
//        setCancleBackground(R.drawable.activity_area_select_shape01);
        setConfirmText("暂不绑定");
//        setConfirmTextColor(context.getResources().getColor(R.color.load_color));
//        setTitle(context.getResources().getString(R.string.remind));

        mobile_et=view.findViewById(R.id.mobile_et);
        code_et=view.findViewById(R.id.code_et);
        get_code_cdb=view.findViewById(R.id.get_code_cdb);

        setContent(view);

    }

    /**
     * 获取手机号
     * @return
     */
    public String getMobile(){
        return mobile_et.getText().toString();
    }

    /**
     * 获取验证码
     * @return
     */
    public String getCode(){
        return code_et.getText().toString();
    }
}
