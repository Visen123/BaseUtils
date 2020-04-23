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

import android.support.annotation.NonNull;


/**
 * <p>Permission request.</p>
 * Created by Yan Zhenjie on 2016/9/9.
 */
public interface Request {

    /**
     * 需要请求的权限
     * One or more permissions.
     */
    @NonNull
    Request permission(String... permissions);

    /**
     * 需要请求的权限
     * One or more permission groups.
     */
    @NonNull
    Request permission(String[]... groups);

    /**
     * 拦截器，当用户不给权限时优先执行，用户给权限就不会执行
     * Set request rationale.
     */
    @NonNull
    Request rationale(Rationale listener);

    /**
     * 用户已经给了权限
     * Action to be taken when all permissions are granted.
     */
    @NonNull
    Request onGranted(Action granted);

    /**
     * 用户不给权限，包括用户选择拒绝和用户点了不再提示2种情况
     * Action to be taken when all permissions are denied.
     */
    @NonNull
    Request onDenied(Action denied);

    /**
     * Request permission.
     */
    void start();

}