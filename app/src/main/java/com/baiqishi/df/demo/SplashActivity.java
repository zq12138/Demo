package com.baiqishi.df.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bqs.risk.df.android.BqsDF;

/**
 * Desction:启动页-权限授权
 * Author:Jianbo.Peng
 * Date:2017/6/1 下午3:13
 */
public class SplashActivity extends BaseActivity{
    
    //获取6.0运行时权限列表，第一个参数：是否授权gps，第二个参数：是否授权通讯录，第三个参数：是否授权通话记录
    String[] requestPermissions = BqsDF.getRuntimePermissions(true, true, true);
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        
        PermissionUtils.requestMultiPermissions(this, requestPermissions, mPermissionGrant);

        
    }
    
    /**
     * 授权结果，该回调不管权限是拒绝还是同意都会进入该回调方法
     */
    private PermissionUtils.PermissionGrant mPermissionGrant = new PermissionUtils.PermissionGrant() {
        @Override
        public void onPermissionGranted(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults, String[] requestPermissions) {
            Global.authRuntimePermissions = true;
    
            /**
             * 因为在启动页进行运行时权限授权，所以要在授权结果回调中出发一次初始化
             */
            initBqsDFSDK();
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtils.requestPermissionsResult(requestCode, requestPermissions, grantResults, requestPermissions, mPermissionGrant);
    }
}
