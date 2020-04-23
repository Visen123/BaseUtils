package com.yanyiyun.function.selectImage;

import java.util.Comparator;

public class ImageFolderComparator implements Comparator<Floder> {
    @Override
    public int compare(Floder o1, Floder o2) {
        if(o1.getCount()<o2.getCount()){
            return 1;
        }else if(o1.getCount()==o2.getCount()){
            return 0;
        }else {
            return -1;
        }
    }
}
