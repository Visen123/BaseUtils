package com.yanyiyun.baseutils.library.function.network;

import com.yanyiyun.baseutils.library.tool.StringTool;
import com.yanyiyun.baseutils.library.tool.log;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 添加参数和请求头插值器
 */
public class AddParameterAndHeaderInterceptor implements Interceptor {

    private HashMap<String,String> params;
    private HashMap<String,String> headers;
    private StringBuilder stringBuilder;

    public AddParameterAndHeaderInterceptor(HashMap<String, String> params,HashMap<String,String> headers) {
        this.headers=headers;
        this.params = params;
        stringBuilder=new StringBuilder();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();

        stringBuilder.append(request.url().toString()+"?");
        RequestBody requestBody=request.body();
        Request.Builder builder=request.newBuilder();

        if(requestBody instanceof FormBody){ //requestBody为表单
            FormBody.Builder formBodyBuilder=formBody((FormBody) requestBody,params);
            builder.method(request.method(),formBodyBuilder.build());
        }else {  //requestBody非表单
            HttpUrl.Builder httpUrlBuilder=notFormBody(request,params);
            builder.url(httpUrlBuilder.build());
        }

        addHeader(builder,headers);
        request=builder.build();
        log.e("请求的url:" + stringBuilder.toString());

        Response response = chain.proceed(request);

        return response;
    }

    /**
     * 添加请求头
     * @param builder
     * @param headers
     */
    private void addHeader(Request.Builder builder,HashMap<String,String> headers){
        if(headers!=null){
            for(String key:headers.keySet()){
               String val=headers.get(key);
               if(!StringTool.isEmpty(val)){
                   builder.addHeader(key,val);
               }
            }
        }
    }

    /**
     * 参数为表单数据
     * @param formBody
     * @param params
     * @return
     */
    private FormBody.Builder formBody(FormBody formBody,HashMap<String,String> params){
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        for(int i=0;i<formBody.size();i++){
            String name=formBody.name(i);
            String value=formBody.value(i);
            if(!StringTool.isEmpty(value)){
                formBodyBuilder.add(name,value);
                stringBuilder.append(name).append("=").append(value).append("&");
            }
        }
        if(params!=null){
            for(String key:params.keySet()){
                String val=params.get(key);
                formBodyBuilder.add(key,val);
                stringBuilder.append(key).append("=").append(val).append("&");
            }
        }
        return formBodyBuilder;
    }

    /**
     * 参数为非表单
     * @param request
     * @param params
     * @return
     */
    private HttpUrl.Builder notFormBody(Request request,HashMap<String,String> params){
        HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
        if(params!=null){
            for(String key:params.keySet()){
                String val=params.get(key);
                if(!StringTool.isEmpty(val)){
                    httpUrlBuilder.addQueryParameter(key,val);
                    stringBuilder.append(key).append("=").append(val).append("&");
                }
            }
        }
        return httpUrlBuilder;
    }
}
