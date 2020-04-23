package com.yanyiyun.function.selectImage;

import android.os.Parcel;

public class ItemEntity implements IBaseItemEntity {
    /**
     * 视频文件路径
     */
    private String path;
    /**
     * 视频持续时间
     */
    private int duration;
    /**
     * 视频缩略图路径或者图片路径
     */
    private String thumb;

    private boolean isSelect;

    private int type;

    public ItemEntity() {
    }

    protected ItemEntity(Parcel in) {
        path = in.readString();
        duration = in.readInt();
        thumb = in.readString();
        isSelect = in.readByte() != 0;
        type=in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeInt(duration);
        dest.writeString(thumb);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeInt(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ItemEntity> CREATOR = new Creator<ItemEntity>() {
        @Override
        public ItemEntity createFromParcel(Parcel in) {
            return new ItemEntity(in);
        }

        @Override
        public ItemEntity[] newArray(int size) {
            return new ItemEntity[size];
        }
    };

    @Override
    public boolean isSelect() {
        return isSelect;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public void setType(int type) {
        this.type=type;
    }

    @Override
    public void setSelect(boolean select) {
        isSelect = select;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int getDuration() {
        return duration;
    }

    @Override
    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public String getThumb() {
        return thumb;
    }

    @Override
    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
