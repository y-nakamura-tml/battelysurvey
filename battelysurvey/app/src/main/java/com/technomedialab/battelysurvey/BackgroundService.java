package com.technomedialab.battelysurvey;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

import androidx.annotation.Nullable;

import static com.technomedialab.battelysurvey.LogUtility.TAG;

public class BackgroundService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG(this),"onStartCommand");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        AlarmManager am = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(getApplicationContext(), AlarmReceiver.class);
        am.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis()+
                        Const.MIN_INTERVAL,
                PendingIntent.getBroadcast(getApplicationContext(),0,i,PendingIntent.FLAG_UPDATE_CURRENT));


        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
