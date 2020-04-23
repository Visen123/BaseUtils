package com.yanyiyun.function.network;

/**
 * 响应消息结构体
 *
 * @author lizhi
 */
public class Result {
    private int code;   //返回10000为请求成功
    private String msg;

    public Result() {
    }

    public Result(String msg, int code) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}