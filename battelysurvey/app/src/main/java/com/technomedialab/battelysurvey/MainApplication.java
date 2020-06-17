package com.technomedialab.battelysurvey;

import android.app.Application;

public class MainApplication extends Application {

    float[] sensorValues;
    private int blevelflg;

    @Override
    public void onCreate() {
        super.onCreate();
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
}