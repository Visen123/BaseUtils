package com.yanyiyun.baseutils.activity.designPattern.commandMode;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;

/**
 * 命令模式
 */
public class CommandModeActivity extends BaseActivity implements View.OnClickListener{
    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.command_mode_activity);
        mContext=this;
        initview();
    }

    private void initview() {

        //Client  创建命令 并设置其接收者
        Lights lights=new Lights();
        LightOnCommand lightOnCommand=new LightOnCommand(lights);

        //调用者
        RemoteControl remoteControl =new RemoteControl();
//        remoteControl.setCommand(lightOnCommand);
//        remoteControl.buttonWasPressed();
    }

    @Override
    public void onClick(View v) {

    }
}
