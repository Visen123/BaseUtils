package com.yanyiyun.baseutils.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.adapter.SelectImageAndAdapter;
import com.yanyiyun.baseutils.library.function.selectImage.AndSelectImage;
import com.yanyiyun.baseutils.library.function.selectImage.ItemEntity;
import com.yanyiyun.baseutils.library.tool.log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 选择本地图片
 */
public class SelectImageAndActivity extends BaseActivity implements View.OnClickListener{
    private TextView head_text_title;
    private GridView list_gv;

    private Context mContext;
    private ArrayList<String> data=new ArrayList<>();
    private SelectImageAndAdapter adapter;
    private final int SELECT_IMAGE=0;
    private final int SELECT_VIDEO=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_image_and_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("选择图片");

        list_gv=findViewById(R.id.list_gv);

        findViewById(R.id.add_tv).setOnClickListener(this);

        adapter=new SelectImageAndAdapter(mContext,data,SELECT_IMAGE);
        list_gv.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode==RESULT_OK){
            Bundle bundle=intent.getExtras();
            switch (requestCode){
                //选择图片
                case SELECT_IMAGE:
                    ArrayList<ItemEntity> list=bundle.getParcelableArrayList(AndSelectImage.SELECT_IMAGE);
                    data.clear();
                    if(list!=null&&list.size()>0){
                        for(int i=0;i<list.size();i++){
                            ItemEntity itemEntity=list.get(i);
                            data.add(itemEntity.getPath());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    asynPostMultipartBody(new File(list.get(0).getPath()));
                    break;
                    //选择视频返回
                case SELECT_VIDEO:
                    ArrayList<ItemEntity> l=bundle.getParcelableArrayList(AndSelectImage.SELECT_VIDEO);
                    data.clear();
                    if(l!=null&&l.size()>0){
                        for(int i=0;i<l.size();i++){
                            ItemEntity itemEntity=l.get(i);
                            data.add(itemEntity.getPath());
                        }
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
            break;
            case R.id.add_tv:
                new AndSelectImage().withActivity(this)
                        .withNumber(1)
                        .withRequestCode(SELECT_IMAGE)
                        .withType(AndSelectImage.TYPE_IMAGE)
                        .withColumnNumber(4)
                        .withTakePhoto(false)
                        .start();
                break;
        }
    }
}
