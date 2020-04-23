package com.yanyiyun.baseutils.library.tool;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.util.Locale;

public class ApplicationUtil {

    /**
     * 获得版本号
     * @param context
     * @return
     */
    public static int getLocalVersionCode(Context context){
        int versionCode=1;
        try {
            versionCode = context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获得版本名称
     * @param context
     * @return
     */
    public static String getLocalVersionName(Context context){
        String versionName = "1.0";

        try {
            versionName = context.getPackageManager().getPackageInfo(getPackageName(context), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionName;
    }

    /**
     * 获得包名
     * @param context
     * @return
     */
    public static String getPackageName(Context context){
        PackageInfo info = null;
        try {
            info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.packageName;
    }

    /**
     * 安装apk
     * @param fileName
     */
    public static void installAPK(Context context,String fileName) {
        File file = new File(fileName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // 由于没有在Activity环境下启动Activity,设置下面的标签
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) { //判读版本是否在7.0以上
            //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
            Uri apkUri = FileProvider.getUriForFile(context, getPackageName(context)+".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        context.startActivity(intent);
    }

    /**
     * 获取当前应用语言
     */
    public static Locale getCurrentLanguage() {
        return Locale.getDefault();
    }
}
