package com.yanyiyun.baseutils.activity.okhttp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.function.screenAdaptation.ScreenAdapterTools;
import com.yanyiyun.tool.log;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

/**
 * OkHttp的基本使用
 */
public class OkHttpDemoActivity extends BaseActivity {

    @BindView(R.id.head_text_title)TextView head_text_title;

    private String getUrl="http://api.entserver.lan/V3/customer/login?username=18259243837&password=123456&type=1&login_key=5e8bc9947ed224d8ef01dd99fe8f2df1&access_token=068adfd8ab7137d096468b17866c24f5&systems_os=android_HUAWEI NXT-AL10_8.0.0&version_os=1&app_version=android_432&ucopenid=083d2581319fc6f7cdb503df1dfe089f&version_type=1&appid=299e5b6a4871202g&client_time=1563780362&sign=&";
    private String postUrl="http://api.entserver.lan/V3/customer/login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.okhttp_demo_activity);
        ScreenAdapterTools.getInstance().loadView(getWindow().getDecorView());
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        head_text_title.setText("OkHttp的基本使用");
    }

    /**
     * 异步GET请求
     */
    private void asynGet(){
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder()
                .url(getUrl)
                .get()
                .build();
        Call call=client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body()!=null){
                    log.e("response:"+response.body().string());
                }
            }
        });
    }

    /**
     * 同步GET请求
     */
    private void synGet(){
        OkHttpClient client=new OkHttpClient();
        final Request request=new Request.Builder()
                .url(getUrl)
                .get()
                .build();
        final Call call=client.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Response response=call.execute();
                    if(response.body()!=null){
                        log.e("response:"+response.body().string());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 异步PostString
     */
    private void asynPostString(){
        MediaType mediaType=MediaType.parse("text/x-markdown; charset=utf-8");
        String requestBody="I am Jdqm";
        Request request=new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType,requestBody))
                .build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request)
                .enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        log.e("请求失败");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.body()!=null){
                            log.e("response:"+response.body().string());
                        }
                    }
                });
    }

    /**
     * 异步PostStreanm
     */
    private void asynPostStream(){
        RequestBody requestBody=new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return MediaType.parse("text/x-markdown; charset=utf-8");
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.writeUtf8("I am Jdqm");
            }
        };
        Request request=new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(requestBody)
                .build();
        OkHttpClient client=new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.e("请求成功");
                if(response.body()!=null){
                    log.e("response:"+response.body().string());
                }
            }
        });
    }

    /**
     * 异步PostFile
     */
    private void asynPostFile(){
        MediaType mediaType=MediaType.parse("text/x-markdown; charset=utf-8");
        OkHttpClient client=new OkHttpClient();
        File file=new File("text.md");
        Request request=new Request.Builder()
                .url("https://api.github.com/markdown/raw")
                .post(RequestBody.create(mediaType,file))
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.e("请求成功");
                if(response.body()!=null){
                    log.e("response:"+response.body().string());
                }
            }
        });
    }

    /**
     * 异步PostForm
     */
    private void asynPostForm(){
        OkHttpClient client=new OkHttpClient();
        RequestBody requestBody=new FormBody.Builder()
                .add("username","18259243837")
                .add("password","123456")
                .add("type","1")
                .add("login_key","5e8bc9947ed224d8ef01dd99fe8f2df1")
                .add("access_token","068adfd8ab7137d096468b17866c24f5")
                .add("systems_os","android_HUAWEI NXT-AL10_8.0.0")
                .add("version_os","1")
                .add("app_version","432")
                .add("ucopenid","083d2581319fc6f7cdb503df1dfe089f")
                .add("version_type","1")
                .add("appid","299e5b6a4871202g")
                .add("client_time","1563780362")
                .build();
        Request request=new Request.Builder()
                .url(postUrl)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                log.e("请求成功");
                if(response.body()!=null){
                    log.e("response:"+response.body().string());
                }
            }
        });
    }

    /**
     * 异步分块请求
     */
    private void asynPostMultipartBody(File file){
        OkHttpClient okHttpClient=new OkHttpClient();

        MultipartBody.Builder builder=new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM);

        builder.addFormDataPart("type","image");
        builder.addFormDataPart("access_token","a2d978f75d34be8f030541560d808b66");
        builder.addFormDataPart("ucopenid","083d2581319fc6f7cdb503df1dfe089f");
        builder.addFormDataPart("appid","299e5b6a4871202g");
        builder.addFormDataPart("file",file.getName(),RequestBody.create(MediaType.parse("image/png"),file));
        MultipartBody body= builder.build();
        Request request=new Request.Builder()
                .url("http://api.entserver.lan/V1/file/upload")
                .post(body)
                .build();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.e("请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.body()!=null){
                    log.e("response:"+response.body().string());
                }
            }
        });
    }

    @OnClick({R.id.head_img_left,R.id.asyn_get_tv,R.id.syn_get_tv,R.id.asyn_post_string_tv
            ,R.id.asyn_post_stream_tv,R.id.asyn_post_form_tv})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
                //异步GET请求
            case R.id.asyn_get_tv:
                asynGet();
                break;
                //同步GET请求
            case R.id.syn_get_tv:
                synGet();
                break;
                //异步PostString
            case R.id.asyn_post_string_tv:
                asynPostString();
                break;
                //异步PostSteanm
            case R.id.asyn_post_stream_tv:
                asynPostStream();
                break;
                //异步PostForm
            case R.id.asyn_post_form_tv:
                asynPostForm();
                break;
        }
    }
}
