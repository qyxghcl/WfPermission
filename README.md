# WfPermission
A tool for permission requests

## How to use
### Step1.
```
allprojects {
    repositories {
        google()
        jcenter()
        maven {
            url 'https://jitpack.io'
        }
    }
}
```

### Step2.
```
implementation 'com.github.qyxghcl:WPermission:v1.0'
```

### Step3.
```
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
```

## document
[document](https://www.jianshu.com/p/080b3128e564)
