package com.yanyiyun.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 图片操作工具
 */
public class ImageProgressTool {

    private static float modelH=800f;
    private static float modelW=480f;

    public static void setModel(float w,float h){
        modelW=w;
        modelH=h;
    }
    /**
     * 对Bitmap质量压缩
     * @param image
     * @return
     */
    public static Bitmap compressImage2Bitmap(Bitmap image){
        byte[] baos=compressImage2byte(image,100);

        ByteArrayInputStream isBm=new ByteArrayInputStream(baos);
        Bitmap bitmap=BitmapFactory.decodeStream(isBm,null,null);
        return bitmap;
    }

    /**
     * 质量压缩Bitmap返回字节数组
     * @param bitmap
     * @return
     */
    public static byte[] compressImage2byte(Bitmap bitmap,int maxOptions){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int options=100;
        //循环判断如果压缩后图片是否大于100kb,大于继续压缩
        while (baos.toByteArray().length/1024>maxOptions){
            baos.reset();
            //这里压缩options%，把压缩后的数据存放到baos中
            bitmap.compress(Bitmap.CompressFormat.JPEG,options,baos);
            options-=10;
        }

        return baos.toByteArray();
    }

    /**
     * 图片按比例大小压缩方法（根据路径获取图片并压缩）
     * @param srcPath
     * @return
     */
    public static Bitmap getImage(String srcPath){
        BitmapFactory.Options newOpts=new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds=true;
        //此时返回bm为空
        BitmapFactory.decodeFile(srcPath,newOpts);

        newOpts.inJustDecodeBounds=false;
        int w=newOpts.outWidth;
        int h=newOpts.outHeight;

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be=1;

        if(w>h&&w>modelW){ //如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / modelW);
        }else if(w<h&&h>modelH){
            be = (int) (newOpts.outHeight / modelH); //如果高度高的话根据宽度固定大小缩放
        }

        if(be<=0){
            be=1;
        }

        newOpts.inSampleSize=be;
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        Bitmap bitmap=BitmapFactory.decodeFile(srcPath,newOpts);
        //压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    /**
     * 图片按比例大小压缩方法（根据Bitmap图片压缩）
     * @param image
     * @return
     */
    public static Bitmap comp(Bitmap image){
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG,100,baos);
        //判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if(baos.toByteArray().length/1024>1024){
            baos.reset();
            image.compress(Bitmap.CompressFormat.JPEG,50,baos);
        }

        ByteArrayInputStream isBm=new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts=new BitmapFactory.Options();
        newOpts.inJustDecodeBounds=true;
        Bitmap bitmap=BitmapFactory.decodeStream(isBm,null,newOpts);
        newOpts.inJustDecodeBounds=false;
        int w=newOpts.outWidth;
        int h=newOpts.outHeight;

        int be=1;
        if(w>h&&w>modelW){
            be = (int) (newOpts.outWidth / modelW);
        } else if (w < h && h > modelH) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / modelH);
        }

        if (be <= 0)
            be = 1;

        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage2Bitmap(bitmap);
    }
}
