package com.yanyiyun.function.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 请求结果解析为gson
 */
public abstract class PaseGsonNetwork extends BaseNetwork {

    @Override
    public Retrofit getRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addConverterFactory(MyGsonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(BaseOkHttpClient.getOkHttpClient(getPublicParams(),getHeaders()))
                .build();
    }
}
