package com.technomedialab.battelysurvey;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.util.Log;

public class BatteryStatus {

    public int baInfo(StringBuilder logStr, Context context) {

        Intent batteryStatus;
        int blevel;
        IntentFilter ifilter = null;


        ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        batteryStatus = context.registerReceiver(null, ifilter);

        // Battery Level
        blevel = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        Log.d(LogUtility.TAG(this), "電池残量" + Const.CSV_BREAK + blevel + "%");
        logStr.append(GetTimestamp.getNowDate() + "電池残量" + Const.CSV_BREAK + blevel + ",%" + "\n");


        // Battery Health
        int bh = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_HEALTH, -1);
        Log.d(LogUtility.TAG(this), "バッテリー状態" + Const.CSV_BREAK + batteryHealth(bh));
        logStr.append(GetTimestamp.getNowDate() + "バッテリー状態" + Const.CSV_BREAK + batteryHealth(bh) + "\n");

        // Battery plugged
        int bpl = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_PLUGGED, -1);
        Log.d(LogUtility.TAG(this), "電源接続状態" + Const.CSV_BREAK + batteryPluggCheck(bpl));
        logStr.append(GetTimestamp.getNowDate() + "電源接続状態" + Const.CSV_BREAK + batteryPluggCheck(bpl) + "\n");

        // Battery status
        int bst = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_STATUS, -1);
        Log.d(LogUtility.TAG(this), "充電状態" + Const.CSV_BREAK + status(bst));
        logStr.append(GetTimestamp.getNowDate() + "充電状態" + Const.CSV_BREAK + status(bst) + "\n");

        // Battery technology
        String bte = batteryStatus.getStringExtra(
                BatteryManager.EXTRA_TECHNOLOGY);
        Log.d(LogUtility.TAG(this), "電池の種類" + Const.CSV_BREAK + bte);
        logStr.append(GetTimestamp.getNowDate() + "電池の種類" + Const.CSV_BREAK + bte + "\n");

        // temperature
        int btp = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_TEMPERATURE, -1);
        Log.d(LogUtility.TAG(this), "バッテリー温度" + Const.CSV_BREAK + (float) btp / 10);
        logStr.append(GetTimestamp.getNowDate() + "バッテリー温度 " + Const.CSV_BREAK + (float) btp / 10 + "\n");

        // Voltage
        int bv = batteryStatus.getIntExtra(
                BatteryManager.EXTRA_VOLTAGE, -1);
        Log.d(LogUtility.TAG(this), "電圧" + Const.CSV_BREAK + (float) bv / 1000);
        logStr.append(GetTimestamp.getNowDate() + "電圧" + Const.CSV_BREAK + (float) bv / 1000 + "\n");

        return blevel;
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
            status = "充電されてない";
        } else if (bst == BatteryManager.BATTERY_STATUS_UNKNOWN) {
            status = "不明";
        }

        return status;
    }

}
