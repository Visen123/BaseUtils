package com.yanyiyun.view.recyclerView.swipeMenu.entity;

import android.support.annotation.FloatRange;
import android.support.annotation.IntDef;
import android.support.annotation.IntRange;
import android.widget.LinearLayout;

import com.yanyiyun.view.recyclerView.swipeMenu.view.SwipeMenuLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

public class SwipeMenu {

    @IntDef({HORIZONTAL,VERTICAL})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OrientationMode{}

    public static final int HORIZONTAL=LinearLayout.HORIZONTAL;
    public static final int VERTICAL=LinearLayout.VERTICAL;

    private SwipeMenuLayout mMenuLayout;
    private int mOrientation;
    private List<SwipeMenuItem> mSwipeMenuItems;

    public SwipeMenu(SwipeMenuLayout mMenuLayout) {
        this.mMenuLayout = mMenuLayout;
        mOrientation=SwipeMenu.HORIZONTAL;
        mSwipeMenuItems=new ArrayList<>();
    }

    public void setOpenPercent(@FloatRange(from=0.1,to=1) float openPercent){
        mMenuLayout.setOpenPercent(openPercent);
    }

    public void setScrollerDuration(@IntRange(from=1) int scrollerDuration){
        mMenuLayout.setScrollerDuration(scrollerDuration);
    }

    public void setOrientation(@OrientationMode int orientation){
        mOrientation=orientation;
    }

    @OrientationMode
    public int getOrientation(){
        return mOrientation;
    }

    public void addMenuItem(SwipeMenuItem item){
        mSwipeMenuItems.add(item);
    }

    public void removeMenuItem(SwipeMenuItem item){
        mSwipeMenuItems.remove(item);
    }

    public List<SwipeMenuItem> getMenuItems(){
        return mSwipeMenuItems;
    }

    public boolean hasMenuItems(){
        return !mSwipeMenuItems.isEmpty();
    }
}
