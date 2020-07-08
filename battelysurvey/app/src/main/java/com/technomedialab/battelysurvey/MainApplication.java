package com.technomedialab.battelysurvey;

import android.app.Application;
import android.util.Log;
import android.content.Context;
import android.content.SharedPreferences;

public class MainApplication extends Application {

    float[] sensorValues;
    private int blevelflg;
    private String token;
    private String slacktoken;
    private String slackchannel[];
    private String selectChannel;
    /** アプリ設定保存用Preferences */
    private SharedPreferences mSettingData;
    /** 定数 - アプリ状態保存用Preferences名 */
    private static final String SETTING_DATA_PREF = "SETTING_DATA";
    /** 定数 - 保存名：測定間隔　*/
    private static final String MIN_INTERVAL = "MIN_INTERVAL";
    /** 定数 - 保存名：照度設定　*/
    private static final String LIGHT_SENSOR_FLG = "LIGHT_SENSOR_FLG";
    /** 定数 - 保存名：チャンネル　*/
    private static final String SELECT_CHANNEL = "SELECT_CHANNEL";
    /** 定数 - 保存名：トークン　*/
    private static final String SELECT_TPKEN = "SELECT_TPKEN";


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

    //Slackトークン取得
    public String getToken() {
        //Log.d("トークンゲット",token);
//        return token;
        return mSettingData.getString(SELECT_TPKEN,"");
    }
    //slackトークンセット
    public void setToken(String values) {
//        token = values;
//        Log.d("トークンセット",token);
        SharedPreferences.Editor editor = mSettingData.edit();
        editor.putString(SELECT_TPKEN, values);
        editor.apply();
    }

    //選択されたチャンネルを取得
    public String getSelectChannel() {
//        return selectChannel;
        return mSettingData.getString(SELECT_CHANNEL,"");
    }
    //選択されたチャンネルをセット
    public void setSelectChannel(String values) {
        SharedPreferences.Editor editor = mSettingData.edit();
        editor.putString(SELECT_CHANNEL, values);
        editor.apply();
    }

//    //Slackトークン取得
//    public String getSlackToken() {
//        //Log.d("トークンゲット",token);
//        return slacktoken;
//    }
//    //slackトークンセット
//    public void setSlackToken(String values) {
//        slacktoken = values;
////        Log.d("トークンセット",token);
//    }

    //Slackチャンネル取得
    public String[] getChannel() {
        //Log.d("トークンゲット",token);
        return slackchannel;
    }
    //slackチェンネルセット
    public void setChannel(String[] values) {
        slackchannel = values;
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