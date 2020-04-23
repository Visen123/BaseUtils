package com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.util;

import android.view.View;
import android.widget.OverScroller;

import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.view.SwipeMenuRecyclerView;

public class SwipeLeftHorizontal extends SwipeHorizontal {

    public SwipeLeftHorizontal( View menuView) {
        super(SwipeMenuRecyclerView.LEFT_DIRECTION, menuView);
    }

    @Override
    public boolean isMenuOpen(int scrollX) {
        int i=-getMenuView().getWidth()*getDirection();
        return scrollX<=i&&i!=0;
    }

    @Override
    public boolean isMenuOpenNotEqual(int scrollX) {
        return scrollX<-getMenuView().getWidth()*getDirection();
    }

    @Override
    public void autoOpenMenu(OverScroller scroller, int scrollX, int duration) {
        scroller.startScroll(Math.abs(scrollX),0,
                getMenuView().getWidth()-Math.abs(scrollX),0,duration);
    }

    @Override
    public void autoCloseMenu(OverScroller scroller, int scrollX, int duration) {
        scroller.startScroll(-Math.abs(scrollX),0,Math.abs(scrollX),0,duration);
    }

    /**
     * 保证滑动的范围在0到 -getMenuView().getWidth()
     * @param x
     * @param y
     * @return
     */
    @Override
    public Checker checkXY(int x, int y) {
        mChecker.x=x;
        mChecker.y=y;
        mChecker.shouldResetSwipe=false;
        if(mChecker.x==0){
            mChecker.shouldResetSwipe=true;
        }
        if(mChecker.x>=0){
            mChecker.x=0;
        }
        if(mChecker.x<-getMenuView().getWidth()){
            mChecker.x=-getMenuView().getWidth();
        }
        return mChecker;
    }

    @Override
    public boolean isClickOnCotentView(int contentViewWidth, float x) {
        return x>getMenuView().getWidth();
    }
}
