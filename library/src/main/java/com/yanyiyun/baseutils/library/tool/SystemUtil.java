package com.yanyiyun.baseutils.library.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.yanyiyun.baseutils.library.BuildConfig;
import com.yanyiyun.baseutils.library.function.dialog.product.PermissionHintDialog;
import com.yanyiyun.baseutils.library.function.permission.Action;
import com.yanyiyun.baseutils.library.function.permission.AndPermission;
import com.yanyiyun.baseutils.library.function.permission.Rationale;
import com.yanyiyun.baseutils.library.function.permission.RequestExecutor;

import java.io.File;
import java.util.List;

/**
 * 系统相关工具类
 * Created by Administrator on 2018/11/16.
 */

public class SystemUtil {

    public static final int REQ_CODE_CONTACT = 123;

    @SuppressLint("StaticFieldLeak")
    private static SystemUtil systemUtil;

    private SystemUtil(Context context) {
        mContext =context;
    }

    public static SystemUtil getInstance(Context context) {
        if (systemUtil == null) {
            synchronized (SystemUtil.class) {
                if (systemUtil == null) {
                    systemUtil = new SystemUtil(context);
                }
            }
        }
        return systemUtil;
    }

    private Context mContext;

    /**
     * 跳转到系统的设置界面
     */
    public void toSetting() {
        final Intent intent = new Intent(Settings.ACTION_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 获取系统版本号
     * @param context
     * @return
     */
    public String getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode = "";
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode + "";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;

    }

    /**
     * 跳转到系统的wifi设置界面
     */
    public void toWIFI() {
        final Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的移动网络设置界面
     */
    public void toMobileNet() {
        final Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的飞行模式设置界面
     */
    public void toAirplane() {
        final Intent intent = new Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的蓝牙设置界面
     */
    public void toBluetooth() {
        final Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的NFC设置界面
     */
    public void toNFC() {
        final Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的NFC共享设置界面
     */
    public void toNFCShare() {
        final Intent intent = new Intent(Settings.ACTION_NFCSHARING_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的GPS设置界面
     */
    public void toGPS() {
        final Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(intent);
    }

    /**
     * 跳转到系统的拨号界面
     */
    public void toDialBoard(String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        mContext.startActivity(intent);
    }

    /**
     * 拨打电话
     */
    public void dialNow(String phoneNumber) {
        Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + phoneNumber));
        mContext.startActivity(intent);
    }

    /**
     * 跳转至发送短信的界面
     */
    public void toSendMsg(String phoneNumber, String content) {
        Uri uri = Uri.parse("smsto:" + phoneNumber);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", content);
        mContext.startActivity(intent);
    }

    /**
     * 发送短信
     */
    public void sendMsg(String phoneNumber, String content) {
        if (TextUtils.isEmpty(content))
            return;
        PendingIntent sentIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(), 0);
        SmsManager smsManager = SmsManager.getDefault();
        if (content.length() >= 70) {
            List<String> ms = smsManager.divideMessage(content);
            for (String str : ms) {
                smsManager.sendTextMessage(phoneNumber, null, str, sentIntent, null);
            }
        } else {
            smsManager.sendTextMessage(phoneNumber, null, content, sentIntent, null);
        }
    }

    /**
     * 跳转到联系人界面
     */
    public void toContact(Activity activity) {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.PICK");
        intent.setType("vnd.android.cursor.dir/phone_v2");
        activity.startActivityForResult(intent, REQ_CODE_CONTACT);
    }

    /**
     * 获取手机状态信息
     * <p>需添加权限 </p>
     *
     * @return DeviceId(IMEI) = 99000311726612<br>
     * DeviceSoftwareVersion = 00<br>
     * Line1Number =<br>
     * NetworkCountryIso = cn<br>
     * NetworkOperator = 46003<br>
     * NetworkOperatorName = 中国电信<br>
     * NetworkType = 6<br>
     * honeType = 2<br>
     * SimCountryIso = cn<br>
     * SimOperator = 46003<br>
     * SimOperatorName = 中国电信<br>
     * SimSerialNumber = 89860315045710604022<br>
     * SimState = 5<br>
     * SubscriberId(IMSI) = 460030419724900<br>
     * VoiceMailNumber = *86<br>
     */
    @SuppressLint({"HardwareIds", "MissingPermission"})
    public String getPhoneStatus() {
        TelephonyManager tm = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        String str = "";
        str += "DeviceId(IMEI) = " + tm.getDeviceId() + "\n";
        str += "DeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion() + "\n";
        str += "Line1Number = " + tm.getLine1Number() + "\n";
        str += "NetworkCountryIso = " + tm.getNetworkCountryIso() + "\n";
        str += "NetworkOperator = " + tm.getNetworkOperator() + "\n";
        str += "NetworkOperatorName = " + tm.getNetworkOperatorName() + "\n";
        str += "NetworkType = " + tm.getNetworkType() + "\n";
        str += "PhoneType = " + tm.getPhoneType() + "\n";
        str += "SimCountryIso = " + tm.getSimCountryIso() + "\n";
        str += "SimOperator = " + tm.getSimOperator() + "\n";
        str += "SimOperatorName = " + tm.getSimOperatorName() + "\n";
        str += "SimSerialNumber = " + tm.getSimSerialNumber() + "\n";
        str += "SimState = " + tm.getSimState() + "\n";
        str += "SubscriberId(IMSI) = " + tm.getSubscriberId() + "\n";
        str += "VoiceMailNumber = " + tm.getVoiceMailNumber() + "\n";
        return str;
    }

    /**
     * 获取SIM卡运营商名称
     * <p>中国移动、中国联通、中国电信</p>
     *
     * @return
     */
    public String getSIMName() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getSimOperatorName() : null;
    }

    /**
     * 判断SIM卡是否准备好
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isSIMReady() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getSimState() == TelephonyManager.SIM_STATE_READY;
    }

    /**
     * 获取移动终端类型
     *
     * @return 手机制式
     * <ul>
     * <li>{@link TelephonyManager#PHONE_TYPE_NONE } : 0 手机制式未知</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_GSM  } : 1 手机制式为 GSM，移动和联通</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_CDMA } : 2 手机制式为 CDMA，电信</li>
     * <li>{@link TelephonyManager#PHONE_TYPE_SIP  } : 3</li>
     * </ul>
     */
    public int getPhoneType() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null ? tm.getPhoneType() : -1;
    }

    /**
     * 判断设备是否是手机
     *
     * @return {@code true}: 是<br>{@code false}: 否
     */
    public boolean isPhone() {
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return tm != null && tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    public void openFile(String file){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            getPermission(new String[]{Manifest.permission.INSTALL_PACKAGES,Manifest.permission.READ_EXTERNAL_STORAGE},file);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            getPermission(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},file);
        }else {
            installApk(file);
        }
    }

    private void getPermission(String[] permissions, final String file){
        AndPermission.with(mContext)
                .permission(permissions)
                .onGranted(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        installApk(file);
                    }
                })
                .onDenied(new Action() {
                    @Override
                    public void onAction(List<String> permissions) {
                        Toast.makeText(mContext,"获取安装apk权限失败",Toast.LENGTH_SHORT).show();
                    }
                })
                .rationale(new Rationale() {
                    @Override
                    public void showRationale(Context context, List<String> permissions, RequestExecutor executor) {
                        AndPermission.rationaleDialog(mContext,executor)
                                .setNegativeButton( new PermissionHintDialog.ButtonClickListener() {
                                    @Override
                                    public void onClick() {
                                        Toast.makeText(mContext,"获取安装apk权限失败",Toast.LENGTH_SHORT).show();
                                    }
                                }).show();
                    }
                })
                .start();
    }

    /**
     * 安装apk
     *
     * @param //url
     */
    private void installApk(String file) {
        File apkfile = new File(file);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);

        //判断是否是AndroidN以及更高的版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(mContext, BuildConfig.APPLICATION_ID + ".fileprovider", apkfile);
            i.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            i.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
//		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//				"application/vnd.android.package-archive");
        mContext.startActivity(i);
    }
}
