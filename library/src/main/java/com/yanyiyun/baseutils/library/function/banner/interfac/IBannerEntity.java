package com.yanyiyun.baseutils.library.function.banner.interfac;

/**
 * 可通过实现该接口进行对bean类的拓展
 */
public interface IBannerEntity {

    /**
     * 怕重复方法名，所以名字命名长一点
     */
    String getBannerImgUrlStr();
    int getImgResid();
}
