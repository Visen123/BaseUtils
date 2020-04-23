package com.yanyiyun.baseutils.library.function.dialog.product;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.yanyiyun.baseutils.library.R;
import com.yanyiyun.baseutils.library.function.permission.AndPermission;

/**
 * 获取权限失败对话框
 */
public class PermissionDeniedDialog extends PermissionHintDialog {

    public PermissionDeniedDialog(@NonNull final Context context) {
        super(context);
        setTitle(context.getResources().getString(R.string.permission_title_permission_failed))
                .setContent(context.getResources().getString(R.string.permission_message_permission_failed))
                .setNegativeButton(context.getResources().getString(R.string.permission_cancel))
                .setPositiveButton(context.getResources().getString(R.string.permission_setting), new ButtonClickListener() {
                    @Override
                    public void onClick() {
                        AndPermission.permissionSetting(context).execute();
                    }
                });
    }

    public PermissionDeniedDialog(@NonNull final Activity activity, final int requestCode) {
        super(activity);
        setTitle(activity.getResources().getString(R.string.permission_title_permission_failed))
                .setContent(activity.getResources().getString(R.string.permission_message_permission_failed))
                .setNegativeButton(activity.getResources().getString(R.string.permission_cancel))
                .setPositiveButton(activity.getResources().getString(R.string.permission_setting), new ButtonClickListener() {
                    @Override
                    public void onClick() {
                        AndPermission.permissionSetting(activity).execute(requestCode);
                    }
                });
    }
}
