package com.yanyiyun.view.recyclerView.swipeMenu.touch;

import android.support.v7.widget.helper.ItemTouchHelper;

public class DefaultItemTouchHelper extends ItemTouchHelper {

    private DefaultItemTouchHelperCallback mItemTouchHelperCallback;

    public DefaultItemTouchHelper(){
        this(new DefaultItemTouchHelperCallback());
    }

    private DefaultItemTouchHelper(DefaultItemTouchHelperCallback callback) {
        super(callback);
        mItemTouchHelperCallback=callback;
    }

    public void setOnItemMoveListener(OnItemMoveListener onItemMoveListener){
        mItemTouchHelperCallback.setOnItemMoveListener(onItemMoveListener);
    }

    public OnItemMoveListener getOnItemMoveListener(){
        return mItemTouchHelperCallback.getOnItemMoveListener();
    }

    public void setOnItemMovementListener(OnItemMovementListener onItemMovementListener){
        mItemTouchHelperCallback.setOnItemMovementListener(onItemMovementListener);
    }

    public OnItemMovementListener getOnItemMovementListener(){
       return mItemTouchHelperCallback.getOnItemMovementListener();
    }

    /**
     * 设置是否可以长按拖拽
     * @param canDrag
     */
    public void setLongPressDragEnabled(boolean canDrag) {
        mItemTouchHelperCallback.setLongPressDragEnabled(canDrag);
    }

    public boolean isLongPressDragEnabled() {
        return mItemTouchHelperCallback.isLongPressDragEnabled();
    }

    /**
     * 设置是否可以侧滑删除
     * @param canSwipe
     */
    public void setItemViewSwipeEnabled(boolean canSwipe) {
        mItemTouchHelperCallback.setItemViewSwipeEnabled(canSwipe);
    }

    public boolean isItemViewSwipeEnabled() {
        return this.mItemTouchHelperCallback.isItemViewSwipeEnabled();
    }

    public void setOnItemStateChangedListener(OnItemStateChangedListener onItemStateChangedListener) {
        this.mItemTouchHelperCallback.setOnItemStateChangedListener(onItemStateChangedListener);
    }

    public OnItemStateChangedListener getOnItemStateChangedListener() {
        return this.mItemTouchHelperCallback.getOnItemStateChangedListener();
    }
}
