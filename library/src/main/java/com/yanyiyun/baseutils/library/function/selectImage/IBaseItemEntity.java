package com.yanyiyun.baseutils.library.function.selectImage;

import android.os.Parcelable;

public interface IBaseItemEntity extends Parcelable {

    /**
     * 获得视频或者图片路径
     * @return
     */
    String getPath();

    /**
     * 设置视频或者图片路径
     * @param path
     */
    void setPath(String path);

    /**
     * 设置缩略图
     * @param thumb
     */
    void setThumb(String thumb);

    /**
     * 获得缩略图
     * @return
     */
    String getThumb();

    /**
     * 设置视频持续时间
     * @param duration
     */
    void setDuration(int duration);

    /**
     * 获得视频持续时间
     * @return
     */
    int getDuration();

    void setSelect(boolean select);

    boolean isSelect();

    /**
     * 获取item类型
     * @return
     */
    int getType();

    /**
     * 设置item类型
     */
    void setType(int type);
}
