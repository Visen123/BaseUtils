package com.yanyiyun.view.recyclerView.swipeMenu.interfaces;

public interface SwipeSwitch {
    /**
     * 菜单是否打开
     * @return
     */
    public boolean isMenuOpen();

    /**
     * 左边菜单是否打开
     * @return
     */
    public boolean isLeftMenuOpen();

    /**
     * 右边菜单是否打开
     * @return
     */
    public boolean isRightMenuOpen();

    /**
     * 菜单是否完成打开
     * @return
     */
    public boolean isCompleteOpen();

    /**
     * 左边菜单是否完成打开
     * @return
     */
    public boolean isLeftCompleteOpen();

    /**
     * 右边菜单是否完成打开
     * @return
     */
    public boolean isRightCompleteOpen();

    public boolean isMenuOpenNotEqual();

    public boolean isLeftMenuOpenNotEqual();

    public boolean isRightMenuOpenNotEqual();

    public void smoothOpenMenu();

    public void smoothOpenLeftMenu();

    public void smoothOpenRightMenu();

    public void smoothOpenLeftMenu(int duration);

    public void smoothOpenRightMenu(int duration);

    public  void smoothCloseMenu();

    public  void smoothCloseLeftMenu();

    public void smoothCloseRightMenu();

    public void smoothCloseMenu(int duration);
}
