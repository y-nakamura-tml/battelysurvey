package com.technomedialab.battelysurvey;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MainApplication extends Application {

    float[] sensorValues;
    private int blevelflg;
    /** アプリ設定保存用Preferences */
    private SharedPreferences mSettingData;
    /** 定数 - アプリ状態保存用Preferences名 */
    private static final String SETTING_DATA_PREF = "SETTING_DATA";
    /** 定数 - 保存名：測定間隔　*/
    private static final String MIN_INTERVAL = "MIN_INTERVAL";
    /** 定数 - 保存名：測定間隔　*/
    private static final String LIGHT_SENSOR_FLG = "LIGHT_SENSOR_FLG";

    @Override
    public void onCreate() {
        super.onCreate();

        mSettingData = getSharedPreferences(SETTING_DATA_PREF, Context.MODE_PRIVATE);

    }

    //照度
    public float[] getSensorValues() {
        return sensorValues;
    }
    public void setSensorValues(float[] values) {
        sensorValues = values;
    }

    //Slack送信フラグ
    public int getBlevelflg() {
        return blevelflg;
    }
    public void setBlevelflg(int values) {
        blevelflg = values;
    }

    //測定間隔
    public Long getMinInterval() { return mSettingData.getLong(MIN_INTERVAL,Const.DEFAULT_MIN_INTERVAL); }
    public void setMinInterval(Long values) {
        SharedPreferences.Editor editor = mSettingData.edit();
        editor.putLong(MIN_INTERVAL, values);
        editor.apply();

    }
    //照度フラグ
    public Boolean getLightSensorFlg() { return mSettingData.getBoolean(LIGHT_SENSOR_FLG,true); }
    public void setLightSensorFlg(Boolean values) {
        SharedPreferences.Editor editor = mSettingData.edit();
        editor.putBoolean(LIGHT_SENSOR_FLG, values);
        editor.apply();

    }

}