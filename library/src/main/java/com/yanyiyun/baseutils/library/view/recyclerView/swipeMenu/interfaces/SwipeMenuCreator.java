package com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.interfaces;

import com.yanyiyun.baseutils.library.view.recyclerView.swipeMenu.entity.SwipeMenu;

public interface SwipeMenuCreator {

    public void onCreateMenu(SwipeMenu leftMenu,SwipeMenu rightMenu,int position);
}
