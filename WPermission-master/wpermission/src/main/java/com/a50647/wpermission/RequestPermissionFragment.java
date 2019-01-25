package com.a50647.wpermission;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.SparseArray;

/**
 * @author wm
 * @date 2018/1/4
 */

public final class RequestPermissionFragment extends Fragment {
    private SparseArray<RequestPermissionActivity.Callback> mCallbacks = new SparseArray<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public void requestPermissions(int requestCode, @NonNull String[] permissions, RequestPermissionActivity.Callback callback) {
        mCallbacks.put(requestCode, callback);
        requestPermissions(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        RequestPermissionActivity.Callback callback = mCallbacks.get(requestCode);
        if (callback != null) {
            callback.onRequestPermissionResult(requestCode, permissions, grantResults);
        }
        mCallbacks.remove(requestCode);
    }
}
