package com.a50647.wpermission;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 权限管理类
 *
 * @author wm
 * @date 2018/11/26
 */

public final class PermissionManager {
    private static final int REQ_PER_CODE = 9527;
    private static volatile PermissionManager defaultInstance;
    private WeakReference<Activity> mWeakActivity;

    private PermissionManager() {
    }

    /**
     * 实例化
     *
     * @return PermissionManager实例
     */
    public static PermissionManager getDefault() {
        if (defaultInstance == null) {
            synchronized (PermissionManager.class) {
                if (defaultInstance == null) {
                    defaultInstance = new PermissionManager();
                }
            }
        }
        return defaultInstance;
    }

    /**
     * 请求权限的方法
     *
     * @param activity    需要权限的activity
     * @param listener    权限的回调
     * @param permissions 所请求的权限
     */
    public void requestPermission(FragmentActivity activity, final OnRequestPermissionListener listener, String... permissions) {
        mWeakActivity = new WeakReference<Activity>(activity);
        //如果小于6.0则表示具有所有的权限
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            listener.onGranted();
            return;
        }
        //是否具有所有的权限
        if (hasAllPermissions(activity, permissions)) {
            listener.onGranted();
            return;
        }
        //筛选出有没通过的权限
        String[] unPassPermissions = screenOutUnPassedPermissions(activity, permissions);
        if (unPassPermissions.length == 0) {
            listener.onGranted();
            return;
        }
        //开始询问
        new RequestPermissionActivity(activity).requestPermissions(REQ_PER_CODE, unPassPermissions, new RequestPermissionActivity.Callback() {
            @Override
            public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
                //找到所有被拒绝的权限
                List<String> denied = new ArrayList<>();
                for (int i = 0; i < permissions.length; i++) {
                    String perm = permissions[i];
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        denied.add(perm);
                    }
                }
                //被拒绝的权限为0,表示通过
                if (denied.size() == 0) {
                    listener.onGranted();
                    return;
                }
                //此处剩余的都是未通过的权限,
                //shouldShowRequestPermissionRationale(activity, denied)
                //(1)false 代表勾选了不再询问,则需要提示用户去自主开启,即用户此次想执行业务逻辑是不准许的
                // ,并且下次请求执行的时候,不会弹出系统的选择框
                //(2)true 代表拒绝,但未勾选不再询问,即用户此次想执行业务逻辑是不准许的,但下次请求执行的时候
                // ,仍旧会弹出系统的选择框,让客户选择此次是否给于通过
                Activity weakActivity = mWeakActivity.get();
                if (weakActivity != null) {
                    if (!shouldShowRequestPermissionRationale(weakActivity, denied)) {
                        listener.onDeny(false);
                    } else {
                        listener.onDeny(true);
                    }
                }
            }
        });
    }

    /**
     * 权限是否全部通过
     *
     * @param context     context
     * @param permissions 请求权限
     * @return true全部通过 false并不是全部通过
     */
    private boolean hasAllPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (isUnPassedPermission(context, permission)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 是否是未通过权限
     *
     * @param context    context
     * @param permission 所被检测的权限
     * @return true 是未通过权限 false 不是未通过权限
     */
    private boolean isUnPassedPermission(Context context, String permission) {
        try {
            return ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED;
        } catch (RuntimeException t) {
            return true;
        }
    }

    /**
     * 用于调用系统请求权限之前,将已经具有的权限排除,只保留当前还没有具有的权限
     *
     * @param activity    请求权限的activity
     * @param permissions 需要被筛选的权限
     * @return String[]类型 当前未通过的权限
     */
    private String[] screenOutUnPassedPermissions(Activity activity, String[] permissions) {
        List<String> stringList = new ArrayList<>();
        for (String permission : permissions) {
            if (isUnPassedPermission(activity, permission)) {
                stringList.add(permission);
            }
        }
        String[] strings = new String[stringList.size()];
        for (int i = 0, j = stringList.size(); i < j; i++) {
            strings[i] = stringList.get(i);
        }
        return strings;
    }

    /**
     * 用于RequestPermissionResult得到结果之后的处理
     *
     * @param activity    接收请求结果的activity
     * @param permissions 用于请求的permissions
     * @return (1)上次禁止, 但未勾选不在询问 返回true (2)上次禁止,勾选不在询问 返回false
     */
    private boolean shouldShowRequestPermissionRationale(Activity activity, List<String> permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    public interface OnRequestPermissionListener {
        /**
         * 权限通过
         */
        void onGranted();

        /**
         * 权限拒绝
         *
         * @param isSystemShow 是否会有系统对话框显示
         */
        void onDeny(boolean isSystemShow);
    }
}
