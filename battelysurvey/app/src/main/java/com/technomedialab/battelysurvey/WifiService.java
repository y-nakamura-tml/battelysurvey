package com.technomedialab.battelysurvey;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class WifiService {

    private WifiManager wManager;

    void wifisignalstrength(StringBuilder logStr,Context context){
        //Wi-Fiの電波受信強度取得
        if (wManager == null) {
            wManager = ( WifiManager ) context.getSystemService(context.WIFI_SERVICE);
        }
        WifiInfo wInfo = wManager.getConnectionInfo();
        int rssi = wInfo.getRssi();
        int level = WifiManager.calculateSignalLevel(rssi, 5);
        Log.d(LogUtility.TAG(this), String.format("WiFi RSSI : %d / Level : %d/4", rssi, level));
        logStr.append(GetTimestamp.getNowDate() + String.format("WiFi RSSI" + Const.CSV_BREAK +  "%d ", rssi) + "\n");
        logStr.append(GetTimestamp.getNowDate() + String.format("WiFi Level" + Const.CSV_BREAK + "'" + "%d/4",  level) + "\n");
    }
}
