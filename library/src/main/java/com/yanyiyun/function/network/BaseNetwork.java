package com.yanyiyun.function.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import retrofit2.Retrofit;

/**
 * 基础网络请求类
 */
public abstract class BaseNetwork {

    protected Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create();

    public <V> V getServices(Class<V> service){
        return getRetrofit().create(service);
    }

    public abstract Retrofit getRetrofit();

    /**
     * 获取网络请求的url
     * @return
     */
    public abstract String getBaseUrl();

    /**
     * 获取请求头
     * @return
     */
    public abstract HashMap<String,String> getHeaders();

    /**
     * 获取公共参数
     * @return
     */
    public abstract HashMap<String,String> getPublicParams();
}
