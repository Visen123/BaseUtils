package com.yanyiyun.function.network;

import com.yanyiyun.tool.log;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class BaseOkHttpClient {

    private static OkHttpClient okHttpClient;
    private static long TIME_OUT = 15;

    /**
     * 声明日志类
     */
    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

        @Override

        public void log(String message) {
                log.e("RetrofitLog:"+message);
        }

    });

    public static OkHttpClient getOkHttpClient(HashMap<String,String> params,HashMap<String,String> headers){
        if(okHttpClient==null){
            synchronized (BaseOkHttpClient.class){
                if(okHttpClient==null){
                    okHttpClient=createOkHttpClient(params,headers);
                }
            }
        }
        return okHttpClient;
    }

    /**
     *
     * @param params  请求公共参数
     * @param headers 请求头
     * @return
     */
    public static OkHttpClient createNewOkHttpClient(HashMap<String, String> params,HashMap<String,String> headers){
        return createOkHttpClient(params,headers);
    }

    /**
     *
     * @param params  请求公共参数
     * @param headers 请求头
     * @return
     */
    private static OkHttpClient createOkHttpClient(HashMap<String, String> params,HashMap<String,String> headers){
        return new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(TIME_OUT,TimeUnit.SECONDS)
                .readTimeout(TIME_OUT,TimeUnit.SECONDS)
                .addInterceptor(httpLoggingInterceptor)
                .addNetworkInterceptor(new AddParameterAndHeaderInterceptor(params,headers))
                .build();
    }
}
