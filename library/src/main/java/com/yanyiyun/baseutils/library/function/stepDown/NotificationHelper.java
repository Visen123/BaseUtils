package com.yanyiyun.baseutils.library.function.stepDown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.yanyiyun.baseutils.library.R;

import static com.yanyiyun.baseutils.library.function.stepDown.SportStepJsonUtils.getCalorieByStep;
import static com.yanyiyun.baseutils.library.function.stepDown.SportStepJsonUtils.getDistanceByStep;

/**
 * 通知帮助类
 */
public class NotificationHelper {

    private static NotificationHelper instance;

    private NotificationManager nm;
    private NotificationApiCompat mNotificationApiCompat;

    /**
     * 点击通知栏广播requestCode
     */
    private static final int BROADCAST_REQUEST_CODE = 100;
    private static final String STEP_CHANNEL_ID = "stepChannelId";

    /**
     * 步数通知ID
     */
    private static final int NOTIFY_ID = 1000;

    private static Service mService;

    /**
     * 是否显示通知
     */
    public static boolean isNofi=false;

    private NotificationHelper() {
    }

    public static NotificationHelper getInstance(){
        if(instance==null){
            synchronized (NotificationHelper.class){
                if(instance==null){
                    instance=new NotificationHelper();
                }
            }
        }
        return instance;
    }

    /**
     * 更新通知
     */
    public synchronized void updateNotification(Context context, Service service,int stepCount) {
        if (null == mNotificationApiCompat) {
            initNotification(context,service,stepCount);
        }else {
            String km = getDistanceByStep(stepCount);
            String calorie = getCalorieByStep(stepCount);
            String contentText = calorie + " 千卡  " + km + " 公里";
            mNotificationApiCompat.updateNotification(NOTIFY_ID,
                    context.getString(R.string.
                            title_notification_bar, String.valueOf(stepCount)), contentText);
        }
    }

    /**
     * 隐藏通知
     */
    public void stopNotification(){
        isNofi=false;
        if(mNotificationApiCompat!=null&&mService!=null){
            mNotificationApiCompat.stopForeground(mService);
        }
    }

    /**
     * 显示通知
     */
    public void showNOtification(Context context,Service service){
        isNofi=true;
        if(mService!=null&&mNotificationApiCompat!=null){
            mNotificationApiCompat.startForeground(mService,NOTIFY_ID);
        }else if(mService==null&&service!=null){
            mService=service;
            initNotification(context,mService,0);
        }
    }


    /**
     * 初始化通知
     * @param context
     * @param service
     * @param currentStep
     */
    public synchronized void initNotification(Context context, Service service,int currentStep) {
        mService=service;

        nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        int smallIcon = context.getResources().getIdentifier("icon_step_small",
                "mipmap", context.getPackageName());
        if (0 == smallIcon) {
            smallIcon = R.mipmap.ic_launcher;
        }
        String receiverName = getReceiver(context.getApplicationContext());
        PendingIntent contentIntent = PendingIntent.getBroadcast(context,
                BROADCAST_REQUEST_CODE, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        if (!TextUtils.isEmpty(receiverName)) {
            try {
                contentIntent = PendingIntent.getBroadcast(context, BROADCAST_REQUEST_CODE,
                        new Intent(context, Class.forName(receiverName)),
                        PendingIntent.FLAG_UPDATE_CURRENT);
            } catch (Exception e) {
                e.printStackTrace();
                contentIntent = PendingIntent.getBroadcast(context, BROADCAST_REQUEST_CODE,
                        new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            }
        }
        String km = getDistanceByStep(currentStep);
        String calorie = getCalorieByStep(currentStep);
        String contentText = calorie + " 千卡  " + km + " 公里";
        int largeIcon = context.getResources().getIdentifier("ic_laun" +
                "cher", "mipmap", context.getPackageName());
        Bitmap largeIconBitmap = null;
        if (0 != largeIcon) {
            largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), largeIcon);
        } else {
            largeIconBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        mNotificationApiCompat = new NotificationApiCompat.Builder(context,
                nm,
                STEP_CHANNEL_ID,
                context.getString(R.string.step_channel_name),
                smallIcon)
                .setContentIntent(contentIntent)
                .setContentText(contentText)
                .setContentTitle(context.getString(R.string.title_notification_bar, String.valueOf(currentStep)))
                .setTicker(context.getString(R.string.app_name))
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_MIN)
                .setLargeIcon(largeIconBitmap)
                .setOnlyAlertOnce(true)
                .builder();
        mNotificationApiCompat.startForeground(mService, NOTIFY_ID);
        mNotificationApiCompat.notify(NOTIFY_ID);
    }

    public static String getReceiver(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), PackageManager.GET_RECEIVERS);
            ActivityInfo[] activityInfos = packageInfo.receivers;
            if (null != activityInfos && activityInfos.length > 0) {
                for (int i = 0; i < activityInfos.length; i++) {
                    String receiverName = activityInfos[i].name;
                    Class superClazz = Class.forName(receiverName).getSuperclass();
                    int count = 1;
                    while (null != superClazz) {
                        if (superClazz.getName().equals("java.lang.Object")) {
                            break;
                        }
                        if (superClazz.getName().equals(BaseClickBroadcast.class.getName())) {
                            return receiverName;
                        }
                        if (count > 20) {
                            //用来做容错，如果20个基类还不到Object直接跳出防止while死循环
                            break;
                        }
                        count++;
                        superClazz = superClazz.getSuperclass();

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
