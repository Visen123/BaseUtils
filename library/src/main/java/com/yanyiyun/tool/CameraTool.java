package com.yanyiyun.tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;

import com.yanyiyun.function.dialog.product.PermissionHintDialog;
import com.yanyiyun.function.permission.Action;
import com.yanyiyun.function.permission.AndPermission;
import com.yanyiyun.function.permission.Rationale;
import com.yanyiyun.function.permission.RequestExecutor;

import java.io.File;
import java.util.List;

/**
 * 照相机工具类
 */
public class CameraTool {

    private   final String DIR=Environment.getExternalStorageDirectory().toString();
    /**
     * 存储照片的路径
     */
    private   final String PATH_IMAGE = DIR+ "/tool/Camera/image";
    /**
     * 存储视频的路径
     */
    private   final String PATH_VIDEO=DIR+"/tool/Camera/video/";

//    public static final String packageName="com.dengjinwen.basetool.library";

    private   File mImageFile;
    private   String path;
    private static CameraTool instance;

    private CameraTool(){

    }

    public static CameraTool getInstance() {
       if(instance==null){
           synchronized (CameraTool.class){
               if(instance==null){
                   instance=new CameraTool();
               }
           }
       }
       return instance;
    }

    /**
     * 录制视频
     * @param activity
     * @param flag requestCode
     * @return
     */
    public  String takeVideo(Activity activity,int flag,PermissionFailListener listener){
        path=take(activity,flag,MediaStore.ACTION_VIDEO_CAPTURE,listener);
        return path;
    }

    /**
     * 拍照
     * @param activity
     * @param flag requestCode
     * @return
     */
    public  String takePicture(final Activity activity, final int flag,PermissionFailListener listener){
        path=take(activity,flag,MediaStore.ACTION_IMAGE_CAPTURE,listener);
        return path;
    }

    private   String take(final Activity activity, final int flag, final String type, final PermissionFailListener listener){
        final long time=System.currentTimeMillis();
        String parentPath;
        if(type.equals(MediaStore.ACTION_IMAGE_CAPTURE)){
            path=PATH_IMAGE+"/"+time+".jpg";
            parentPath=PATH_IMAGE;
        }else {
            path=PATH_VIDEO+"/"+time+".mp4";
            parentPath=PATH_VIDEO;
        }
        final File parentFile=new File(parentPath);
        AndPermission.with(activity)
                .permission(android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        createFile(parentFile,type,time);
                        final Uri mUri=FileProvider.getUriForFile(activity,activity.getApplicationInfo().packageName+".fileprovider",mImageFile);
                        toCamera(mUri,activity,flag,type);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
//                        AndPermission.defaultSettingDialog(activity).show();
                        listener.failure();
                    }
                })
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        AndPermission.rationaleDialog(activity,executor)
                                .setNegativeButton(new PermissionHintDialog.ButtonClickListener() {
                                    @Override
                                    public void onClick() {
                                        listener.failure();
                                    }
                                }).show();
                    }
                })
                .start();
        return path;
    }

    /**
     * 生成存储文件
     * @param parentFile
     * @param type
     * @param time
     */
    private  void createFile(File parentFile,String type,long time){
        if(!parentFile.exists()){
            boolean cu=parentFile.mkdirs();
        }
        if(type.equals(MediaStore.ACTION_IMAGE_CAPTURE)){
            mImageFile=new File(parentFile,time+".jpg");
        }else {
            mImageFile=new File(parentFile,time+".mp4");
        }
    }

    private  void toCamera(Uri mUri, Activity activity,int flag,String type){
        Intent intent = new Intent(type);
//        Intent intent=new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(mUri, "image/*");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//		intent.putExtra("crop", "true");//进行修剪
//		intent.putExtra("aspectX", 1);
//		intent.putExtra("aspectY", 1);
//		intent.putExtra("outputX", 150);
//		intent.putExtra("outputY", 150);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
//		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
//		intent.putExtra("return-data", false);

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.N){
            //重要的一步，使用grantUriPermission来给对应的包提升读写指定uri的临时权限。否则即使调用成功，也会保存裁剪照片失败。
            List<ResolveInfo> resInfoList = activity.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
            for (ResolveInfo resolveInfo : resInfoList) {
                String p = resolveInfo.activityInfo.packageName;
                activity.grantUriPermission(p, mUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
        }
        activity.startActivityForResult(intent, flag);
    }

    public interface PermissionFailListener{
        public void failure();
    }

}
