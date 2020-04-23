package com.yanyiyun.function.dialog.product;

import android.content.Context;
import android.support.annotation.NonNull;

import com.yanyiyun.R;
import com.yanyiyun.function.permission.RequestExecutor;

public class PermissionRationaleDialog extends PermissionHintDialog {

    public PermissionRationaleDialog(@NonNull Context context, final RequestExecutor executor) {
        super(context);
        setTitle(context.getResources().getString(R.string.permission_title_permission_rationale))
                .setContent(context.getResources().getString(R.string.permission_message_permission_rationale))
                .setNegativeButton(context.getResources().getString(R.string.permission_cancel))
                .setPositiveButton(context.getResources().getString(R.string.permission_resume), new ButtonClickListener() {
                    @Override
                    public void onClick() {
                        executor.execute();
                    }
                });

    }
}
