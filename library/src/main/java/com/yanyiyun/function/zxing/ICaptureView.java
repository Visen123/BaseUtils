package com.yanyiyun.function.zxing;

import com.yanyiyun.function.zxing.view.ViewfinderView;

public interface ICaptureView extends IBaseCaptureView{

     ViewfinderView getViewfinderView();

     /**
      * 切换闪光灯图片
      * @param flashState
      */
     void switchFlashImg(int flashState);

}
