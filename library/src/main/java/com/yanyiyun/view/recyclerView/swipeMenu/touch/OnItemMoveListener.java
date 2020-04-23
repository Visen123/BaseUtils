package com.yanyiyun.view.recyclerView.swipeMenu.touch;

import android.support.v7.widget.RecyclerView;

public interface OnItemMoveListener {

    boolean onItemMove(RecyclerView.ViewHolder srcHolder,RecyclerView.ViewHolder targetHolder);

    void onItemDismiss(RecyclerView.ViewHolder srcHolder);
}
