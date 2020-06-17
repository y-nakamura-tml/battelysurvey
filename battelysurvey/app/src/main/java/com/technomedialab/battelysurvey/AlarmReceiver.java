package com.technomedialab.battelysurvey;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.CellSignalStrength;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.util.Calendar;
import java.util.List;


public class AlarmReceiver extends BroadcastReceiver {

    private Intent batteryStatus;
    private BatteryManager bManager;
    private WifiManager wManager;
    private TelephonyManager tManager;
    private IntentFilter ifilter;
    private LogSave tsl;
    private StringBuilder logStr;
    private NotificationChannel channel;
    private int blevel;
    private OkHttpPost postTask;
    private int blevelflg;
    private MainApplication mainApp;


    @Override
    public void onReceive(Context context, Intent intent) {

        if (ifilter == null) {

            ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        }
        batteryStatus = context.registerReceiver(null, ifilter);


        if (logStr == null) {
            logStr = new StringBuilder();
        }

        if (batteryStatus != null) {
            baInfo(logStr);
        }

        //照度の値を取得
        MainApplication mainApp = (MainApplication) context.getApplicationContext();

        float[] sensorValues = mainApp.getSensorValues();
        if (sensorValues != null){
            Log.d(LogUtility.TAG(this), "照度：" + String.valueOf(sensorValues[0]));
            logStr.append(GetTimestamp.getNowDate() + "照度：" + String.valueOf(sensorValues[0]) + "\n");

        }


        //Android8.0以降の端末で通知を送る
        if (Build.VERSION.SDK_INT >= 26) {


            ///////////////////////////////////
            ///////通知エリアに通知する/////////
            //////////////////////////////////
//                NotificationManager manager = ( NotificationManager )
//                        context.getSystemService(Context.NOTIFICATION_SERVICE);
//                if (channel == null) {
//                    channel = new NotificationChannel(
//                            // アプリでユニークな ID
//                            "channel_1",
//                            // ユーザが「設定」アプリで見ることになるチャンネル名
//                            "test_channel",
//                            // チャンネルの重要度（0 ~ 4）
//                            NotificationManager.IMPORTANCE_DEFAULT
//                    );
//                    // 通知時のライトの色
//                    channel.setLightColor(Color.GREEN);
//                    // ロック画面で通知を表示するかどうか
//                    channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
//                    // 端末にチャンネルを登録し、「設定」で見れるようにする
//                    manager.createNotificationChannel(channel);
//                }
//
//                // 通知チャンネルのIDを指定するコンストラクタが増えている
//                NotificationCompat.Builder builder
//                        = new NotificationCompat.Builder(context, "channel_1")
//                        .setContentTitle("通知")
//                        .setContentText("電池残量が80％を超えました！")
//                        .setSmallIcon(R.drawable.ic_notifications_black_24dp);
//                NotificationManagerCompat.from(context).notify(1, builder.build());

        }


        //Slackに端末名と電池残量を通知
        if (postTask == null) {
            postTask = new OkHttpPost();
        }
        blevelflg = mainApp.getBlevelflg();

        if (mainApp != null) {
            //電池残量が80%を超えた場合通知する
            if (blevel == 80 && blevelflg != 1) {
                mainApp.setBlevelflg(1);
                postTask.execute(String.valueOf(blevel));
            } else if (blevel == 81 && blevelflg != 2) {
                mainApp.setBlevelflg(2);
                postTask.execute(String.valueOf(blevel));
            } else if (blevel == 82 && blevelflg != 3) {
                mainApp.setBlevelflg(3);
                postTask.execute(String.valueOf(blevel));
            } else if (blevel == 83 && blevelflg != 4) {
                mainApp.setBlevelflg(4);
                postTask.execute(String.valueOf(blevel));
            } else if (blevel == 90 && blevelflg != 5) {
                mainApp.setBlevelflg(5);
                postTask.execute(String.valueOf(blevel));
            } else if (blevel == 100 && blevelflg != 6) {
                mainApp.setBlevelflg(6);
                postTask.execute(String.valueOf(blevel));
//            blevelflg = 6;
//            postTask.execute(String.valueOf(blevel));
            } else if (blevel <= 79) {
                mainApp.setBlevelflg(0);
            } else {
            }
        }

        if (wManager == null) {
            wManager = (WifiManager) context.getSystemService(context.WIFI_SERVICE);
        }

        if (tManager == null) {
            tManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            //Log.d(LogUtility.TAG(this), "tManager" + tManager);

        }
        cellInfo(logStr);

        // 以降は Lollipop と機種によるサポートの違いあり
        if (Build.VERSION.SDK_INT >= 21) {
            bManager = (BatteryManager) context.getSystemService(Context.BATTERY_SERVICE);

//            if (bManager != null) {
//                // batteryAgeing();
//            }
        }


        // アプリのメモリ情報を取得
        Runtime runtime = Runtime.getRuntime();

        if (runtime != null) {
            // トータルメモリ
            Log.d(LogUtility.TAG(this), "全メモリ[KB]: " + (int) (runtime.totalMemory() / 1024));
            logStr.append(GetTimestamp.getNowDate() + "全メモリ[KB]: " + (int) (runtime.totalMemory() / 1024) + "\n");

            // 空きメモリ
            Log.d(LogUtility.TAG(this), "空きメモリ[KB]: " + (int) (runtime.freeMemory() / 1024));
            logStr.append(GetTimestamp.getNowDate() + "空きメモリ[KB]: " + (int) (runtime.freeMemory() / 1024) + "\n");

            // 使用メモリ
            Log.d(LogUtility.TAG(this), "使用メモリ[KB]: " + (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024));
            logStr.append(GetTimestamp.getNowDate() + "使用メモリ[KB]: " + (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024) + "\n");

            // 使用メモリ
            Log.d(LogUtility.TAG(this), "Dalvikで使用できる最大メモリ[KB]: " + (int) (runtime.maxMemory() / 1024));
            logStr.append(GetTimestamp.getNowDate() + "Dalvikで使用できる最大メモリ[KB]: " + (int) (runtime.maxMemory() / 1024) + "\n");
        }

        //輝度取得　0-255
        String brightness = Settings.System.getString(context.getContentResolver(), "screen_brightness");
        Log.d(LogUtility.TAG(this), "輝度: " + brightness);
        logStr.append(GetTimestamp.getNowDate() + "輝度: " + brightness + "\n");


        //電波状態確認
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            Log.d(LogUtility.TAG(this), "ネットワーク接続: 可");
            logStr.append(GetTimestamp.getNowDate() + "ネットワーク接続: 可" + "\n");
        } else {
            Log.d(LogUtility.TAG(this), "ネットワーク接続: 不可 ");
            logStr.append(GetTimestamp.getNowDate() + "ネットワーク接続: 不可 " + "\n");
        }

        NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:         // Wifi
                    Log.d(LogUtility.TAG(this), "Wi-Fiに接続中");
                    logStr.append(GetTimestamp.getNowDate() + "Wi-Fiに接続中" + "\n");
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (info.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            Log.d(LogUtility.TAG(this), "LTEに接続中");        // LTE
                            logStr.append(GetTimestamp.getNowDate() + "LTEに接続中" + "\n");
                            break;
                        default:
                            Log.d(LogUtility.TAG(this), "3Gに接続中");         //
                            logStr.append(GetTimestamp.getNowDate() + "3Gに接続中" + "\n");
                            break;
                    }
                    break;
                default:
                    Log.d(LogUtility.TAG(this), "接続状態不明");
                    logStr.append(GetTimestamp.getNowDate() + "接続状態不明" + "\n");
            }
        } else {
            Log.d(LogUtility.TAG(this), "機内モードかも");
            logStr.append(GetTimestamp.getNowDate() + "機内モードかも" + "\n");

        }


        WifiInfo wInfo = wManager.getConnectionInfo();

        int rssi = wInfo.getRssi();
        int level = WifiManager.calculateSignalLevel(rssi, 5);
        Log.d(LogUtility.TAG(this), String.format("WiFi RSSI : %d / Level : %d/4", rssi, level));
        logStr.append(GetTimestamp.getNowDate() + String.format("WiFi RSSI : %d / Level : %d/4", rssi, level) + "\n");


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
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context.getApplicationContext(), AlarmReceiver.class);
        am.set(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis() +
                        Const.MIN_INTERVAL,
                PendingIntent.getBroadcast(context.getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT));

    }

    @SuppressLint("MissingPermission")
    private void cellInfo(StringBuilder logStr) {
        //List<CellInfo> cellInfoList;
        List<CellSignalStrength> signalInfoList;

        try {
            SignalStrength signalStrength = null;
            if (android.os.Build.VERSION.SDK_INT == android.os.Build.VERSION_CODES.P) {
                signalStrength = tManager.getSignalStrength();
                String[] parts = signalStrength.toString().split(" ");

                int CdmaDbm = signalStrength.getCdmaDbm();
                String mGsmSignalStrength = "mGsmSignalStrength:" + parts[1];
                String mGsmBitErrorRate = "mGsmBitErrorRate:" + parts[2];
                String cdmaDbm = "CdmaDbm:" + parts[3];
                String cdmaEcio = "CdmaEcio:" + parts[4];
                String evdoDbm = "EvdoDbm:" + parts[5];
                String evdoEcio = "EvdoEcio:" + parts[6];
                String evdoSnr = "EvdoSnr:" + parts[7];
                String lteSignalStrength = "LteSignalStrength:" + parts[8];
                String lteRsrp = "LteRsrp:" + parts[9];
                String lteRsrq = "LteRsrq:" + parts[10];
                String lteRssnr = "LteRssnr:" + parts[11];
                String lteCqi = "LteCqi:" + parts[12];
                String LteRsrpBoost = "LteRsrpBoost:" + parts[13];
                String mTdScdmaRscp = "mTdScdmaRscp:" + parts[14];
                String WcdmaSignalStrength = "WcdmaSignalStrength:" + parts[15];
                String WcdmaRscpAsu = "WcdmaRscpAsu:" + parts[16];
                String WcdmaRscp = "WcdmaRscp:" + parts[17];


                Log.d(LogUtility.TAG(this), "3Gの電波強度" + CdmaDbm);
                logStr.append(GetTimestamp.getNowDate() + "3Gの電波強度" + CdmaDbm + "\n");

//                Log.d(LogUtility.TAG(this), mGsmSignalStrength);
//                logStr.append(GetTimestamp.getNowDate() + mGsmSignalStrength + "\n");
//                Log.d(LogUtility.TAG(this), mGsmBitErrorRate);
//                logStr.append(GetTimestamp.getNowDate() + mGsmBitErrorRate + "\n");
//                Log.d(LogUtility.TAG(this), cdmaDbm);
//                logStr.append(GetTimestamp.getNowDate() + cdmaDbm + "\n");
//                Log.d(LogUtility.TAG(this), cdmaEcio);
//                logStr.append(GetTimestamp.getNowDate() + cdmaEcio + "\n");
//                Log.d(LogUtility.TAG(this), evdoDbm);
//                logStr.append(GetTimestamp.getNowDate() + evdoDbm + "\n");
//                Log.d(LogUtility.TAG(this), evdoEcio);
//                logStr.append(GetTimestamp.getNowDate() + evdoEcio + "\n");
//                Log.d(LogUtility.TAG(this), evdoSnr);
//                logStr.append(GetTimestamp.getNowDate() + evdoSnr + "\n");
//                Log.d(LogUtility.TAG(this), evdoEcio);
//                logStr.append(GetTimestamp.getNowDate() + evdoEcio + "\n");
//                Log.d(LogUtility.TAG(this), lteSignalStrength);
//                logStr.append(GetTimestamp.getNowDate() + lteSignalStrength + "\n");
                Log.d(LogUtility.TAG(this), lteRsrp);
                logStr.append(GetTimestamp.getNowDate() + lteRsrp + "\n");
                Log.d(LogUtility.TAG(this), lteRsrq);
                logStr.append(GetTimestamp.getNowDate() + lteRsrq + "\n");
//                Log.d(LogUtility.TAG(this), lteRssnr);
//                logStr.append(GetTimestamp.getNowDate() + lteRssnr + "\n");
//                Log.d(LogUtility.TAG(this), lteCqi);
//                logStr.append(GetTimestamp.getNowDate() + lteCqi + "\n");
//                Log.d(LogUtility.TAG(this), LteRsrpBoost);
//                logStr.append(GetTimestamp.getNowDate() + LteRsrpBoost + "\n");
//                Log.d(LogUtility.TAG(this), mTdScdmaRscp);
//                logStr.append(GetTimestamp.getNowDate() + mTdScdmaRscp + "\n");
//                Log.d(LogUtility.TAG(this), WcdmaSignalStrength);
//                logStr.append(GetTimestamp.getNowDate() + WcdmaSignalStrength + "\n");
//                Log.d(LogUtility.TAG(this), WcdmaRscpAsu);
//                logStr.append(GetTimestamp.getNowDate() + WcdmaRscpAsu + "\n");
//                Log.d(LogUtility.TAG(this), WcdmaRscp);
//                logStr.append(GetTimestamp.getNowDate() + WcdmaRscp + "\n");

                Log.d(LogUtility.TAG(this), "OS9: " + signalStrength.toString());
                logStr.append(GetTimestamp.getNowDate() + "OS9: " + signalStrength.toString() + "\n");


            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) {
                signalStrength = tManager.getSignalStrength();
                String[] strSignalStrength = signalStrength.toString().split(",");
                String[] mWcdmaData = strSignalStrength[2].split(" ");
                String[] mLteData = strSignalStrength[4].split(" ");

                Log.d(LogUtility.TAG(this),  "mWcdma:" + mWcdmaData[1].toString());
                logStr.append(GetTimestamp.getNowDate() + "mWcdma:" + mWcdmaData[1].toString() + "\n");
//                Log.d(LogUtility.TAG(this),  "mWcdma:" + mWcdmaData[2].toString());
//                logStr.append(GetTimestamp.getNowDate() + "mWcdma:" + mWcdmaData[2].toString() + "\n");
//                Log.d(LogUtility.TAG(this), "mWcdma:" + mWcdmaData[3].toString());
//                logStr.append(GetTimestamp.getNowDate() + "mWcdma:" + mWcdmaData[3].toString() + "\n");
//                Log.d(LogUtility.TAG(this), "mWcdma:" + mWcdmaData[4].toString());
//                logStr.append(GetTimestamp.getNowDate() + "mWcdma:" + mWcdmaData[4].toString() + "\n");
//                Log.d(LogUtility.TAG(this), "mWcdma:" + mWcdmaData[5].toString());
//                logStr.append(GetTimestamp.getNowDate() + "mWcdma:" + mWcdmaData[5].toString() + "\n");

                Log.d(LogUtility.TAG(this), "mLte:" + mLteData[1].toString());
                logStr.append(GetTimestamp.getNowDate() + "mLte:" + mLteData[1].toString() + "\n");
                Log.d(LogUtility.TAG(this), "mLte:" + mLteData[2].toString());
                logStr.append(GetTimestamp.getNowDate() + "mLte:" + mLteData[2].toString() + "\n");
//                Log.d(LogUtility.TAG(this), "mLte:" + mLteData[3].toString());
//                logStr.append(GetTimestamp.getNowDate() + "mLte:" + mLteData[3].toString() + "\n");
//                Log.d(LogUtility.TAG(this), "mLte:" + mLteData[4].toString());
//                logStr.append(GetTimestamp.getNowDate() + "mLte:" + mLteData[4].toString() + "\n");
//                Log.d(LogUtility.TAG(this),   "mLte:" + mLteData[5].toString());
//                logStr.append(GetTimestamp.getNowDate() +  "mLte:" + mLteData[5].toString() + "\n");
//                Log.d(LogUtility.TAG(this),   "mLte:" + mLteData[6].toString());
//                logStr.append(GetTimestamp.getNowDate() +  "mLte:" + mLteData[6].toString() + "\n");
                Log.d(LogUtility.TAG(this), "mLte:" + mLteData[7].toString());
                logStr.append(GetTimestamp.getNowDate() + "mLte:" + mLteData[7].toString() + "\n");

                Log.d(LogUtility.TAG(this), "signalStrength" + signalStrength.toString());
                logStr.append(GetTimestamp.getNowDate() + "signalStrength" + signalStrength.toString() + "\n");

                signalInfoList = signalStrength.getCellSignalStrengths();
                for (CellSignalStrength signalInfo : signalInfoList) {
                    //Log.d(LogUtility.TAG(this), "signalInfo:" + signalInfo.toString());
                    //logStr.append(GetTimestamp.getNowDate() + "signalInfo:" + signalInfo.toString() + "\n");
//                    String ssignal = signalInfo.toString();
//                    String[] parts = ssignal.split(" ");
//                    Log.d(LogUtility.TAG(this), "種別:" + parts[0]);
//                    logStr.append(GetTimestamp.getNowDate() + "種別:" + parts[0] + "\n");
//                    Log.d(LogUtility.TAG(this), "電力強度（RSSI）(-109...-53,2147483647):" + parts[1]);
//                    logStr.append(GetTimestamp.getNowDate() + "電力強度（RSSI）(-109...-53,2147483647):" + parts[1] + "\n");
//                    Log.d(LogUtility.TAG(this), "受信電力（RSRP）(-109...-53,2147483647):" + parts[2]);
//                    logStr.append(GetTimestamp.getNowDate() + "受信電力（RSRP）(-109...-53,2147483647):" + parts[2] + "\n");
//                    Log.d(LogUtility.TAG(this), "受信品質（RSRP）(-109...-53,2147483647):" + parts[3]);
//                    logStr.append(GetTimestamp.getNowDate() + "受信品質（RSRP）(-109...-53,2147483647):" + parts[3] + "\n");
                }
            }

        } catch (Exception e) {
            Log.d("SignalStrength", "+++++++++++++++++++++++++++++++ null array spot 3: " + e);
        }
    }


    private void baInfo(StringBuilder logStr) {

        // Battery Level
        blevel = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_LEVEL, -1);
        Log.d(LogUtility.TAG(this), "電池残量: " + String.valueOf(blevel) + "%");
        logStr.append(GetTimestamp.getNowDate() + "電池残量: " + String.valueOf(blevel) + "%" + "\n");


        // Battery Health
        int bh = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_HEALTH, -1);
        Log.d(LogUtility.TAG(this), "バッテリー状態: " + batteryHealth(bh));
        logStr.append(GetTimestamp.getNowDate() + "バッテリー状態: " + batteryHealth(bh) + "\n");

        // Battery plugged
        int bpl = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_PLUGGED, -1);
        Log.d(LogUtility.TAG(this), "電源接続状態: " + batteryPluggCheck(bpl));
        logStr.append(GetTimestamp.getNowDate() + "電源接続状態: " + batteryPluggCheck(bpl) + "\n");

        // Battery status
        int bst = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_STATUS, -1);
        Log.d(LogUtility.TAG(this), "充電状態: " + status(bst));
        logStr.append(GetTimestamp.getNowDate() + "充電状態: " + status(bst) + "\n");

        // Battery technology
        String bte = batteryStatus.getStringExtra(
                BatteryManager.EXTRA_TECHNOLOGY);
        Log.d(LogUtility.TAG(this), "電池の種類: " + bte);
        logStr.append(GetTimestamp.getNowDate() + "電池の種類: " + bte + "\n");

        // temperature
        int btp = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, -1);
        Log.d(LogUtility.TAG(this), "バッテリー温度: " + String.valueOf((float) btp / 10));
        logStr.append(GetTimestamp.getNowDate() + "バッテリー温度: " + String.valueOf((float) btp / 10) + "\n");

        // Voltage
        int bv = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_VOLTAGE, -1);
        Log.d(LogUtility.TAG(this), "電圧: " + String.valueOf((float) bv / 1000));
        logStr.append(GetTimestamp.getNowDate() + "電圧: " + String.valueOf((float) bv / 1000) + "\n");

    }

    private String batteryHealth(int bh) {
        String health = null;

        if (bh == BatteryManager.BATTERY_HEALTH_GOOD) {
            health = "良好";
        } else if (bh == BatteryManager.BATTERY_HEALTH_DEAD) {
            health = "壊れてる";
        } else if (bh == BatteryManager.BATTERY_HEALTH_COLD) {
            health = "低温";
        } else if (bh == BatteryManager.BATTERY_HEALTH_OVERHEAT) {
            health = "温度異常";
        } else if (bh == BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE) {
            health = "電圧異常";
        } else if (bh == BatteryManager.BATTERY_HEALTH_UNKNOWN) {
            health = "不明";
        } else if (bh == BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE) {
            health = "不明";
        }

        return health;
    }

    private String batteryPluggCheck(int bpl) {
        String plugged = "接続無し";
        if (bpl == BatteryManager.BATTERY_PLUGGED_AC) {
            plugged = "ACアダプタ充電";
        } else if (bpl == BatteryManager.BATTERY_PLUGGED_USB) {
            plugged = "USB充電";
        } else if (bpl == BatteryManager.BATTERY_PLUGGED_WIRELESS) {
            plugged = "ワイヤレス充電器";
        }

        return plugged;
    }

    private String batteryPresent(boolean bpr) {
        String present;
        if (bpr) {
            present = "PRESENT";
        } else {
            present = "NOT PRESENT";
        }
        return present;
    }

    private String status(int bst) {
        String status = "?";
        if (bst == BatteryManager.BATTERY_STATUS_CHARGING) {
            status = "充電中";
        } else if (bst == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            status = "放電中";
        } else if (bst == BatteryManager.BATTERY_STATUS_FULL) {
            status = "充電完了";
        } else if (bst == BatteryManager.BATTERY_STATUS_NOT_CHARGING) {
            status = "";
        }
        return status;

    }

}