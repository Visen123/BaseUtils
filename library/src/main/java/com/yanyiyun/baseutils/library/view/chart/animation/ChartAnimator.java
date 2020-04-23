package com.yanyiyun.baseutils.library.view.chart.animation;

import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;

/**
 * 动画帮助类
 */
public class ChartAnimator {

    private ValueAnimator animator=new ValueAnimator();
    private ValueAnimator.AnimatorUpdateListener listener;
    private TimeInterpolator timeInterpolator;

    public ChartAnimator(ValueAnimator.AnimatorUpdateListener listener, TimeInterpolator timeInterpolator) {
        this.listener = listener;
        this.timeInterpolator = timeInterpolator;
    }

    public ChartAnimator(ValueAnimator.AnimatorUpdateListener listener) {
        this(listener,new DecelerateInterpolator());
    }

    public void animatedY(long duration,float y){
        if(animator!=null&&animator.isRunning()){
            animator.cancel();
            animator.start();
        }else {
            animator=ValueAnimator.ofFloat(0,y).setDuration(duration);
            animator.setInterpolator(timeInterpolator);
            animator.addUpdateListener(listener);
            animator.start();
        }
    }
}
