package com.yanyiyun.function.banner;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.yanyiyun.R;
import com.yanyiyun.function.banner.interfac.IBannerEntity;
import com.yanyiyun.function.banner.interfac.OnBannerClickListener;
import com.yanyiyun.function.banner.interfac.OnBannerImgShowListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

//import com.voctex.banners.bean.BannerEntity;


/**
 * Created by Voctex on 2016/8/14.
 * 广告轮播banner
 */
public class BannerLayout extends FrameLayout implements ViewPager.OnPageChangeListener {

    private BannerViewPager viewPager;
    private BannerPointLayout bannerPointLayout;
    private int currentPosition;
    private Timer mTimer = new Timer();
    private BannerAdapter bannerAdapter;

    public BannerLayout(Context context) {
        super(context);
        initView();
    }

    public BannerLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @TargetApi(value = Build.VERSION_CODES.LOLLIPOP)//21
    public BannerLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }


    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.banner_view, this);
        viewPager = (BannerViewPager) findViewById(R.id.vt_banner_id);
        bannerPointLayout = (BannerPointLayout) findViewById(R.id.vt_banner_point);


    }

    private List<IBannerEntity> mEntities = new ArrayList<>();


    /**
     * 绑定数据
     */
    public void setEntities(List<IBannerEntity> entities, OnBannerImgShowListener callBack) {

        if (callBack==null){
            throw new IllegalStateException("请实现图片加载方法");
        }

        if (entities != null && entities.size() > 0) {
            mEntities.clear();
            mEntities.add(entities.get(entities.size() - 1));
            mEntities.addAll(entities);
            mEntities.add(entities.get(0));

            bannerPointLayout.setPointCount(mEntities.size() - 2);
            bannerAdapter = new BannerAdapter(getContext(), mEntities,callBack);
            viewPager.setAdapter(bannerAdapter);
            viewPager.addOnPageChangeListener(this);
            viewPager.setCurrentItem(1);
            if (entities.size() > 1) {
                viewPager.setCanScroll(true);
            } else {
                viewPager.setCanScroll(false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        if (slidingPageListener != null) {
            slidingPageListener.onPageIndex(position);
        }
        if (positionOffsetPixels == 0.0) {
            if (position == mEntities.size() - 1) {
                viewPager.setCurrentItem(1, false);
            } else if (position == 0) {
                viewPager.setCurrentItem(mEntities.size() - 2, false);
            } else {
                viewPager.setCurrentItem(position);
            }
        }
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;

        if (position == mEntities.size() - 1) {
            bannerPointLayout.setPosition(0);
        } else if (position == 0) {
            bannerPointLayout.setPosition(mEntities.size() - 2 - 1);
        } else {
            bannerPointLayout.setPosition(position - 1);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 设置点击事件
     */
    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener) {
        if (bannerAdapter != null) {
            bannerAdapter.setOnBannerClickListener(onBannerClickListener);
        }
    }

    /**默认启动动画的方法*/
    public void schedule() {
        schedule(2000, 4000);
    }

    private boolean isFirst = false;

    /**可以设置动画间隔时间的方法*/
    public void schedule(long delay, long period) {
        if (mEntities == null || mEntities.size() <= 3) {
            return;
        }
        if (!isFirst) {
            mTimer.schedule(mTimerTask, delay, period);
            isFirst = true;
        }
    }

    private TimerTask mTimerTask = new TimerTask() {
        @Override
        public void run() {

            handler.sendEmptyMessage(0);
        }
    };

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (currentPosition != mEntities.size() - 1) {
                currentPosition = currentPosition + 1;
                viewPager.setCurrentItem(currentPosition);
            } else {
                currentPosition = 0;
                viewPager.setCurrentItem(0);
            }
        }
    };

    /**
     * 设置点的位置
     */
    public void setPointPotision(int gravity) {
        LayoutParams layoutParams = (LayoutParams) bannerPointLayout.getLayoutParams();
        layoutParams.gravity = gravity;
        bannerPointLayout.setLayoutParams(layoutParams);
    }

    /**
     * 设置点的颜色
     */
    public void setPointColor(int lightColor, int dimColor) {
        bannerPointLayout.setPointColor(lightColor, dimColor);
    }

    /**
     * 设置点的margin
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setPointMargin(int left,int top,int right,int bottom){
        LayoutParams layoutParams= (LayoutParams) bannerPointLayout.getLayoutParams();
        layoutParams.setMargins(left,top,right,bottom);
        bannerPointLayout.setLayoutParams(layoutParams);
    }


    public interface SlidingPageListener {
        void onPageIndex(int index);
    }

    private SlidingPageListener slidingPageListener;

    public void setSlidingPageListener(SlidingPageListener slidingPageListener) {
        this.slidingPageListener = slidingPageListener;
    }


}
