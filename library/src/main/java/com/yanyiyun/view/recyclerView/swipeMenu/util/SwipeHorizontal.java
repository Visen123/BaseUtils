package com.yanyiyun.view.recyclerView.swipeMenu.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.OverScroller;

/**
 * 水平方向的侧滑菜单
 */
public abstract class SwipeHorizontal {
    private int direction;
    private View menuView;
    protected Checker mChecker;

    public SwipeHorizontal(int direction, View menuView) {
        this.direction = direction;
        this.menuView = menuView;
        mChecker=new Checker();
    }

    /**
     * 是否可以侧滑
     * @return
     */
    public boolean canSwipe(){
        if(menuView instanceof ViewGroup){
            return ((ViewGroup)menuView).getChildCount()>0;
        }
        return false;
    }

    /**
     * 侧滑菜单是否关闭
     * @param scrollX
     * @return
     */
    public boolean isCompleteClose(int scrollX){
        int i=-getMenuView().getWidth()*getDirection();
        return scrollX==0&&i!=0;
    }

    public int getDirection(){
        return direction;
    }

    public View getMenuView(){
        return menuView;
    }

    public int getMenuWidth(){
        return menuView.getWidth();
    }

    /**
     * 侧滑菜单是否打开
     * @param scrollX
     * @return
     */
    public abstract boolean isMenuOpen(int scrollX);

    public abstract boolean isMenuOpenNotEqual(int scrollX);

    /**
     * 打开侧滑菜单
     * @param scroller
     * @param scrollX
     * @param duration
     */
    public abstract void autoOpenMenu(OverScroller scroller,int scrollX,int duration);

    /**
     * 关闭侧滑菜单
     * @param scroller
     * @param scrollX
     * @param duration
     */
    public abstract void autoCloseMenu(OverScroller scroller,int scrollX,int duration);

    public abstract Checker checkXY(int x,int y);

    /**
     * 是否点击在内容区域
     * @param contentViewWidth
     * @param x
     * @return
     */
    public abstract boolean isClickOnCotentView(int contentViewWidth,float x);

    /**
     * 保证滑动的x y值正确
     */
    public static final class Checker {
        public int x;
        public int y;
        public boolean shouldResetSwipe;
    }
}
