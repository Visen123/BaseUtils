package com.yanyiyun.baseutils.library.function.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class MyGsonConverterFactory extends Converter.Factory {

    private static MyGsonConverterFactory myConverterFactory;

    private MyGsonConverterFactory(){}

    public static MyGsonConverterFactory create() {
        if(null == myConverterFactory) {
            myConverterFactory = new MyGsonConverterFactory();
        }
        return myConverterFactory;
    }

    private Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd hh:mm:ss")
            .create();
    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations,
                                                            Retrofit retrofit) {

        TypeAdapter<?> _typeAdapter = gson.getAdapter(TypeToken.get(type));

        if(type instanceof Class) {
            return new MyGsonConverter<>(_typeAdapter);
        }
        return null;
    }
}
