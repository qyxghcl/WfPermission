package com.wpermission_master;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.a50647.wpermission.PermissionManager;
import com.a50647.wpermission.Permissions;

/**
 * 演示页
 *
 * @author wm
 * @date 2019/1/25
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PermissionManager.getDefault().requestPermission(this
                , new PermissionManager.OnRequestPermissionListener() {
                    @Override
                    public void onGranted() {
                        Toast.makeText(MainActivity.this, "权限通过", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDeny(boolean isSystemShow) {
                        if (isSystemShow){
                            Toast.makeText(MainActivity.this, "权限被拒绝,但是没有选择不再询问" +
                                    ",下次重新请求依旧会有系统弹窗", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(MainActivity.this, "" +
                                    "权限被拒绝,选择不再询问,下次重新请求不会有系统弹窗", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, Permissions.STORAGE);
    }
}
