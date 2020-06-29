package com.technomedialab.battelysurvey;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.telephony.CellInfo;
import android.telephony.CellSignalStrength;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import static java.lang.String.valueOf;

public class AlarmReceiver extends BroadcastReceiver {

    private LogSave tsl;
    private StringBuilder logStr;
    private OkHttpPost postTask;
    private int blevelflg;
    private MobileNetworkStatus mns;
    private WifiService ws;
    private MemoryAcquisition ma;
    private BatteryStatus bs;
    private int blevel;;
    private NetworkStatus ns;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (logStr == null) {
            logStr = new StringBuilder();
        }

        //端末の状態を取得
        if(bs == null) {
            bs = new BatteryStatus();
        }
        blevel = bs.baInfo(logStr,context);

        MainApplication mainApp = (MainApplication) context.getApplicationContext();

        //照度ONの場合、値を取得
        if (mainApp.getLightSensorFlg()) {
            if(mainApp.getSensorValues() != null) {
                float[] sensorValues = mainApp.getSensorValues();
                Log.d(LogUtility.TAG(this), "照度" + Const.CSV_BREAK + sensorValues[0]);
                logStr.append(GetTimestamp.getNowDate() + "照度" + Const.CSV_BREAK + sensorValues[0] + "\n");
            }else{
                Log.d(LogUtility.TAG(this), "赤外線センサー非搭載");
                logStr.append(GetTimestamp.getNowDate() + "赤外線センサー非搭載\n");
            }
        }


        //Slackに端末名と電池残量を通知
        if(postTask == null) {
            postTask = new OkHttpPost(mainApp.getToken());
        }
        blevelflg = mainApp.getBlevelflg();

        if (mainApp != null) {
            //電池残量が80%を超えた場合通知する
            if (blevel == 80 && blevelflg != 1) {
                mainApp.setBlevelflg(1);
                postTask.execute(valueOf(blevel));
            } else if (blevel == 81 && blevelflg != 2) {
                mainApp.setBlevelflg(2);
                postTask.execute(valueOf(blevel));
            } else if (blevel == 82 && blevelflg != 3) {
                mainApp.setBlevelflg(3);
                postTask.execute(valueOf(blevel));
            } else if (blevel == 83 && blevelflg != 4) {
                mainApp.setBlevelflg(4);
                postTask.execute(valueOf(blevel));
            } else if (blevel == 90 && blevelflg != 5) {
                mainApp.setBlevelflg(5);
                postTask.execute(valueOf(blevel));
            } else if (blevel == 100 && blevelflg != 6) {
                mainApp.setBlevelflg(6);
                postTask.execute(valueOf(blevel));
            } else if (blevel <= 79) {
                mainApp.setBlevelflg(0);
            } else {
            }
        }

        //Wi-Fiの電波受信強度取得
        if(ws == null) {
            ws = new WifiService();
        }
        ws.wifisignalstrength(logStr,context);


        //各電波強度を取得
        if(mns == null) {
            mns = new MobileNetworkStatus();
        }
        mns.cellInfo(logStr,context);


        //メモリ取得
        if (ma == null) {
            ma = new MemoryAcquisition();
        }
        ma.getmemory(logStr);


        //輝度取得　0-255
        String brightness = Settings.System.getString(context.getContentResolver(), "screen_brightness");
        Log.d(LogUtility.TAG(this), "輝度" + Const.CSV_BREAK + brightness);
        logStr.append(GetTimestamp.getNowDate() + "輝度" + Const.CSV_BREAK + brightness + "\n");


        //電波状態取得
        if(ns == null){
            ns = new NetworkStatus();
        }
        ns.getStatus(logStr,context);


        //Android10で取得可能な端末の熱状態(0:正常 ～ 7:電源OFFが必要）
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
            PowerManager pm = ( PowerManager ) context.getSystemService(Context.POWER_SERVICE);
            int pmanagernum = pm.getCurrentThermalStatus();
            Log.d(LogUtility.TAG(this), "端末の熱状態" + Const.CSV_BREAK + valueOf(pmanagernum));
        }


        //ログ保存
        if (tsl == null) {
            tsl = new LogSave();
        }
        tsl.textsavelog(context, logStr);

        // ログバッファクリア
        logStr.delete(0, logStr.length());

        //再度呼び出し
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        AlarmManager am = ( AlarmManager ) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        am.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis() +
//                        Const.MIN_INTERVAL,
                        mainApp.getMinInterval(),
                PendingIntent.getBroadcast(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT));

    }

}