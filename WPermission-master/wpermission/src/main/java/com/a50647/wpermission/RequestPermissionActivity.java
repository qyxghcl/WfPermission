package com.a50647.wpermission;


import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * @author wm
 * @date 2018/1/4
 */

public final class RequestPermissionActivity {
    private final static String TAG = "CHECK_PERMISSION";

    private Lazy<RequestPermissionFragment> mActivityResultFragment;

    /**
     * 构造方法
     *
     * @param activity 需要请求权限的activity
     */
    RequestPermissionActivity(FragmentActivity activity) {
        mActivityResultFragment = getLazySingleton((activity.getSupportFragmentManager()));
    }

    /**
     * 获取实际用于权限请求的fragment的单例
     *
     * @param fragmentManager v4包下的fragmentManager
     * @return 实际用于权限请求的fragment的单例
     */
    private Lazy<RequestPermissionFragment> getLazySingleton(final FragmentManager fragmentManager) {
        return new Lazy<RequestPermissionFragment>() {
            private RequestPermissionFragment mRequestPermissionFragment;

            @Override
            public synchronized RequestPermissionFragment get() {
                if (mRequestPermissionFragment == null) {
                    mRequestPermissionFragment = getResultFragment(fragmentManager);
                }
                return mRequestPermissionFragment;
            }
        };
    }

    /**
     * 获取tag的fragment
     *
     * @param fragmentManager fragmentManager
     * @return 标记tag的fragment
     */
    private RequestPermissionFragment getResultFragment(FragmentManager fragmentManager) {
        RequestPermissionFragment resultFragment = (RequestPermissionFragment) fragmentManager
                .findFragmentByTag(TAG);
        boolean isNewInstance = resultFragment == null;
        if (isNewInstance) {
            resultFragment = new RequestPermissionFragment();
            fragmentManager
                    .beginTransaction()
                    .add(resultFragment, TAG)
                    .commitNow();
        }
        return resultFragment;
    }

    public void requestPermissions(int requestCode, @NonNull String[] permissions, Callback callback) {
        mActivityResultFragment.get().requestPermissions(requestCode, permissions, callback);
    }

    public interface Callback {
        /**
         * 请求权限返回结果
         *
         * @param requestCode  请求码
         * @param permissions  请求权限
         * @param grantResults 请求结果
         */
        void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
    }

    @FunctionalInterface
    public interface Lazy<V> {
        /**
         * 单例
         *
         * @return 实际用于权限请求的fragment的单例
         */
        V get();
    }
}
