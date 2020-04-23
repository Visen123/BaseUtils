package com.yanyiyun.baseutils.activity.designPattern.commandMode;

import com.yanyiyun.tool.log;

/**
 * 接收者
 */
public class Lights {

    public void off(){
        log.e("灯关闭");
    }

    public void on(){
        log.e("灯打开");
    }
}
