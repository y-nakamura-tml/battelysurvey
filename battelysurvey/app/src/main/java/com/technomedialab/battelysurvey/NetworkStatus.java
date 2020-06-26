package com.technomedialab.battelysurvey;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

public class NetworkStatus {

    void getStatus(StringBuilder logStr, Context context) {

        //電波状態確認
        ConnectivityManager cm = ( ConnectivityManager ) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork != null) {
            Log.d(LogUtility.TAG(this), "ネットワーク接続" + Const.CSV_BREAK + "可");
            logStr.append(GetTimestamp.getNowDate() + "ネットワーク接続" + Const.CSV_BREAK + "可" + "\n");
        } else {
            Log.d(LogUtility.TAG(this), "ネットワーク接続" + Const.CSV_BREAK + "不可 ");
            logStr.append(GetTimestamp.getNowDate() + "ネットワーク接続" + Const.CSV_BREAK + "不可 " + "\n");
        }

        NetworkInfo info = cm.getActiveNetworkInfo();
        String NetworkStatus = "";

        if (info != null) {
            switch (info.getType()) {
                case ConnectivityManager.TYPE_WIFI:         // Wifi
                    NetworkStatus = "Wi-Fiに接続中";
                    break;
                case ConnectivityManager.TYPE_MOBILE:
                    switch (info.getSubtype()) {
                        case TelephonyManager.NETWORK_TYPE_LTE:
                            NetworkStatus = "LTEに接続中";
                            break;
                        default:
                            NetworkStatus = "3Gに接続中";
                            break;
                    }
                    break;
                default:
                    NetworkStatus = "接続状態不明";
            }
        } else {
            NetworkStatus = "機内モードかも";
        }

        Log.d(LogUtility.TAG(this), "ネットワーク接続先" + Const.CSV_BREAK + NetworkStatus);
        logStr.append(GetTimestamp.getNowDate() + "ネットワーク接続先" + Const.CSV_BREAK + NetworkStatus + "\n");
    }

}
