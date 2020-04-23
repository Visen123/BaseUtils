package com.yanyiyun.function.banner;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yanyiyun.function.banner.interfac.IBannerEntity;
import com.yanyiyun.function.banner.interfac.OnBannerClickListener;
import com.yanyiyun.function.banner.interfac.OnBannerImgShowListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by voctex on 2016/7/22.
 * banner的适配器
 */
public class BannerAdapter extends PagerAdapter {

    /**
     * 装ImageView数组
     */
    private List<ImageView> imgList = new ArrayList<>();
    private OnBannerClickListener onBannerClickListener;

    public BannerAdapter(Context mContext, final List<IBannerEntity> mlist, OnBannerImgShowListener callBack) {
        imgList.clear();
        for (int i = 0; i < mlist.size(); i++) {
            IBannerEntity entity = mlist.get(i);
            final int position = i;
            ImageView img = new ImageView(mContext);
            img.setScaleType(ImageView.ScaleType.FIT_XY);
            img.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT));
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onBannerClickListener != null) {
                        onBannerClickListener.onBannerClick(position - 1, mlist.get(position));
                    }
                }
            });
//            if (entity.getAdImg() == null && entity.getAdResId() != 0) {
//                img.setImageResource(entity.getAdResId());
//            } else {

            if(entity.getImgResid()!=0){
                img.setImageResource(entity.getImgResid());
            }else {
                //回调到最上层，让上层模块自己选择图片加载框架加载网络图片
                if (callBack != null) {
                    callBack.onBannerShow(mlist.get(i).getBannerImgUrlStr(), img);
                }
            }
//            }
            imgList.add(img);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(imgList.get(position));

    }

    /**
     * 载入图片进去，用当前的position 除以 图片数组长度取余数是关键
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView img = imgList.get(position);
        container.addView(img);
        return imgList.get(position);
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        super.setPrimaryItem(container, position, object);
    }


    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
    }


}
