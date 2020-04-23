package com.yanyiyun.function.network;

import com.google.gson.TypeAdapter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by Administrator on 2017/6/12 0012.
 */

public class MyGsonConverter<T> implements Converter<ResponseBody, T> {

    private TypeAdapter<T> typeAdapter;

    private ParseUtils<T> parseUtils;

    public MyGsonConverter(TypeAdapter typeAdapter) {
        this.typeAdapter = typeAdapter;
        parseUtils = new ParseUtils(typeAdapter);
    }

    @Override
    public T convert(ResponseBody value) throws IOException {

        return parseUtils.parseResult(value.string());
    }
}
