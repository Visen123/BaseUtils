/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yanyiyun.baseutils.library.function.permission;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.yanyiyun.baseutils.library.function.dialog.product.PermissionDeniedDialog;
import com.yanyiyun.baseutils.library.function.dialog.product.PermissionRationaleDialog;
import com.yanyiyun.baseutils.library.function.permission.setting.PermissionSetting;
import com.yanyiyun.baseutils.library.function.permission.source.AppActivitySource;
import com.yanyiyun.baseutils.library.function.permission.source.ContextSource;
import com.yanyiyun.baseutils.library.function.permission.source.FragmentSource;
import com.yanyiyun.baseutils.library.function.permission.source.Source;
import com.yanyiyun.baseutils.library.function.permission.source.SupportFragmentSource;

import java.util.List;

/**
 * <p>Request the entrance.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
public class AndPermission {

    private static final RequestFactory FACTORY;

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            FACTORY = new MRequestFactory();
        } else {
            FACTORY = new LRequestFactory();
        }
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Activity activity,
            @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull List<String> deniedPermissions) {
        return hasAlwaysDeniedPermission(new ContextSource(context), deniedPermissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean hasAlwaysDeniedPermission(
            @NonNull Source source,
            @NonNull List<String> deniedPermissions) {
        for (String permission : deniedPermissions) {
            if (!source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param activity          {@link Activity}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Activity activity,
            @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new AppActivitySource(activity), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.support.v4.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.support.v4.app.Fragment fragment,
            @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new SupportFragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param fragment          {@link android.app.Fragment}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull android.app.Fragment fragment,
            @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new FragmentSource(fragment), deniedPermissions);
    }

    /**
     * Some privileges permanently disabled, may need to set up in the execute.
     *
     * @param context           {@link Context}.
     * @param deniedPermissions one or more permissions.
     * @return true, other wise is false.
     */
    public static boolean hasAlwaysDeniedPermission(
            @NonNull Context context,
            @NonNull String... deniedPermissions) {
        return hasAlwaysDeniedPermission(new ContextSource(context), deniedPermissions);
    }

    /**
     * Has always been denied permission.
     */
    private static boolean hasAlwaysDeniedPermission(
            @NonNull Source source,
            @NonNull String... deniedPermissions) {
        for (String permission : deniedPermissions) {
            if (!source.isShowRationalePermission(permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param activity {@link Activity}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService permissionSetting(@NonNull Activity activity) {
        return new PermissionSetting(new AppActivitySource(activity));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService permissionSetting(@NonNull android.support.v4.app.Fragment fragment) {
        return new PermissionSetting(new SupportFragmentSource(fragment));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService permissionSetting(@NonNull android.app.Fragment fragment) {
        return new PermissionSetting(new FragmentSource(fragment));
    }

    /**
     * Create a service that opens the permission setting page.
     *
     * @param context {@link android.app.Fragment}.
     * @return {@link SettingService}.
     */
    @NonNull
    public static SettingService permissionSetting(@NonNull Context context) {
        return new PermissionSetting(new ContextSource(context));
    }

    /**
     * With Activity.
     *
     * @param activity {@link Activity}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull Activity activity) {
        return FACTORY.create(new AppActivitySource(activity));
    }

    /**
     * With android.support.v4.app.Fragment.
     *
     * @param fragment {@link android.support.v4.app.Fragment}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull android.support.v4.app.Fragment fragment) {
        return FACTORY.create(new SupportFragmentSource(fragment));
    }

    /**
     * With android.app.Fragment.
     *
     * @param fragment {@link android.app.Fragment}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull android.app.Fragment fragment) {
        return FACTORY.create(new FragmentSource(fragment));
    }

    /**
     * With context.
     *
     * @param context {@link Context}.
     * @return {@link Request}.
     */
    @NonNull
    public static Request with(@NonNull Context context) {
        return FACTORY.create(new ContextSource(context));
    }

    /**
     * 默认的跳转设置对话框
     * @param context
     * @return
     */
    public static PermissionDeniedDialog defaultSettingDialog(Context context){
        return new PermissionDeniedDialog(context);
    }

    public static PermissionDeniedDialog defaultSettingDialog(Activity activity,int requestCode){
        return new PermissionDeniedDialog(activity,requestCode);
    }

    /**
     * 权限拒绝提示对话框
     * @param context
     * @param executor
     * @return
     */
    public static PermissionRationaleDialog rationaleDialog(Context context,RequestExecutor executor){
        return new PermissionRationaleDialog(context,executor);
    }

    private AndPermission() {
    }

    private interface RequestFactory {
        /**
         * Create permission request.
         */
        Request create(Source source);
    }

    private static class LRequestFactory implements RequestFactory {
        @Override
        public Request create(Source source) {
            return new LRequest(source);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private static class MRequestFactory implements RequestFactory {
        @Override
        public Request create(Source source) {
            return new MRequest(source);
        }
    }

}