package com.baiqishi.df.demo;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;

import com.bqs.risk.df.android.BqsDF;
import com.bqs.risk.df.android.BqsParams;
import com.bqs.risk.df.android.OnBqsDFContactsListener;
import com.bqs.risk.df.android.OnBqsDFListener;

/**
 * Desction:所有的activity都继承BaseActivity
 * Author:Jianbo.Peng
 * Date:2017/6/1 下午3:12
 */
public class BaseActivity extends AppCompatActivity implements OnBqsDFListener {

    private static final boolean LOG_DEBUG = true;

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * Global.authRuntimePermissions:用于判断运行时权限是否授权
         * BqsDF.canInitBqsSDK()：用于控制不被频繁初始化。如果设备指纹采集成功了，app不被kill的情况下30分钟内不会重复提交设备信息
         */
        if(Global.authRuntimePermissions && BqsDF.canInitBqsSDK()){
            initBqsDFSDK();
        }
    }

    protected void initBqsDFSDK(){
        //1、添加设备信息采集回调checkAuthentication fail
        BqsDF.setOnBqsDFListener(this);
        BqsDF.setOnBqsDFContactsListener(new OnBqsDFContactsListener() {
            @Override
            public void onGatherResult(boolean gatherStatus) {
                printLog("通讯录采集状态 gatherStatus=" + gatherStatus);
            }

            @Override
            public void onSuccess(String tokenKey) {
                printLog("通讯录采集成功");
            }

            @Override
            public void onFailure(String resultCode, String resultDesc) {
                printLog("通讯录采集失败 resultCode=" + resultCode + " resultDesc=" + resultDesc);
            }
        });
        //BqsDF.setOnBqsDFCallRecordListener(...);

        //2、配置初始化参数
        BqsParams params = new BqsParams();
        params.setPartnerId("8d1ac2011a7d4485ad151d892ce9549a");//商户编号
        params.setTestingEnv(false);//false是生产环境 true是测试环境
        params.setGatherGps(true);
        params.setGatherContact(true);
        params.setGatherCallRecord(true);

        //3、执行初始化
        BqsDF.initialize(this, params);
        //采集通讯录,第一个参数：是否采集通讯录，第二个参数：是否采集通话记录
        BqsDF.commitContactsAndCallRecords(false, false);
        BqsDF.commitLocation();
        //BqsDF.commitLocation(longitude, latitude);

        //4、提交tokenkey到贵公司服务器
        String tokenkey = BqsDF.getTokenKey();

        //注意：上传tokenkey时最好再停留几百毫秒的时间（给SDK预留上传设备信息的时间）
        new CountDownTimer(500, 500){
            @Override
            public void onTick(long l) {}

            @Override
            public void onFinish() {
                submitTokenkey();
            }
        }.start();
    }

    //#mark - OnBqsDFListener
    //设备采集成功
    @Override
    public void onSuccess(String tokenKey) {
        //回调的tokenkey和通过BqsDF.getTokenKey()拿到的值都是一样的
        printLog("白骑士SDK采集设备信息成功 tokenkey=" + tokenKey);
    }

    //设备采集失败
    @Override
    public void onFailure(String resultCode, String resultDesc) {
        printLog("白骑士SDK采集设备信息失败 resultCode=" + resultCode + " resultDesc=" + resultDesc);
    }

    /**
     * 提交tokenkey到贵公司服务器
     */
    private void submitTokenkey(){
        String tokenkey = BqsDF.getTokenKey();

        printLog("提交tokenkey:" +  tokenkey);
        //发起Http请求
        //....

    }

    private void printLog(String log){
        if(LOG_DEBUG) {
            System.out.println(log);
        }
    }
}
