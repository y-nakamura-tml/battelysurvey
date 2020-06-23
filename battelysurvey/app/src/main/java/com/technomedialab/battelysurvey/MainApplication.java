package com.technomedialab.battelysurvey;

import android.app.Application;
import android.util.Log;

public class MainApplication extends Application {

    float[] sensorValues;
    private int blevelflg;
    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    //照度
//    public float[] getSensorValues() {
//        return sensorValues;
//    }
//    public void setSensorValues(float[] values) {
//        sensorValues = values;
//    }

    //Slack送信フラグ
    public int getBlevelflg() {
        return blevelflg;
    }
    public void setBlevelflg(int values) {
        blevelflg = values;
    }

    //Slackトークン
    public String getToken() {
        //Log.d("トークンゲット",token);
        return token;
    }
    public void setToken(String values) {
        token = values;
        Log.d("トークンセット",token);
    }



}