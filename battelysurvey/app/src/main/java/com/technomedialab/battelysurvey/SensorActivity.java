package com.technomedialab.battelysurvey;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class SensorActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager manager;

    // 照度センサー
    private Sensor lightSensor;

    // 表示用
    //private TextView textview;

    private StringBuilder logStr;
    private LogSave tsl;

    private MainApplication mainApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_home);

        mainApp = (MainApplication) this.getApplication();

        /*----------他に取得可能なセンサー-----------
        *TYPE_ACCELEROMETER :加速度センサ
        *TYPE_ALL           :全部のセンサを指定
        *TYPE_GYROSCOPE     :ジャイロスコープ
        *TYPE_LIGHT         :照度センサ
        *TYPE_MAGNETIC_FIELD:地磁気センサ
        *TYPE_ORIENTATION   :傾きセンサ(非推奨定数)
        *TYPE_PRESSURE      :加圧センサ
        *TYPE_PROXIMITY     :接近センサ
        *TYPE_TEMPERATURE   :温度センサ
        -------------------------------------------*/

        //センサーマネージャを取得
        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //センサマネージャから照度センサーを指定
        lightSensor = manager.getDefaultSensor(Sensor.TYPE_LIGHT);

        //照度センサーのリスナー設定
        setLightSensor(mainApp.getLightSensorFlg());

        //textview = (TextView)findViewById(R.id.textView);
        //textview.setText("照度センサー　＝　");

        logStr = new StringBuilder();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.textView) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // accuracy に変更があった時の処理
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        long currentTimeMillis = System.currentTimeMillis();

        Sensor sensor = event.sensor;
        float[] values = event.values;
        long timestamp = event.timestamp;

        if (mainApp != null) {
            mainApp.setSensorValues(values);
        }

//        // センサー
//        if(sensor.getType() == Sensor.TYPE_LIGHT){
//            //Log.d("SENSOR_DATA", "TYPE_LIGHT = " + String.valueOf(values[0]));
//            //textview.setText("照度センサー　＝　" + String.valueOf(values[0]));
//            Log.d(LogUtility.TAG(this), "照度：" + String.valueOf(values[0]));
//            logStr.append(GetTimestamp.getNowDate() + "照度：" + String.valueOf(values[0]) + "\n");
//
//            //ログ保存
//            if (tsl == null) {
//                tsl = new LogSave();
//            }
//            tsl.textsavelog(this, logStr);
//            // ログバッファクリア
//            logStr.delete(0, logStr.length());
//        }


    }

    //照度センサーのリスナー設定
    public void setLightSensor (Boolean flg){
        if (flg) {
            // リスナー登録
            manager.registerListener(this,
                    lightSensor,
                    SensorManager.SENSOR_DELAY_FASTEST);

        }else{
            // リスナー解除
            manager.unregisterListener(this,lightSensor);

        }

    }

}
