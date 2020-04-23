package com.yanyiyun.baseutils.activity.selfview;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.yanyiyun.baseutils.R;
import com.yanyiyun.baseutils.activity.BaseActivity;
import com.yanyiyun.function.banner.BannerLayout;
import com.yanyiyun.function.banner.bean.BannerEntity;
import com.yanyiyun.function.banner.interfac.IBannerEntity;
import com.yanyiyun.function.banner.interfac.OnBannerClickListener;
import com.yanyiyun.function.banner.interfac.OnBannerImgShowListener;

import java.util.ArrayList;
import java.util.List;

public class BannerLayoutActivity extends BaseActivity implements View.OnClickListener,OnBannerImgShowListener,OnBannerClickListener {

    private TextView head_text_title;
    private BannerLayout banner_bl;

    private Context mContext;
    private String[] imgs = {"http://img.zcool.cn/community/0128d7579588680000012e7e04ea1b.png",
            "http://img.mp.sohu.com/upload/20170622/0413b0b8196c4dff992f23e776f222ea_th.png",
            "http://img.mp.sohu.com/upload/20170608/3071b0ecd12848ee8af6189c5be0a287_th.png",
            "http://img4.imgtn.bdimg.com/it/u=312061494,261017842&fm=26&gp=0.jpg",
            "http://img3.imgtn.bdimg.com/it/u=3067155577,1063155389&fm=214&gp=0.jpg",
            "http://img.mp.sohu.com/upload/20170622/d98f293dfbaa41a8bac4e4216a0af3b7_th.png",
            "http://img.mp.sohu.com/upload/20170622/38520b8b41a94610acabf61afc573fbe_th.png"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bannerlayou_activity);
        mContext=this;
        initview();
    }

    private void initview() {
        findViewById(R.id.head_img_left).setOnClickListener(this);
        head_text_title=findViewById(R.id.head_text_title);
        head_text_title.setText("BannerLayout");

        banner_bl=findViewById(R.id.banner_bl);

        List<IBannerEntity> mList = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            BannerEntity bannerEntity = new BannerEntity();
            bannerEntity.setAdImg(imgs[i]);
            mList.add(bannerEntity);
        }
        BannerEntity bannerEntity=new BannerEntity();
        bannerEntity.setAdResId(R.mipmap.image);
        mList.add(bannerEntity);

        banner_bl.setEntities(mList,this); //绑定数据
        banner_bl.setPointColor(Color.BLUE, Color.RED);  //设置圆点的颜色
        banner_bl.setPointPotision(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);//设置圆点的位置
        banner_bl.setPointMargin(10,0,10,90); //这个方法可以移动圆点的位置
        banner_bl.schedule(2000, 3000); //设置轮播图跳转的间隔时间
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.head_img_left:
                finish();
                break;
        }
    }

    @Override
    public void onBannerShow(String url, ImageView imgView) {
        RequestOptions options = new RequestOptions().centerCrop().priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE).error(R.mipmap.image).placeholder(R.mipmap.image);
        Glide.with(this)
                .load(url)
                .apply(options)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView);
    }

    @Override
    public void onBannerClick(int position, IBannerEntity bean) {

    }
}
