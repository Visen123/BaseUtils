package com.yanyiyun.baseutils.library.function.stepDown;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yanyiyun.baseutils.library.function.stepDown.interfaces.IStepDBHelper;
import com.yanyiyun.baseutils.library.function.stepDown.interfaces.IStepValuePassListener;
import com.yanyiyun.baseutils.library.function.stepDown.interfaces.IUpdateUiCallBack;
import com.yanyiyun.baseutils.library.tool.DateUtils;
import com.yanyiyun.baseutils.library.tool.log;

public class BaseToolStepService extends Service implements SensorEventListener {

    public static final String notification="notification";

    /**
     * binder服务与activity交互桥梁
     */
    private LcBinder lcBinder = new LcBinder();
    /**
     * 当前步数
     */
    private int nowStepCount=0;
    /**
     * 传感器对象
     */
    private SensorManager sensorManager;
    /**
     * 计步传感器类型  Sensor.TYPE_STEP_COUNTER或者Sensor.TYPE_STEP_DETECTOR
     */
    private static int stepSensorType = -1;
    /**
     * 加速度传感器中获取的步数
     */
    private StepCount mStepCount;
    /**
     * 数据回调接口，通知上层调用者数据刷新
     */
    private IUpdateUiCallBack mCallback;
    /**
     * 每次第一次启动记步服务时是否从系统中获取了已有的步数记录
     */
    private boolean hasRecord = false;
    /**
     * 系统中获取到的已有的步数
     */
    private int hasStepCount = 0;
    /**
     * 上一次的步数
     */
    private int previousStepCount = 0;

    private IStepDBHelper stepDBHelper;

    private BroadcastReceiver mInfoReceiver;

    @Override
    public void onCreate() {
        super.onCreate();
        stepDBHelper=StepDBHelper.factory(getApplicationContext());
        initBroadcastReceiver();
        new Thread(new Runnable() {
            @Override
            public void run() {
                startStepDetector();
            }
        }).start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        NotificationHelper.getInstance().initNotification(this,this,nowStepCount);
        if(!NotificationHelper.isNofi){
           NotificationHelper.getInstance().stopNotification();
        }
        return lcBinder;
    }

    /**
     * 选择计步数据采集的传感器
     * SDK大于等于19，开启计步传感器，小于开启加速度传感器
     */
    private void startStepDetector(){
        if(sensorManager!=null){
            sensorManager=null;
        }
        sensorManager= (SensorManager) getSystemService(SENSOR_SERVICE);
        int versionCodes=Build.VERSION.SDK_INT;
        if(versionCodes>=19){
            //SDK版本大于等于19开启计步传感器
            addCountStepListener();
        }else {
            //小于就使用加速度传感器
            addBasePedometerListener();
        }
    }

    /**
     * 启动计步传感器计步
     */
    private void addCountStepListener(){
        Sensor countSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        Sensor detectorSensor=sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(countSensor!=null){
            stepSensorType=Sensor.TYPE_STEP_COUNTER;
            sensorManager.registerListener(BaseToolStepService.this,
                    countSensor,SensorManager.SENSOR_DELAY_NORMAL);
//        } else if(detectorSensor!=null){
//            stepSensorType=Sensor.TYPE_STEP_DETECTOR;
//            sensorManager.registerListener(StepService.this,detectorSensor,
//                    SensorManager.SENSOR_DELAY_NORMAL);
        }else {  //两种记步传感器都没有则启动加速度传感器计步
            addBasePedometerListener();
        }
    }

    /**
     * 启动加速度传感器计步
     */
    private void addBasePedometerListener(){
        mStepCount=new StepCount();
        long time=System.currentTimeMillis();
        String currrentDay=DateUtils.dateFormat(time,"yyyy-MM-dd");
        StepData front=PreferencesHelper.getLastSensorStep(getApplicationContext());
        if(currrentDay.equals(front.getToday())){  //本地保存最近的数据是当天 设置当天本地保存的步数
            nowStepCount= (int) front.getStep();
        }else {  //不是当天起始步数为0
            nowStepCount=0;
        }
        mStepCount.setSteps(nowStepCount);
        Sensor sensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        boolean isAvailable=sensorManager.registerListener(mStepCount.getStepDetector(),
                sensor,SensorManager.SENSOR_DELAY_UI);
        mStepCount.initListener(new IStepValuePassListener() {
            @Override
            public void stepChanged(int steps) {
                long time=System.currentTimeMillis();
                String currrentDay=DateUtils.dateFormat(time,"yyyy-MM-dd");
                nowStepCount=steps;
                StepData stepData=new StepData();
                stepData.setToday(currrentDay);
                stepData.setStep(nowStepCount);
                stepData.setLastSenorStep(-1);
                stepData.setDate(time);
                PreferencesHelper.saveLastSensorStep(getApplicationContext(),stepData);
                updateNotification();
            }
        });
    }

    /**
     * 通知调用者步数更新 数据交互
     */
    private void updateNotification() {
        log.e( "更新步数nowStepCount:"+nowStepCount);
        if (mCallback != null) {
            mCallback.updateUi(nowStepCount);
        }
        if(NotificationHelper.isNofi){
            NotificationHelper.getInstance().updateNotification(this,this,nowStepCount);
        }
    }

    /**
     * 计步传感器数据变化回调接口
     * @param event
     */
    @Override
    public void onSensorChanged(SensorEvent event) {
        String name=PreferencesHelper.getName(this);
       if(name!=null&&!name.isEmpty()){
           if(stepSensorType==Sensor.TYPE_STEP_COUNTER){  //返回一段时间步数
               //获取当前传感器返回的临时步数
               int tempStep= (int) event.values[0];
               log.e("传感器返回的步数："+tempStep);

               long time=System.currentTimeMillis();
               String currrentDay=DateUtils.dateFormat(time,"yyyy-MM-dd");
               StepData stepDataNewDay=new StepData();
               stepDataNewDay.setDate(time);
               stepDataNewDay.setToday(currrentDay);
               stepDataNewDay.setLastSenorStep(tempStep);

               StepData front=PreferencesHelper.getLastSensorStep(getApplicationContext());
               if(front!=null){  //APP不是第一次安装
                   log.e("本地保存的step:"+front.getStep()+",LastSenorStep:"+front.getLastSenorStep());
                   if(front.getToday().equals(currrentDay)){  //最后一次回调是当天
                       if(front.getLastSenorStep()!=-1){
                           if(PreferencesHelper.getLoginFirstGetStep(this)){  //登录后第一次获取到步数
                               nowStepCount= (int) front.getStep();
                               log.e("登出后第一次获取步数,nowStepCount:"+nowStepCount);
                           }else {
                               if(front.getLastSenorStep()>tempStep){ //机子被重启
                                   log.e("重启后第一次获取步数,nowStepCount:"+nowStepCount);
                                   nowStepCount= (int) (front.getStep()+tempStep);
                               }  else {
                                   log.e("正常的步数回调,nowStepCount:"+nowStepCount);
                                   nowStepCount= (int) (front.getStep()+tempStep-front.getLastSenorStep());
                               }
                           }
                       }else {
                           nowStepCount=DBConstants.ERROR;
                       }
                   }else {  //最后一次回调不是当天
                       nowStepCount=DBConstants.ERROR;
                   }
               }else {  //APP第一次安装
                   nowStepCount=DBConstants.ERROR;
               }
               stepDataNewDay.setStep(nowStepCount);
               PreferencesHelper.saveLastSensorStep(getApplicationContext(),stepDataNewDay);
               PreferencesHelper.setLoginFirstGetStep(this,false);
//            }
           } //else if(stepSensorType==Sensor.TYPE_STEP_DETECTOR){  //返回单个步伐变化
//            if (event.values[0] == 1.0) {
//                nowStepCount++;
//            }
//        }

           updateNotification();
       }
    }


    /**
     * 计步传感器精度变化回调接口
     * @param sensor
     * @param accuracy
     */
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 数据传递接口
     *
     * @param paramICallback
     */
    public void registerCallback(IUpdateUiCallBack paramICallback) {
        this.mCallback = paramICallback;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //返回START_STICKY ：在运行onStartCommand后service进程被kill后，那将保留在开始状态，但是不保留那些传入的intent。
        // 不久后service就会再次尝试重新创建，因为保留在开始状态，在创建     service后将保证调用onstartCommand。
        // 如果没有传递任何开始命令给service，那将获取到null的intent。
        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消前台进程
        stopForeground(true);
        unregisterReceiver(mInfoReceiver);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    /**
     * 绑定回调接口
     */
    public class LcBinder extends Binder {
        public BaseToolStepService getService() {
            return BaseToolStepService.this;
        }
    }

    private void initBroadcastReceiver(){
        IntentFilter filter=new IntentFilter();

        //监听日期变化
        filter.addAction(Intent.ACTION_DATE_CHANGED);  //日期发生变化调用
        filter.addAction(Intent.ACTION_TIME_CHANGED); //在设置中修改调用
        filter.addAction(Intent.ACTION_TIME_TICK);  //每分钟调用一次

        mInfoReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action=intent.getAction();
                //监听日期变化
                if(action.equals(Intent.ACTION_DATE_CHANGED)||
                        action.equals(Intent.ACTION_TIME_CHANGED)||
                        action.equals(Intent.ACTION_TIME_TICK)){
                    if("00:00".equals(DateUtils.getCurrentDate("HH:mm"))){
                    }
                }
            }
        };

        registerReceiver(mInfoReceiver, filter);
    }

}
