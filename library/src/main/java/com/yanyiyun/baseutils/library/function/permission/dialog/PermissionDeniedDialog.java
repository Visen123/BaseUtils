package com.yanyiyun.baseutils.library.function.permission.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

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
                .setNegativeButton(context.getResources().getString(R.string.permission_cancel), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                })
                .setPositiveButton(context.getResources().getString(R.string.permission_setting), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                        AndPermission.permissionSetting(context).execute();
                    }
                });
    }
}
