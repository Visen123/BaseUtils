package com.yanyiyun.function.stepDown;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PreferencesHelper {

    public static final String APP_SHARE="step_share_prefs";
    public static final String LOGIN_FIRST_GET_STEP="login_first_get_step";

    /**
     * 计步器上次返回的步数
     */
    public static final String LAST_SENSOR_TIME="last_sensor_time";
    public static final String NAME="front_name";

    private static SharedPreferences getSharedPreferences(Context context){
        return context.getSharedPreferences(APP_SHARE,0);
    }

    /**
     * 保存计步器最后返回的步数
     * @param context
     * @param stepData
     */
    public static void saveLastSensorStep(Context context,StepData stepData){
        Gson gson=new Gson();
        Type type=new TypeToken<StepData>(){}.getType();
        String json=gson.toJson(stepData,type);
        getSharedPreferences(context).edit().putString(getName(context)+LAST_SENSOR_TIME,json).apply();
    }

    /**
     * 获得计步器最后返回的步数
     * @param context
     * @return
     */
    public static StepData getLastSensorStep(Context context){
        Gson gson=new Gson();
        Type type=new TypeToken<StepData>(){}.getType();
        String json=getSharedPreferences(context).getString(getName(context)+LAST_SENSOR_TIME,null);
        if(json!=null){
            StepData stepData=gson.fromJson(json,type);
            return stepData;
        }
        return null;
    }

    /**
     * 保存步数到本地的前缀
     * @param name
     */
    public static void saveName(Context context,String name){
        getSharedPreferences(context).edit().putString(NAME,name).apply();
    }

    public static String getName(Context context){
        return getSharedPreferences(context).getString(NAME,"");
    }

    /**
     * 保存是否是登录首次获取步数
     * @param context
     * @param isLogin 是否登出
     */
    public static void setLoginFirstGetStep(Context context,boolean isLogin){
        getSharedPreferences(context).edit().putBoolean(getName(context)+LOGIN_FIRST_GET_STEP,
                isLogin).commit();
    }

    /**
     * 获取是否是登录首次获取步数
     * @param context
     * @return
     */
    public static boolean getLoginFirstGetStep(Context context){
        return getSharedPreferences(context).getBoolean(getName(context)+LOGIN_FIRST_GET_STEP,
                true);
    }
}
