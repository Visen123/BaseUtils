package com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.touch;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public interface OnItemMovementListener {
    int INVALID = 0;

    int LEFT = ItemTouchHelper.LEFT;

    int UP = ItemTouchHelper.UP;

    int RIGHT = ItemTouchHelper.RIGHT;

    int DOWN = ItemTouchHelper.DOWN;

    int onDragFlags(RecyclerView recyclerView,RecyclerView.ViewHolder targetViewHolder);

    int onSwipeFlags(RecyclerView recyclerView,RecyclerView.ViewHolder targetViewHolder);
}
