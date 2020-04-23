package com.yanyiyun.baseutils.library.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;

/**
 * 实现ViewAnimationUtils动画的自定义View
 */
public class AnimView extends View {

    private float width;
    private float height;

    private float raduis;

    /**
     * 图片是否显示
     */
    private boolean isShow=true;

    /**
     * 图片是否正在执行动画
     */
    private boolean isAniming=false;

    public AnimView(Context context) {
        super(context);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        width=getMeasuredWidth();
        height=getMeasuredHeight();

        //如width 3  height 4 所得值为5 获得直角三角形斜边长度
        raduis= (float) Math.hypot(width,height);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x=event.getX();
                float y=event.getY();

                if(!isAniming){
                    showAnim((int)x,(int)y);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void showAnim(int x,int y){
        isAniming=true;

        //5.0以上才能使用
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            if(isShow){  //隐藏
                /**
                 * view 执行动画的view
                 * x 原点坐标
                 * y 原点坐标
                 * startRadius 动画开始的半径
                 * endRadius  动画结束的半径
                 */
                Animator circulayReveal= ViewAnimationUtils.createCircularReveal(this,x,y,raduis,0);

                circulayReveal.setDuration(1000);
                circulayReveal.start();

                circulayReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        setAlpha(0);
                        isShow=false;
                        isAniming=false;
                    }
                });
            }else {  //显示
                isShow=true;
                setAlpha(1);

                Animator circulayReveal= ViewAnimationUtils.createCircularReveal(this,x,y,0,raduis);
                circulayReveal.setDuration(1000);
                circulayReveal.start();

                circulayReveal.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        isAniming=false;
                    }
                });
            }
        }
    }
}
