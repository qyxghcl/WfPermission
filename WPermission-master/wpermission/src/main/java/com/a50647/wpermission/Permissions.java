package com.a50647.wpermission;

import android.Manifest;

/**
 * 常用权限常量
 *
 * @author wm
 * @date 2018/11/26
 */

public final class Permissions {
    /**
     * 存储相关
     */
    public static final String[] STORAGE = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * 相机
     */
    public static final String[] CAMERA = new String[]{
            Manifest.permission.CAMERA
    };

    /**
     * 位置
     */
    public static final String[] LOCATION = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
}
